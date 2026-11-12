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

/// SCRUM-31 / RF21, RF22, RF29, RF31, RF40. Detalle completo de una reserva
/// propia: estado, desglose del pago (embebido en ReservaResponseDTO.pago, no
/// hace falta pegarle a /Pago/reserva/{id} aparte), cancelar, comprobante
/// imprimible (boton -> window.print(), con @media print en el CSS que oculta
/// todo lo que no es el comprobante). SCRUM-33 (dejar reseña) se resuelve aca
/// mismo, inline, en vez de una pantalla separada: opera sobre esta misma
/// reserva y solo tiene sentido en este contexto (FINALIZADA, sin reseña previa).
@Component({
  selector: 'app-detalle-reserva',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado, Esqueleto],
  templateUrl: './detalle-reserva.html',
  styleUrl: './detalle-reserva.css',
})
export class DetalleReserva {
  readonly id = input.required<string>();

  private readonly reservaService = inject(ReservaService);
  private readonly aviso = inject(AvisoService);
  private readonly reviewService = inject(ReviewService);

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

  puedeResenar(r: Reserva): boolean {
    return r.estadoReserva === 'FINALIZADA' && !r.tieneReview;
  }

  cancelar(): void {
    const r = this.reserva();
    if (!r) return;
    this.errorCancelar.set(null);
    this.cancelando.set(true);
    this.reservaService.cancelar(r.idReserva).subscribe({
      next: () => {
        this.cancelando.set(false);
        this.aviso.mostrar('Reserva cancelada');
        this.cargar(r.idReserva);
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
        this.aviso.mostrar('¡Gracias por tu reseña!');
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
