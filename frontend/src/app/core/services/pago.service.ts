import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Reserva } from '../models/reserva.model';

/// Espejo de AdminReembolsoDTO. montoReembolsoCliente es cuanto del monto base
/// se le devuelve al cliente -- el resto queda para el anfitrion (ver
/// PagoService.ejecutarCancelacion / ReservaService.cancelarComoAdminDTO en el backend).
export interface AdminReembolso {
  idReserva: number;
  montoReembolsoCliente: number;
}

/// SCRUM-46 / RF24. Reembolso manual: cancela la reserva con un reembolso a
/// medida, para casos que no entran en las reglas automaticas. Exclusivo ADMIN.
@Injectable({ providedIn: 'root' })
export class PagoService {
  private readonly http = inject(HttpClient);

  reembolsoAdmin(dto: AdminReembolso): Observable<Reserva> {
    return this.http.post<Reserva>(`${API_BASE_URL}/Pago/reembolso-admin`, dto);
  }
}
