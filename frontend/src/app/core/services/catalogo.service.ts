import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { TipoAlojamiento } from '../models/tipo-alojamiento.model';
import { Servicio } from '../models/servicio.model';

/// Espejo de ReemplazoCatalogoDTO -- fusiona idOrigen dentro de idDestino
/// (todo lo que referenciaba a idOrigen pasa a referenciar idDestino, y
/// idOrigen se borra).
export interface ReemplazoCatalogo {
  idOrigen: number;
  idDestino: number;
}

/// Lectura publica de los catalogos administrables (backend: ambos GET /listar
/// son permitAll). "crear"/"renombrar"/"reemplazar"/"eliminar" (SCRUM-43/44)
/// son exclusivos ADMIN. El catalogo tambien se completa solo cuando un
/// anfitrion publica un alojamiento con un tipo/servicio nuevo (ver
/// AlojamientoRegistroDTO) -- "crear" es para cargarlo de antemano.
@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private readonly http = inject(HttpClient);

  listarTipos(): Observable<TipoAlojamiento[]> {
    return this.http.get<TipoAlojamiento[]>(`${API_BASE_URL}/TipoAlojamiento/listar`);
  }

  crearTipo(nombre: string): Observable<TipoAlojamiento> {
    return this.http.post<TipoAlojamiento>(`${API_BASE_URL}/TipoAlojamiento/crear`, { nombre });
  }

  renombrarTipo(id: number, nombre: string): Observable<TipoAlojamiento> {
    return this.http.put<TipoAlojamiento>(`${API_BASE_URL}/TipoAlojamiento/${id}`, { nombre });
  }

  reemplazarTipo(dto: ReemplazoCatalogo): Observable<void> {
    return this.http.put<void>(`${API_BASE_URL}/TipoAlojamiento/reemplazar`, dto);
  }

  eliminarTipo(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/TipoAlojamiento/eliminar/${id}`);
  }

  listarServicios(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`${API_BASE_URL}/Servicio/listar`);
  }

  crearServicio(nombre: string): Observable<Servicio> {
    return this.http.post<Servicio>(`${API_BASE_URL}/Servicio/crear`, { nombre });
  }

  renombrarServicio(id: number, nombre: string): Observable<Servicio> {
    return this.http.put<Servicio>(`${API_BASE_URL}/Servicio/${id}`, { nombre });
  }

  reemplazarServicio(dto: ReemplazoCatalogo): Observable<void> {
    return this.http.put<void>(`${API_BASE_URL}/Servicio/reemplazar`, dto);
  }

  eliminarServicio(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/Servicio/eliminar/${id}`);
  }
}
