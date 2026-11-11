import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, switchMap, tap, throwError } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { TipoUsuario, Usuario, UsuarioRegistro } from '../models/usuario.model';

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
    if (this.tokenSignal()) {
      queueMicrotask(() => this.cargarPerfil().subscribe({ error: () => {} }));
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
      catchError((err) => {
        this.cerrarSesion();
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
  }
}

function leerTokenGuardado(): string | null {
  try {
    return localStorage.getItem(TOKEN_KEY);
  } catch {
    return null;
  }
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
