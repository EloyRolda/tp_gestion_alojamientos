import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { TipoAlojamiento } from '../models/tipo-alojamiento.model';
import { Servicio } from '../models/servicio.model';

/// Lectura publica de los catalogos administrables (backend: ambos GET /listar
/// son permitAll). La administracion (alta/baja/modificacion) es exclusiva del
/// rol ADMIN -- llega en el Lote 4.
@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private readonly http = inject(HttpClient);

  listarTipos(): Observable<TipoAlojamiento[]> {
    return this.http.get<TipoAlojamiento[]>(`${API_BASE_URL}/TipoAlojamiento/listar`);
  }

  listarServicios(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`${API_BASE_URL}/Servicio/listar`);
  }
}
