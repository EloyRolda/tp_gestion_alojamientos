import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { EstadoReserva, Reserva } from '../../../core/models/reserva.model';
import { calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';

type FiltroEstado = EstadoReserva | 'TODAS';

/// SCRUM-45 / RF24. Todas las reservas de la plataforma (cualquier cliente,
/// cualquier anfitrion), con filtro por estado -- mismo criterio que
/// ReservasAnfitrion (Lote 3), pero sin acotar a "las mias".
@Component({
  selector: 'app-todas-reservas',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado, Esqueleto, Vacio],
  templateUrl: './todas-reservas.html',
  styleUrl: './todas-reservas.css',
})
export class TodasReservas {
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
    this.reservaService.listarTodas().subscribe({
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
