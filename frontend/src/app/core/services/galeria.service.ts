import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Galeria, Imagen } from '../models/galeria.model';

/// Lectura publica (backend: ambos GET son permitAll). "subir"/"eliminarImagen"
/// (SCRUM-37) exigen sesion de ANFITRION dueño (o ADMIN) -- las valida el propio
/// backend contra la galeria/alojamiento, no hace falta duplicar ese chequeo aca.
@Injectable({ providedIn: 'root' })
export class GaleriaService {
  private readonly http = inject(HttpClient);

  obtenerPorAlojamiento(idAlojamiento: number): Observable<Galeria> {
    return this.http.get<Galeria>(`${API_BASE_URL}/Galeria/alojamiento/${idAlojamiento}`);
  }

  listarImagenes(idGaleria: number): Observable<Imagen[]> {
    return this.http.get<Imagen[]>(`${API_BASE_URL}/Galeria/${idGaleria}/imagenes`);
  }

  /// multipart/form-data: no se fija Content-Type a mano, el browser arma el
  /// boundary solo si se deja que HttpClient lo infiera del FormData.
  subir(idGaleria: number, file: File, alt: string): Observable<Imagen> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('alt', alt);
    formData.append('galleryId', String(idGaleria));
    return this.http.post<Imagen>(`${API_BASE_URL}/Galeria/upload`, formData);
  }

  eliminarImagen(idImagen: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/Galeria/imagen/${idImagen}`);
  }
}
