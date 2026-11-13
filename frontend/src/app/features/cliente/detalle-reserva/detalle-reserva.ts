import { Component, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { ReviewService, ReviewRegistro } from '../../../core/services/review.service';
import { Reserva } from '../../../core/models/reserva.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';
import { explicarCancelacion, explicarResultado, notaPrevia } from '../../../core/utils/cancelacion.util';
import { PAGOS_SIMULADOS } from '../../../core/config/api.config';
import { hoyIso } from '../../../core/utils/fecha.util';
import { CalendarioRango, RangoOcupado } from '../../../shared/calendario-rango/calendario-rango';

/// SCRUM-31 / RF21, RF22, RF29, RF31, RF40. Detalle completo de una reserva
/// propia: estado, desglose del pago (embebido en ReservaResponseDTO.pago, no
/// hace falta pegarle a /Pago/reserva/{id} aparte), cancelar, comprobante
/// imprimible (boton -> window.print(), con @media print en el CSS que oculta
/// todo lo que no es el comprobante). SCRUM-33 (dejar reseña) se resuelve aca
/// mismo, inline, en vez de una pantalla separada: opera sobre esta misma
/// (Ademas: mientras la reserva esta SOLICITADA se pueden cambiar las fechas,
/// con el mismo calendario del formulario de reserva.)
/// reserva y solo tiene sentido en este contexto (FINALIZADA, sin reseña previa).
@Component({
  selector: 'app-detalle-reserva',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado, Esqueleto, CalendarioRango],
  templateUrl: './detalle-reserva.html',
  styleUrl: './detalle-reserva.css',
})
export class DetalleReserva {
  readonly id = input.required<string>();

  private readonly reservaService = inject(ReservaService);
  private readonly aviso = inject(AvisoService);
  private readonly reviewService = inject(ReviewService);
  private readonly confirmacion = inject(ConfirmacionService);

  readonly reserva = signal<Reserva | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly cancelando = signal(false);
  readonly errorCancelar = signal<string | null>(null);

  readonly puntuacion = signal(5);
  readonly comentario = signal('');
  readonly enviandoReview = signal(false);
  readonly errorReview = signal<string | null>(null);
  readonly reviewEnviada = signal(false);

  /// Cambiar fechas (solo SOLICITADA).
  readonly hoy = hoyIso();
  readonly editandoFechas = signal(false);
  readonly nuevaInicio = signal('');
  readonly nuevaFin = signal('');
  readonly ocupados = signal<RangoOcupado[]>([]);
  readonly guardandoFechas = signal(false);
  readonly errorFechas = signal<string | null>(null);

  constructor() {
    effect(() => {
      const idNumerico = Number(this.id());
      if (!Number.isFinite(idNumerico)) return;
      this.cargar(idNumerico);
    });
  }

  noches(r: Reserva): number {
    return calcularNoches(r.fechaInicio, r.fechaFin);
  }

  total(r: Reserva): number {
    return r.precioTotal ?? calcularTotal(r.fechaInicio, r.fechaFin, r.precioNochePactado);
  }

  puedeCancelar(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA' || r.estadoReserva === 'CONFIRMADA';
  }

  puedeCambiarFechas(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA';
  }

  /// El plazo de 48 h para pagar ya paso, pero el proceso que la cancela
  /// todavia no corrio: no tiene sentido mandar a pagar.
  pagoVencido(r: Reserva): boolean {
    return r.pago?.estado === 'PENDIENTE' && !!r.pago.fechaLimitePago && new Date(r.pago.fechaLimitePago) < new Date();
  }

  abrirCambioFechas(r: Reserva): void {
    this.nuevaInicio.set(r.fechaInicio);
    this.nuevaFin.set(r.fechaFin);
    this.errorFechas.set(null);
    this.editandoFechas.set(true);
    // Las otras reservas propias en este alojamiento quedan en gris.
    this.reservaService.listarPropios().subscribe({
      next: (lista) =>
        this.ocupados.set(
          lista
            .filter(
              (x) =>
                x.idReserva !== r.idReserva &&
                x.alojamiento.idAlojamiento === r.alojamiento.idAlojamiento &&
                x.estadoReserva !== 'CANCELADA',
            )
            .map((x) => ({ inicio: x.fechaInicio, fin: x.fechaFin })),
        ),
    });
  }

  guardarFechas(): void {
    const r = this.reserva();
    if (!r || !this.nuevaInicio() || !this.nuevaFin()) return;
    this.errorFechas.set(null);
    this.guardandoFechas.set(true);
    this.reservaService
      .actualizar({ idReserva: r.idReserva, fechaInicio: this.nuevaInicio(), fechaFin: this.nuevaFin() })
      .subscribe({
        next: () => {
          this.guardandoFechas.set(false);
          this.editandoFechas.set(false);
          this.aviso.mostrar('Fechas actualizadas', ['El anfitrión ya ve las fechas nuevas de tu solicitud.']);
          this.cargar(r.idReserva);
        },
        error: (err) => {
          this.guardandoFechas.set(false);
          this.errorFechas.set(extraerMensajeError(err));
        },
      });
  }

  puedeResenar(r: Reserva): boolean {
    return r.estadoReserva === 'FINALIZADA' && !r.tieneReview;
  }

  async cancelar(): Promise<void> {
    const r = this.reserva();
    if (!r) return;
    const ok = await this.confirmacion.pedir({
      titulo: '¿Cancelar la reserva?',
      mensaje: explicarCancelacion(r, 'cliente'),
      nota: notaPrevia(r, PAGOS_SIMULADOS),
      aceptar: 'Sí, cancelar',
      cancelar: 'No, mantenerla',
      peligro: true,
    });
    if (!ok) return;
    this.errorCancelar.set(null);
    this.cancelando.set(true);
    this.reservaService.cancelar(r.idReserva).subscribe({
      next: () => {
        this.cancelando.set(false);
        this.mostrarResultado(r.idReserva);
      },
      error: (err) => {
        this.cancelando.set(false);
        this.errorCancelar.set(extraerMensajeError(err));
      },
    });
  }

  enviarReview(): void {
    const r = this.reserva();
    if (!r) return;
    this.errorReview.set(null);
    this.enviandoReview.set(true);
    const dto: ReviewRegistro = {
      idReserva: r.idReserva,
      puntuacion: this.puntuacion(),
      comentario: this.comentario(),
    };
    this.reviewService.registrar(dto).subscribe({
      next: () => {
        this.enviandoReview.set(false);
        this.reviewEnviada.set(true);
        this.aviso.mostrar('¡Gracias por tu reseña!', ['Ya se ve en la página del alojamiento.']);
        this.cargar(r.idReserva);
      },
      error: (err) => {
        this.enviandoReview.set(false);
        this.errorReview.set(extraerMensajeError(err));
      },
    });
  }

  imprimir(): void {
    window.print();
  }

  /// Recarga la reserva y muestra el cartel con lo que paso con la plata.
  private mostrarResultado(idReserva: number): void {
    this.reservaService.obtener(idReserva).subscribe({
      next: (actualizada) => {
        this.reserva.set(actualizada);
        void this.confirmacion.informar({ ...explicarResultado(actualizada, 'cliente', PAGOS_SIMULADOS), aceptar: 'Continuar' });
      },
      error: () => this.cargar(idReserva),
    });
  }

  private cargar(idReserva: number): void {
    this.cargando.set(true);
    this.error.set(null);
    this.reservaService.obtener(idReserva).subscribe({
      next: (r) => {
        this.reserva.set(r);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
