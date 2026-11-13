import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, catchError, finalize, map, switchMap, tap, throwError } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { TipoUsuario, Usuario, UsuarioRegistro } from '../models/usuario.model';
import { AvisoService } from './aviso.service';

const TOKEN_KEY = 'alojame_token';

interface LoginResponse {
  token: string;
}

interface ClaimsToken {
  sub: string;
  roles: Array<{ authority: string }>;
  exp: number;
  iat: number;
}

/// Fuente de verdad del estado de sesion. El rol se decodifica del propio JWT
/// (claim "roles", confirmado contra el backend real: [{authority:"ROLE_X"}]),
/// sin esperar ninguna llamada HTTP -- asi los guards son 100% sincronicos. El
/// perfil completo (nombre, email, etc.) se carga aparte via /Usuario/me, solo
/// para mostrarlo en pantalla, nunca para decidir permisos.
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly aviso = inject(AvisoService);
  /// Timer que cierra la sesion cuando vence el JWT (el backend no avisa: con
  /// un token vencido responde 403 a todo, igual que a un anonimo).
  private timerVencimiento: ReturnType<typeof setTimeout> | null = null;
  private verificando = false;

  private readonly tokenSignal = signal<string | null>(leerTokenGuardado());
  private readonly usuarioSignal = signal<Usuario | null>(null);

  readonly token = this.tokenSignal.asReadonly();
  readonly usuario = this.usuarioSignal.asReadonly();
  readonly estaLogueado = computed(() => this.tokenSignal() !== null);
  readonly rol = computed<TipoUsuario | null>(() => {
    const token = this.tokenSignal();
    return token ? extraerRolDeToken(token) : null;
  });

  constructor() {
    // queueMicrotask: si ya hay token guardado (ej. recargar la pagina con
    // sesion activa), NO se puede llamar a cargarPerfil() aca adentro de forma
    // sincronica -- el interceptor hace inject(AuthService) y, como el
    // constructor todavia no termino de devolver, Angular lo detecta como
    // dependencia circular contra si mismo (NG0200) y la sesion se pierde.
    // Diferir un microtask alcanza: para entonces el constructor ya devolvio
    // y el injector tiene la instancia completamente registrada.
    const guardado = this.tokenSignal();
    if (guardado) {
      if (tokenVencido(guardado)) {
        this.cerrarSesion();
      } else {
        this.programarVencimiento(guardado);
        queueMicrotask(() => this.cargarPerfil().subscribe({ error: () => {} }));
      }
    }
  }

  login(identificador: string, password: string): Observable<void> {
    return this.http.post<LoginResponse>(`${API_BASE_URL}/auth/login`, { identificador, password }).pipe(
      tap((res) => this.guardarSesion(res.token)),
      switchMap(() => this.cargarPerfil()),
      map(() => void 0),
    );
  }

  registrar(dto: UsuarioRegistro): Observable<string> {
    return this.http.post(`${API_BASE_URL}/Usuario/registrar`, dto, { responseType: 'text' });
  }

  verificar(token: string): Observable<string> {
    return this.http.get(`${API_BASE_URL}/auth/verificar`, { params: { token }, responseType: 'text' });
  }

  reenviarVerificacion(email: string): Observable<string> {
    return this.http.post(`${API_BASE_URL}/auth/reenviar-verificacion`, { email }, { responseType: 'text' });
  }

  cerrarSesion(): void {
    try {
      localStorage.removeItem(TOKEN_KEY);
    } catch {
      /* almacenamiento no disponible (modo privado, etc.) -- igual limpiamos el estado en memoria */
    }
    this.tokenSignal.set(null);
    this.usuarioSignal.set(null);
    if (this.timerVencimiento) clearTimeout(this.timerVencimiento);
    this.timerVencimiento = null;
  }

  /// Cierra la sesion porque dejo de servir (vencio, o la cuenta fue
  /// desactivada), avisa y manda a iniciar sesion volviendo a donde estaba.
  expirar(mensaje = 'Tu sesión venció. Iniciá sesión de nuevo.'): void {
    if (!this.tokenSignal()) return;
    // Al recargar la pagina el router todavia no termino de navegar (router.url
    // sigue en "/"): ahi la direccion real esta en la barra del navegador.
    const volverA = this.router.navigated ? this.router.url : location.pathname + location.search;
    this.cerrarSesion();
    this.aviso.mostrar(mensaje);
    this.router.navigate(['/login'], { queryParams: volverA.startsWith('/login') ? {} : { returnUrl: volverA } });
  }

  /// El backend respondio 403 a un pedido con token: puede ser que la sesion
  /// ya no sirva (token vencido o cuenta desactivada; el backend no lo
  /// distingue de un anonimo). Se confirma con /Usuario/me, que cualquier
  /// cuenta valida puede leer, y si tambien falla se cierra la sesion.
  verificarSesion(): void {
    if (this.verificando || !this.tokenSignal()) return;
    if (tokenVencido(this.tokenSignal()!)) {
      this.expirar();
      return;
    }
    this.verificando = true;
    this.http
      .get<Usuario>(`${API_BASE_URL}/Usuario/me`)
      .pipe(finalize(() => (this.verificando = false)))
      .subscribe({
        next: (u) => this.usuarioSignal.set(u),
        error: (err: HttpErrorResponse) => {
          if (err.status === 401 || err.status === 403) {
            this.expirar('Tu sesión ya no es válida. Iniciá sesión de nuevo.');
          }
        },
      });
  }

  /// Para despues de editar el perfil (SCRUM-28): refresca el signal "usuario"
  /// con lo que quedo guardado, sin tener que loguearse de nuevo.
  refrescarPerfil(): Observable<Usuario> {
    return this.cargarPerfil();
  }

  /// Reemplaza el JWT guardado sin pasar por /auth/login. Necesario cuando el
  /// propio usuario cambia su email (UsuarioController.actualizar devuelve el
  /// token nuevo en el header "X-New-Token"): el token viejo tiene como subject
  /// el email anterior, y sin reemplazarlo el usuario queda deslogueado en el
  /// siguiente request (ver UsuarioService.actualizar).
  actualizarToken(token: string): void {
    this.guardarSesion(token);
  }

  private cargarPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>(`${API_BASE_URL}/Usuario/me`).pipe(
      tap((u) => this.usuarioSignal.set(u)),
      catchError((err: HttpErrorResponse) => {
        // Solo si el backend rechaza la sesion: un error de red o un 500 no
        // tienen por que dejar al usuario afuera.
        if (err.status === 401 || err.status === 403) {
          this.expirar('Tu sesión ya no es válida. Iniciá sesión de nuevo.');
        }
        return throwError(() => err);
      }),
    );
  }

  private guardarSesion(token: string): void {
    try {
      localStorage.setItem(TOKEN_KEY, token);
    } catch {
      /* almacenamiento no disponible -- la sesion dura solo lo que dure la pestaña */
    }
    this.tokenSignal.set(token);
    this.programarVencimiento(token);
  }

  private programarVencimiento(token: string): void {
    if (this.timerVencimiento) clearTimeout(this.timerVencimiento);
    const exp = vencimientoMs(token);
    if (exp == null) return;
    // setTimeout no acepta mas de ~24,8 dias; el JWT dura 24 h.
    const espera = Math.min(Math.max(exp - Date.now(), 0), 2_147_000_000);
    this.timerVencimiento = setTimeout(() => this.expirar(), espera);
  }
}

function leerTokenGuardado(): string | null {
  try {
    return localStorage.getItem(TOKEN_KEY);
  } catch {
    return null;
  }
}

function leerClaims(token: string): ClaimsToken | null {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(atob(base64)) as ClaimsToken;
  } catch {
    return null;
  }
}

/// Momento (en ms) en que vence el JWT, segun su claim "exp".
function vencimientoMs(token: string): number | null {
  const exp = leerClaims(token)?.exp;
  return typeof exp === 'number' ? exp * 1000 : null;
}

function tokenVencido(token: string): boolean {
  const exp = vencimientoMs(token);
  return exp != null && exp <= Date.now();
}

function extraerRolDeToken(token: string): TipoUsuario | null {
  try {
    const payloadBase64 = token.split('.')[1];
    const payload: ClaimsToken = JSON.parse(atob(payloadBase64));
    const authority = payload.roles?.[0]?.authority;
    if (!authority?.startsWith('ROLE_')) return null;
    return authority.slice('ROLE_'.length) as TipoUsuario;
  } catch {
    return null;
  }
}
