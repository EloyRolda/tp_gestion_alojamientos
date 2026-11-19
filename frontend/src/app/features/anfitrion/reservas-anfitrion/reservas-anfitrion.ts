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
import { crearPaginado } from '../../../core/utils/paginado.util';
import { Paginador } from '../../../shared/paginador/paginador';

type FiltroEstado = EstadoReserva | 'TODAS';

/// SCRUM-38 / RF27. Reservas de TODOS los alojamientos del anfitrion logueado,
/// con filtro por estado (en el cliente: ya viene todo en una sola llamada,
/// filtrar de vuelta contra el backend no suma nada -- mismo criterio que
/// la búsqueda de alojamientos).
@Component({
  selector: 'app-reservas-anfitrion',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado, Esqueleto, Vacio, BuscadorLista, Paginador],
  templateUrl: './reservas-anfitrion.html',
  styleUrl: './reservas-anfitrion.css',
})
export class ReservasAnfitrion {
  private readonly reservaService = inject(ReservaService);

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
        coincide(b, r.idReserva, r.alojamiento.titulo, r.cliente.nombre, r.cliente.apellido),
    );
  });
  /// Paginado de la lista (10/20/30/50 por pagina, SCRUM-74).
  readonly paginado = crearPaginado(this.reservasFiltradas, () => [this.busqueda(), this.filtro()]);

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
