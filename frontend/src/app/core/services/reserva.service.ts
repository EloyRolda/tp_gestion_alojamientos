import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Reserva, ReservaModificar, ReservaRegistro } from '../models/reserva.model';

/// Todos los endpoints exigen sesion (ver SecurityConfig: /Reserva/listar/propios,
/// /registrar, /actualizar, /mostrar/**, /cancelar/** -- ninguno es permitAll).
@Injectable({ providedIn: 'root' })
export class ReservaService {
  private readonly http = inject(HttpClient);

  listarPropios(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${API_BASE_URL}/Reserva/listar/propios`);
  }

  obtener(id: number): Observable<Reserva> {
    return this.http.get<Reserva>(`${API_BASE_URL}/Reserva/mostrar/${id}`);
  }

  registrar(dto: ReservaRegistro): Observable<Reserva> {
    return this.http.post<Reserva>(`${API_BASE_URL}/Reserva/registrar`, dto);
  }

  actualizar(dto: ReservaModificar): Observable<Reserva> {
    return this.http.put<Reserva>(`${API_BASE_URL}/Reserva/actualizar`, dto);
  }

  cancelar(id: number): Observable<Reserva> {
    return this.http.patch<Reserva>(`${API_BASE_URL}/Reserva/cancelar/${id}`, {});
  }
}
