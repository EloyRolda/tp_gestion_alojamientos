import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { HttpParams } from '@angular/common/http';
import {
  AlojamientoRanking,
  CancelacionPorMotivo,
  CobroPendiente,
  FlujoDinero,
  IngresoMensual,
  ResumenAlojamientoAnfitrion,
  ResumenCliente,
} from '../models/estadisticas.model';

/// "cliente" (Lote 2), "anfitrion" (Lote 3) y "admin" (Lote 4).
@Injectable({ providedIn: 'root' })
export class EstadisticasService {
  private readonly http = inject(HttpClient);

  /// Sin idCliente: el del usuario logueado. Con idCliente: el de ese cliente
  /// (solo lo acepta el backend si quien pregunta es ADMIN).
  resumenCliente(idCliente?: number): Observable<ResumenCliente> {
    const params = idCliente != null ? new HttpParams().set('idCliente', idCliente) : undefined;
    return this.http.get<ResumenCliente>(`${API_BASE_URL}/Estadisticas/cliente/resumen`, { params });
  }

  /// Igual que resumenCliente: idAnfitrion solo lo usa un ADMIN.
  resumenAnfitrion(idAnfitrion?: number): Observable<ResumenAlojamientoAnfitrion[]> {
    const params = idAnfitrion != null ? new HttpParams().set('idAnfitrion', idAnfitrion) : undefined;
    return this.http.get<ResumenAlojamientoAnfitrion[]>(`${API_BASE_URL}/Estadisticas/anfitrion/resumen`, { params });
  }

  cobrosPendientes(): Observable<CobroPendiente[]> {
    return this.http.get<CobroPendiente[]>(`${API_BASE_URL}/Estadisticas/anfitrion/cobros-pendientes`);
  }

  /// desde/hasta en formato ISO (yyyy-MM-dd); null/undefined = sin limite en ese extremo.
  flujoDeDinero(desde?: string, hasta?: string): Observable<FlujoDinero> {
    let params = new HttpParams();
    if (desde) params = params.set('desde', desde);
    if (hasta) params = params.set('hasta', hasta);
    return this.http.get<FlujoDinero>(`${API_BASE_URL}/Estadisticas/admin/flujo-dinero`, { params });
  }

  cancelacionesPorMotivo(desde?: string, hasta?: string): Observable<CancelacionPorMotivo[]> {
    let params = new HttpParams();
    if (desde) params = params.set('desde', desde);
    if (hasta) params = params.set('hasta', hasta);
    return this.http.get<CancelacionPorMotivo[]>(`${API_BASE_URL}/Estadisticas/admin/cancelaciones-por-motivo`, { params });
  }

  ingresosMensuales(anio: number): Observable<IngresoMensual[]> {
    return this.http.get<IngresoMensual[]>(`${API_BASE_URL}/Estadisticas/admin/ingresos-mensuales`, { params: { anio } });
  }

  topAlojamientos(criterio: 'ingresos' | 'reservas' | 'rating', limite = 10): Observable<AlojamientoRanking[]> {
    return this.http.get<AlojamientoRanking[]>(`${API_BASE_URL}/Estadisticas/admin/top-alojamientos`, { params: { criterio, limite } });
  }
}
