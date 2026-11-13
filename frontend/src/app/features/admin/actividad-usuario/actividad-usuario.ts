import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { forkJoin, of, switchMap, map } from 'rxjs';
import { Usuario } from '../../../core/models/usuario.model';
import { Reserva } from '../../../core/models/reserva.model';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { ResumenAlojamientoAnfitrion, ResumenCliente } from '../../../core/models/estadisticas.model';
import { ReservaService } from '../../../core/services/reserva.service';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { calcularTotal } from '../../../core/utils/reserva.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';

/// Todo lo que el admin necesita ver de un usuario, en su ficha (debajo de
/// "Editar usuario"), con links para seguir el recorrido:
///   - Anfitrion: sus numeros, los alojamientos a su nombre (publicados y
///     pausados) y las reservas de esos alojamientos, con cada cliente.
///   - Cliente: sus numeros y sus reservas, con cada alojamiento y anfitrion.
/// Las reservas activas (solicitadas o confirmadas) se muestran primero: son
/// las que importan, por ejemplo, antes de desactivar la cuenta.
@Component({
  selector: 'app-actividad-usuario',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink, Estado, Esqueleto],
  templateUrl: './actividad-usuario.html',
  styleUrl: './actividad-usuario.css',
})
export class ActividadUsuario {
  private readonly reservaService = inject(ReservaService);
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly estadisticas = inject(EstadisticasService);

  readonly usuario = input.required<Usuario>();

  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly resumenCliente = signal<ResumenCliente | null>(null);
  readonly alojamientos = signal<ResumenAlojamientoAnfitrion[]>([]);
  /// Alojamiento completo por id: si esta publicado y quien es su anfitrion.
  readonly detalle = signal<Map<number, Alojamiento>>(new Map());
  readonly reservas = signal<Reserva[]>([]);
  readonly verTodas = signal(false);

  readonly activas = computed(() =>
    this.reservas().filter((r) => r.estadoReserva === 'SOLICITADA' || r.estadoReserva === 'CONFIRMADA'),
  );
  readonly visibles = computed(() => (this.verTodas() ? this.reservas() : this.activas()));
  readonly publicados = computed(() => this.alojamientos().filter((a) => this.detalle().get(a.idAlojamiento)?.activo).length);
  readonly ingresos = computed(() => this.alojamientos().reduce((t, a) => t + Number(a.ingresosTotales), 0));
  readonly reservasAnfitrion = computed(() => this.alojamientos().reduce((t, a) => t + a.cantidadReservas, 0));

  constructor() {
    effect(() => this.cargar(this.usuario()));
  }

  publicado(idAlojamiento: number): boolean | null {
    const a = this.detalle().get(idAlojamiento);
    return a ? a.activo : null;
  }

  anfitrionDe(idAlojamiento: number) {
    return this.detalle().get(idAlojamiento)?.anfitrion ?? null;
  }

  totalDe(r: Reserva): number {
    return r.precioTotal ?? calcularTotal(r.fechaInicio, r.fechaFin, r.precioNochePactado);
  }

  private cargar(u: Usuario): void {
    this.cargando.set(true);
    this.error.set(null);
    const listo = {
      error: (err: HttpErrorResponse) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    };

    if (u.tipo === 'ANFITRION') {
      forkJoin({ resumen: this.estadisticas.resumenAnfitrion(u.id), todas: this.reservaService.listarTodas() })
        .pipe(
          switchMap(({ resumen, todas }) => {
            const ids = resumen.map((a) => a.idAlojamiento);
            const propios = new Set(ids);
            const reservas = todas.filter((r) => propios.has(r.alojamiento.idAlojamiento));
            return this.alojamientoService.porIds(ids).pipe(map((detalle) => ({ resumen, reservas, detalle })));
          }),
        )
        .subscribe({
          next: ({ resumen, reservas, detalle }) => {
            this.alojamientos.set([...resumen].sort((a, b) => a.titulo.localeCompare(b.titulo)));
            this.mostrarReservas(reservas, detalle);
          },
          ...listo,
        });
    } else if (u.tipo === 'CLIENTE') {
      forkJoin({ resumen: this.estadisticas.resumenCliente(u.id), todas: this.reservaService.listarTodas() })
        .pipe(
          switchMap(({ resumen, todas }) => {
            const reservas = todas.filter((r) => r.cliente.id === u.id);
            const ids = [...new Set(reservas.map((r) => r.alojamiento.idAlojamiento))];
            return (ids.length ? this.alojamientoService.porIds(ids) : of(new Map<number, Alojamiento>())).pipe(
              map((detalle) => ({ resumen, reservas, detalle })),
            );
          }),
        )
        .subscribe({
          next: ({ resumen, reservas, detalle }) => {
            this.resumenCliente.set(resumen);
            this.mostrarReservas(reservas, detalle);
          },
          ...listo,
        });
    } else {
      this.cargando.set(false);
    }
  }

  private mostrarReservas(reservas: Reserva[], detalle: Map<number, Alojamiento>): void {
    // Las mas proximas primero.
    this.reservas.set([...reservas].sort((a, b) => b.fechaInicio.localeCompare(a.fechaInicio)));
    this.detalle.set(detalle);
    // Si no hay activas, directamente se muestran todas.
    this.verTodas.set(this.activas().length === 0);
    this.cargando.set(false);
    // Si se llego con #reservas (por ejemplo, desde el aviso de la baja), el
    // router quiso bajar antes de que existiera la seccion: se baja ahora.
    if (location.hash === '#reservas') {
      setTimeout(() => document.getElementById('reservas')?.scrollIntoView({ block: 'start' }));
    }
  }
}
