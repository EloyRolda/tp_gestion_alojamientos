import { Component, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { PagoService } from '../../../core/services/pago.service';
import { Reserva } from '../../../core/models/reserva.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';

/// SCRUM-45/46 / RF24. Detalle de cualquier reserva de la plataforma, visto
/// como admin: cancelar (regla automatica segun quien cancela) o reembolso
/// manual a medida (POST /Pago/reembolso-admin, que cancela la reserva con
/// el split que indique el admin -- no entra en las 3 reglas automaticas).
@Component({
  selector: 'app-detalle-reserva-admin',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink, Estado, Esqueleto],
  templateUrl: './detalle-reserva-admin.html',
  styleUrl: './detalle-reserva-admin.css',
})
export class DetalleReservaAdmin {
  readonly id = input.required<string>();

  private readonly reservaService = inject(ReservaService);
  private readonly aviso = inject(AvisoService);
  private readonly pagoService = inject(PagoService);

  readonly reserva = signal<Reserva | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly procesando = signal(false);

  readonly montoReembolso = signal<number | null>(null);
  readonly enviandoReembolso = signal(false);
  readonly errorReembolso = signal<string | null>(null);

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

  /// El reembolso manual solo tiene sentido si ya existe un Pago para
  /// devolver (CONFIRMADA) -- una SOLICITADA nunca tuvo Pago, asi que
  /// cancelarla con reembolsoAdmin solo cancelaria sin mover plata.
  puedeReembolsar(r: Reserva): boolean {
    return r.estadoReserva === 'CONFIRMADA';
  }

  cancelar(): void {
    const r = this.reserva();
    if (!r) return;
    this.error.set(null);
    this.procesando.set(true);
    this.reservaService.cancelar(r.idReserva).subscribe({
      next: () => {
        this.procesando.set(false);
        this.aviso.mostrar('Reserva cancelada');
        this.cargar(r.idReserva);
      },
      error: (err) => {
        this.procesando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  enviarReembolso(): void {
    const r = this.reserva();
    const monto = this.montoReembolso();
    if (!r || monto === null || monto < 0) return;

    this.errorReembolso.set(null);
    this.enviandoReembolso.set(true);
    this.pagoService.reembolsoAdmin({ idReserva: r.idReserva, montoReembolsoCliente: monto }).subscribe({
      next: () => {
        this.enviandoReembolso.set(false);
        this.montoReembolso.set(null);
        this.aviso.mostrar('Reembolso ejecutado');
        this.cargar(r.idReserva);
      },
      error: (err) => {
        this.enviandoReembolso.set(false);
        this.errorReembolso.set(extraerMensajeError(err));
      },
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
