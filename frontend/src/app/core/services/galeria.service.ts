import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Galeria, Imagen } from '../models/galeria.model';

/// Lectura publica (backend: ambos GET son permitAll). La gestion (subir/borrar
/// imagenes) es de escritura, exclusiva del anfitrion dueño -- llega en el Lote 3.
@Injectable({ providedIn: 'root' })
export class GaleriaService {
  private readonly http = inject(HttpClient);

  obtenerPorAlojamiento(idAlojamiento: number): Observable<Galeria> {
    return this.http.get<Galeria>(`${API_BASE_URL}/Galeria/alojamiento/${idAlojamiento}`);
  }

  listarImagenes(idGaleria: number): Observable<Imagen[]> {
    return this.http.get<Imagen[]>(`${API_BASE_URL}/Galeria/${idGaleria}/imagenes`);
  }
}
