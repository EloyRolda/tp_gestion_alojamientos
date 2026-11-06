import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Review } from '../models/review.model';

/// Espejo de ReviewRegistroDTO.
export interface ReviewRegistro {
  idReserva: number;
  puntuacion: number;
  comentario: string;
}

/// Lectura publica (backend: GET /Review/alojamiento/{id} es permitAll).
/// "registrar" (SCRUM-33 / RF34) exige sesion: solo el cliente dueño de una
/// reserva ya FINALIZADA (lo valida ReviewService.crear en el backend).
@Injectable({ providedIn: 'root' })
export class ReviewService {
  private readonly http = inject(HttpClient);

  listarPorAlojamiento(idAlojamiento: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${API_BASE_URL}/Review/alojamiento/${idAlojamiento}`);
  }

  registrar(dto: ReviewRegistro): Observable<Review> {
    return this.http.post<Review>(`${API_BASE_URL}/Review/registrar`, dto);
  }
}
