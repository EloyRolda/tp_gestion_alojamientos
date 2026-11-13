import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { Reserva } from '../../../core/models/reserva.model';
import { calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';
import { coincide } from '../../../core/utils/busqueda.util';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';

/// SCRUM-30 / RF26. Listado de las reservas propias del cliente logueado.
@Component({
  selector: 'app-mis-reservas',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink, Estado, Esqueleto, Vacio, BuscadorLista],
  templateUrl: './mis-reservas.html',
  styleUrl: './mis-reservas.css',
})
export class MisReservas {
  private readonly reservaService = inject(ReservaService);

  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  /// Busca por alojamiento o numero de reserva (sin tildes ni mayusculas).
  readonly busqueda = signal('');
  readonly reservasFiltradas = computed(() =>
    this.reservas().filter((r) => coincide(this.busqueda().replace('#', ''), r.idReserva, r.alojamiento.titulo)),
  );

  constructor() {
    this.reservaService.listarPropios().subscribe({
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
