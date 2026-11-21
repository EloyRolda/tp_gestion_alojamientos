/// Llamadas al backend de reportes (RF44): alta de un reporte y el catalogo fijo
/// de motivos. Sin pedidos HTTP en el constructor: el catalogo se pide la
/// primera vez que se abre la carta y se cachea.
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { API_BASE_URL } from '../config/api.config';
import { MotivoReporte, Reporte, ReporteRegistro } from '../models/reporte.model';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_BASE_URL}/Reporte`;

  /// El catalogo de motivos no cambia, asi que se guarda la ultima respuesta.
  private motivosCache: MotivoReporte[] | null = null;

  crear(dto: ReporteRegistro): Observable<Reporte> {
    return this.http.post<Reporte>(this.url, dto);
  }

  motivos(): Observable<MotivoReporte[]> {
    if (this.motivosCache) {
      return of(this.motivosCache);
    }
    return this.http
      .get<MotivoReporte[]>(`${this.url}/motivos`)
      .pipe(tap((lista) => (this.motivosCache = lista)));
  }
}
