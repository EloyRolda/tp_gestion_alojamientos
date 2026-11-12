import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { EstadoReserva, Reserva } from '../../../core/models/reserva.model';
import { calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';

type FiltroEstado = EstadoReserva | 'TODAS';

/// SCRUM-38 / RF27. Reservas de TODOS los alojamientos del anfitrion logueado,
/// con filtro por estado (en el cliente: ya viene todo en una sola llamada,
/// filtrar de vuelta contra el backend no suma nada -- mismo criterio que
/// BusquedaComponent del Lote 1).
@Component({
  selector: 'app-reservas-anfitrion',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado],
  templateUrl: './reservas-anfitrion.html',
  styleUrl: './reservas-anfitrion.css',
})
export class ReservasAnfitrion {
  private readonly reservaService = inject(ReservaService);

  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly filtro = signal<FiltroEstado>('TODAS');

  readonly reservasFiltradas = computed(() => {
    const f = this.filtro();
    return f === 'TODAS' ? this.reservas() : this.reservas().filter((r) => r.estadoReserva === f);
  });

  readonly estados: FiltroEstado[] = ['TODAS', 'SOLICITADA', 'CONFIRMADA', 'FINALIZADA', 'CANCELADA'];

  constructor() {
    this.reservaService.listarPorAnfitrion().subscribe({
      next: (r) => {
        this.reservas.set(r);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  totalDe(r: Reserva): number {
    return r.precioTotal ?? calcularTotal(r.fechaInicio, r.fechaFin, r.precioNochePactado);
  }
}
