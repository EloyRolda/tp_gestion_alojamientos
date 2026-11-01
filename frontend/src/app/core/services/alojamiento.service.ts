import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Alojamiento } from '../models/alojamiento.model';
import { ServicioxAlojamiento } from '../models/servicio.model';

/// Lectura publica (backend: GET /Alojamiento/listar y /mostrar/{id} son permitAll).
/// No hay query params de filtro en el backend -- la busqueda (RF39) se filtra
/// del lado del cliente sobre esta misma lista, ver BusquedaComponent.
@Injectable({ providedIn: 'root' })
export class AlojamientoService {
  private readonly http = inject(HttpClient);

  listar(): Observable<Alojamiento[]> {
    return this.http.get<Alojamiento[]>(`${API_BASE_URL}/Alojamiento/listar`);
  }

  obtener(id: number): Observable<Alojamiento> {
    return this.http.get<Alojamiento>(`${API_BASE_URL}/Alojamiento/mostrar/${id}`);
  }

  listarServicios(idAlojamiento: number): Observable<ServicioxAlojamiento[]> {
    return this.http.get<ServicioxAlojamiento[]>(`${API_BASE_URL}/Alojamiento/${idAlojamiento}/servicios`);
  }
}
