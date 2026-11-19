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

/// Espejo de ResultadoFusionDTO: que se elimino, que se conservo y cuantos
/// alojamientos pasaron de uno a otro.
export interface ResultadoFusion {
  eliminado: string;
  conservado: string;
  alojamientosMovidos: number;
}

/// id de la entrada del catalogo -> cantidad de alojamientos que la usan
/// (las que no aparecen no tienen ninguno). Solo ADMIN.
export type UsoCatalogo = Record<number, number>;

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

  reemplazarTipo(dto: ReemplazoCatalogo): Observable<ResultadoFusion> {
    return this.http.put<ResultadoFusion>(`${API_BASE_URL}/TipoAlojamiento/reemplazar`, dto);
  }

  usoTipos(): Observable<UsoCatalogo> {
    return this.http.get<UsoCatalogo>(`${API_BASE_URL}/TipoAlojamiento/uso`);
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

  reemplazarServicio(dto: ReemplazoCatalogo): Observable<ResultadoFusion> {
    return this.http.put<ResultadoFusion>(`${API_BASE_URL}/Servicio/reemplazar`, dto);
  }

  usoServicios(): Observable<UsoCatalogo> {
    return this.http.get<UsoCatalogo>(`${API_BASE_URL}/Servicio/uso`);
  }

  eliminarServicio(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/Servicio/eliminar/${id}`);
  }
}
