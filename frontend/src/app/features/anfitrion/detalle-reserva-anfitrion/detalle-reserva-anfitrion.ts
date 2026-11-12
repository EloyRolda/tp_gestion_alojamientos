import { Component, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { Reserva } from '../../../core/models/reserva.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';

/// SCRUM-39 / RF20, RF23, RF25. Detalle de una reserva propia desde el lado
/// del anfitrion: confirmar (SOLICITADA -> CONFIRMADA, dispara la preferencia
/// de pago), cancelar, finalizar (dispara el Payout -- la API de Payouts de
/// MercadoPago rechaza las credenciales TEST de esta app con
/// "Invalid signature", limitacion ajena a este codigo).
@Component({
  selector: 'app-detalle-reserva-anfitrion',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink, Estado],
  templateUrl: './detalle-reserva-anfitrion.html',
  styleUrl: './detalle-reserva-anfitrion.css',
})
export class DetalleReservaAnfitrion {
  readonly id = input.required<string>();

  private readonly reservaService = inject(ReservaService);

  readonly reserva = signal<Reserva | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly procesando = signal(false);

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

  puedeConfirmar(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA';
  }

  puedeCancelar(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA' || r.estadoReserva === 'CONFIRMADA';
  }

  puedeFinalizar(r: Reserva): boolean {
    return r.estadoReserva === 'CONFIRMADA' && new Date(r.fechaFin) < new Date();
  }

  confirmar(): void {
    this.ejecutar((id) => this.reservaService.confirmar(id));
  }

  cancelar(): void {
    this.ejecutar((id) => this.reservaService.cancelar(id));
  }

  finalizar(): void {
    this.ejecutar((id) => this.reservaService.finalizar(id));
  }

  private ejecutar(accion: (id: number) => ReturnType<ReservaService['confirmar']>): void {
    const r = this.reserva();
    if (!r) return;
    this.error.set(null);
    this.procesando.set(true);
    accion(r.idReserva).subscribe({
      next: () => {
        this.procesando.set(false);
        this.cargar(r.idReserva);
      },
      error: (err) => {
        this.procesando.set(false);
        this.error.set(extraerMensajeError(err));
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
