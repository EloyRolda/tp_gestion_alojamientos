/// Llamadas al backend de reportes (RF44): alta de un reporte, el catalogo fijo
/// de motivos y, para el administrador, el listado y la resolucion de cada caso
/// (SCRUM-79). Sin pedidos HTTP en el constructor: el catalogo se pide la primera
/// vez que se abre la carta y se cachea.
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { API_BASE_URL } from '../config/api.config';
import {
  AccionModeracion,
  MotivoReporte,
  Reporte,
  ReporteAdmin,
  ReporteRegistro,
} from '../models/reporte.model';

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

  /// Listado de moderacion (solo ADMIN). Sin estado, o con 'TODOS', trae todo;
  /// con PENDIENTE o RESUELTO filtra por ese estado.
  listarAdmin(estado?: string): Observable<ReporteAdmin[]> {
    const filtro = estado && estado !== 'TODOS' ? { estado } : undefined;
    return this.http.get<ReporteAdmin[]>(this.url, { params: filtro });
  }

  /// Resuelve un reporte pendiente con la accion elegida por el admin.
  resolver(id: number, accion: AccionModeracion): Observable<ReporteAdmin> {
    return this.http.post<ReporteAdmin>(`${this.url}/${id}/resolver`, { accion });
  }
}
