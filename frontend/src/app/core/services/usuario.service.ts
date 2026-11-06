import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Usuario } from '../models/usuario.model';

/// Espejo de UsuarioModificarDTO. El id lo completa el backend desde la sesion
/// (ver UsuarioController.actualizar) -- nunca se manda desde el cliente.
export interface UsuarioModificar {
  nombre?: string;
  apellido?: string;
  usuarioLogin?: string;
  email?: string;
  password?: string;
  telefono?: string;
}

/// El propio usuario edita sus datos (PUT /Usuario/actualizar, requiere sesion).
@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly http = inject(HttpClient);

  actualizar(dto: UsuarioModificar): Observable<Usuario> {
    return this.http.put<Usuario>(`${API_BASE_URL}/Usuario/actualizar`, dto);
  }
}
