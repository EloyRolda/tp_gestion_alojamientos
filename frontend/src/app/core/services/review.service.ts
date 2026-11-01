import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Review } from '../models/review.model';

/// Lectura publica (backend: GET /Review/alojamiento/{id} es permitAll). Dejar
/// una reseña (RF34) es de escritura, exclusiva de un cliente con reserva
/// finalizada -- llega en el Lote 2.
@Injectable({ providedIn: 'root' })
export class ReviewService {
  private readonly http = inject(HttpClient);

  listarPorAlojamiento(idAlojamiento: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${API_BASE_URL}/Review/alojamiento/${idAlojamiento}`);
  }
}
