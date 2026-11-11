import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Usuario, UsuarioRegistro } from '../models/usuario.model';
import { AuthService } from './auth.service';

/// Espejo de AliasCobroInfoDTO. Nunca trae titular (no se puede obtener por API).
export interface AliasCobroInfo {
  valido: boolean;
  tipo: string;
  banco: string | null;
  mensaje: string;
}

/// Espejo de UsuarioModificarDTO. El id lo completa el backend desde la sesion
/// en el PUT propio (ver UsuarioController.actualizar) -- nunca se manda desde
/// el cliente ahi. En el PUT de admin si se manda explicito (otro usuario).
export interface UsuarioModificar {
  id?: number;
  nombre?: string;
  apellido?: string;
  usuarioLogin?: string;
  email?: string;
  password?: string;
  telefono?: string;
  activo?: boolean;
}

/// El propio usuario edita sus datos (PUT /Usuario/actualizar) o un ADMIN
/// administra cualquier usuario (SCRUM-42 / RF06, RF07) -- listar, activar/
/// desactivar, editar datos, crear otros administradores. Las rutas admin
/// exigen rol ADMIN (SecurityConfig), no hace falta duplicar ese chequeo aca.
@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);

  /// Si el PUT cambio el email propio, el backend devuelve el JWT nuevo en el
  /// header "X-New-Token" (el viejo token tiene el email anterior como subject
  /// y deja de servir) -- hay que leerlo con observe:'response' y reemplazarlo
  /// antes de que el siguiente request autenticado falle.
  actualizar(dto: UsuarioModificar): Observable<Usuario> {
    return this.http.put<Usuario>(`${API_BASE_URL}/Usuario/actualizar`, dto, { observe: 'response' }).pipe(
      map((respuesta) => {
        const nuevoToken = respuesta.headers.get('X-New-Token');
        if (nuevoToken) {
          this.auth.actualizarToken(nuevoToken);
        }
        return respuesta.body as Usuario;
      }),
    );
  }

  listarTodos(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${API_BASE_URL}/Usuario/listar`);
  }

  mostrar(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${API_BASE_URL}/Usuario/mostrar/${id}`);
  }

  actualizarComoAdmin(dto: UsuarioModificar): Observable<Usuario> {
    return this.http.put<Usuario>(`${API_BASE_URL}/Usuario/actualizar/admin`, dto);
  }

  /// Desactiva (no borra) -- ver UsuarioService.borrarPorId en el backend: solo pisa "activo" a false.
  desactivar(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/Usuario/eliminar/${id}`);
  }

  registrarAdministrador(dto: UsuarioRegistro): Observable<string> {
    return this.http.post(`${API_BASE_URL}/Usuario/registrar/administrador`, dto, { responseType: 'text' });
  }

  /// Paso 1 del alias de cobro: valida formato/banco, no guarda nada todavia.
  validarAliasCobro(valor: string): Observable<AliasCobroInfo> {
    return this.http.post<AliasCobroInfo>(`${API_BASE_URL}/Usuario/alias-cobro/validar`, { valor });
  }

  /// Paso 2: confirma y recien ahi persiste el alias en el usuario logueado.
  confirmarAliasCobro(valor: string): Observable<AliasCobroInfo> {
    return this.http.post<AliasCobroInfo>(`${API_BASE_URL}/Usuario/alias-cobro/confirmar`, { valor });
  }
}
