import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { ResumenCliente } from '../models/estadisticas.model';

/// Solo el endpoint "cliente" por ahora (Lote 2). Los de "anfitrion"/"admin"
/// llegan con sus lotes correspondientes.
@Injectable({ providedIn: 'root' })
export class EstadisticasService {
  private readonly http = inject(HttpClient);

  resumenCliente(): Observable<ResumenCliente> {
    return this.http.get<ResumenCliente>(`${API_BASE_URL}/Estadisticas/cliente/resumen`);
  }
}
