import { Component, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { CobroPendiente } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';
import { crearPaginado } from '../../../core/utils/paginado.util';
import { Paginador } from '../../../shared/paginador/paginador';

/// SCRUM-40 / RF33. Reservas ya pagadas por el cliente que todavia esperan
/// liquidarse -- se liquidan (Payout real) recien cuando el anfitrion marca
/// la reserva como FINALIZADA (ver SCRUM-39).
@Component({
  selector: 'app-cobros-pendientes',
  standalone: true,
  imports: [DatePipe, DecimalPipe, Esqueleto, Vacio, Paginador],
  templateUrl: './cobros-pendientes.html',
  styleUrl: './cobros-pendientes.css',
})
export class CobrosPendientes {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly cobros = signal<CobroPendiente[]>([]);
  /// Paginado de la lista (10/20/30/50 por pagina, SCRUM-74).
  readonly paginado = crearPaginado(this.cobros);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly totalPendiente = () => this.cobros().reduce((acc, c) => acc + c.montoAcobrar, 0);

  constructor() {
    this.estadisticasService.cobrosPendientes().subscribe({
      next: (c) => {
        this.cobros.set(c);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
