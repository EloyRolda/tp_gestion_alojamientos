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
import { coincide } from '../../../core/utils/busqueda.util';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';

type FiltroEstado = EstadoReserva | 'TODAS';

/// SCRUM-45 / RF24. Todas las reservas de la plataforma (cualquier cliente,
/// cualquier anfitrion), con filtro por estado -- mismo criterio que
/// ReservasAnfitrion (Lote 3), pero sin acotar a "las mias".
///
/// Alojamiento, cliente y anfitrion son links (al alojamiento y a la ficha de
/// cada usuario). La reserva no trae al anfitrion: se resuelve aparte, una
/// sola vez, con AlojamientoService.porIds.
@Component({
  selector: 'app-todas-reservas',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado, Esqueleto, Vacio, BuscadorLista],
  templateUrl: './todas-reservas.html',
  styleUrl: './todas-reservas.css',
})
export class TodasReservas {
  private readonly reservaService = inject(ReservaService);
  private readonly alojamientoService = inject(AlojamientoService);
  /// Alojamiento completo por id (para saber su anfitrion).
  private readonly alojamientos = signal<Map<number, Alojamiento>>(new Map());

  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly filtro = signal<FiltroEstado>('TODAS');
  /// Busca por numero de reserva, alojamiento o cliente (sin tildes ni mayusculas).
  readonly busqueda = signal('');

  readonly reservasFiltradas = computed(() => {
    const f = this.filtro();
    const b = this.busqueda().replace('#', '');
    return this.reservas().filter(
      (r) =>
        (f === 'TODAS' || r.estadoReserva === f) &&
        coincide(
          b,
          r.idReserva,
          r.alojamiento.titulo,
          r.cliente.nombre,
          r.cliente.apellido,
          this.anfitrionDe(r)?.nombre,
          this.anfitrionDe(r)?.apellido,
        ),
    );
  });

  readonly estados: FiltroEstado[] = ['TODAS', 'SOLICITADA', 'CONFIRMADA', 'FINALIZADA', 'CANCELADA'];

  constructor() {
    this.reservaService.listarTodas().subscribe({
      next: (r) => {
        this.reservas.set(r);
        this.cargando.set(false);
        this.alojamientoService
          .porIds(r.map((x) => x.alojamiento.idAlojamiento))
          .subscribe({ next: (m) => this.alojamientos.set(m) });
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  anfitrionDe(r: Reserva) {
    return this.alojamientos().get(r.alojamiento.idAlojamiento)?.anfitrion ?? null;
  }

  totalDe(r: Reserva): number {
    return r.precioTotal ?? calcularTotal(r.fechaInicio, r.fechaFin, r.precioNochePactado);
  }
}
