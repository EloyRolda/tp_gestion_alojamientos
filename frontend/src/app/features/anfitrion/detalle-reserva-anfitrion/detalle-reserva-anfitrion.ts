import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { Reserva } from '../../../core/models/reserva.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { AuthService } from '../../../core/services/auth.service';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';
import { explicarCancelacion, explicarResultado, formatearPesos, notaPrevia } from '../../../core/utils/cancelacion.util';
import { PAGOS_SIMULADOS } from '../../../core/config/api.config';
import { hoyIso, sumarDias } from '../../../core/utils/fecha.util';

/// SCRUM-39 / RF20, RF23, RF25. Detalle de una reserva propia desde el lado
/// del anfitrion: confirmar (SOLICITADA -> CONFIRMADA, dispara la preferencia
/// de pago), cancelar, finalizar (dispara el Payout -- la API de Payouts de
/// MercadoPago rechaza las credenciales TEST de esta app con
/// "Invalid signature", limitacion ajena a este codigo).
@Component({
  selector: 'app-detalle-reserva-anfitrion',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink, Estado, Esqueleto],
  templateUrl: './detalle-reserva-anfitrion.html',
  styleUrl: './detalle-reserva-anfitrion.css',
})
export class DetalleReservaAnfitrion {
  readonly id = input.required<string>();

  private readonly reservaService = inject(ReservaService);
  private readonly aviso = inject(AvisoService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly auth = inject(AuthService);

  /// Sin alias de cobro, la plataforma no tiene a donde transferirle al finalizar.
  readonly sinAliasCobro = computed(() => this.auth.usuario() != null && !this.auth.usuario()!.aliasCobro);

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

  /// Una solicitud cuya fecha de llegada ya paso no se confirma: solo se cancela.
  puedeConfirmar(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA' && !this.llegadaPasada(r);
  }

  llegadaPasada(r: Reserva): boolean {
    return r.fechaInicio < hoyIso();
  }

  puedeCancelar(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA' || r.estadoReserva === 'CONFIRMADA';
  }

  /// El backend deja finalizar desde el dia siguiente al check-out (fechaFin
  /// anterior a hoy), y solo tiene sentido si el cliente pago.
  puedeFinalizar(r: Reserva): boolean {
    return r.estadoReserva === 'CONFIRMADA' && r.pago?.estado === 'PAGADO' && r.fechaFin < hoyIso();
  }

  /// Dia desde el que se puede finalizar (para avisarlo mientras tanto).
  finalizableDesde(r: Reserva): string {
    return sumarDias(r.fechaFin, 1);
  }

  confirmar(): void {
    this.ejecutar((id) => this.reservaService.confirmar(id), 'Reserva confirmada', [
      'El cliente tiene 48 horas para pagar con MercadoPago. Si no paga, la reserva se cancela sola.',
    ]);
  }

  async cancelar(): Promise<void> {
    const r = this.reserva();
    if (!r) return;
    const ok = await this.confirmacion.pedir({
      titulo: '¿Cancelar la reserva?',
      mensaje: explicarCancelacion(r, 'anfitrion'),
      nota: notaPrevia(r, PAGOS_SIMULADOS),
      aceptar: 'Sí, cancelar',
      cancelar: 'No, mantenerla',
      peligro: true,
    });
    if (ok) this.ejecutar((id) => this.reservaService.cancelar(id), null);
  }

  async finalizar(): Promise<void> {
    const r = this.reserva();
    if (!r?.pago) return;
    const ok = await this.confirmacion.pedir({
      titulo: 'Finalizar la estadía',
      mensaje: [
        `La reserva queda cerrada y la plataforma te transfiere ${formatearPesos(Number(r.pago.montoBase))} a tu alias de cobro.`,
        'Después de esto el cliente puede dejar su reseña.',
      ],
      nota: PAGOS_SIMULADOS ? '[Modo desarrollo: la transferencia con MercadoPago se simula.]' : undefined,
      aceptar: 'Finalizar',
    });
    if (ok) this.ejecutar((id) => this.reservaService.finalizar(id), null);
  }

  /// mensajeOk null = mostrar el cartel con el resultado (cancelar, finalizar).
  private ejecutar(
    accion: (id: number) => ReturnType<ReservaService['confirmar']>,
    mensajeOk: string | null,
    detalle: string[] = [],
  ): void {
    const r = this.reserva();
    if (!r) return;
    this.error.set(null);
    this.procesando.set(true);
    accion(r.idReserva).subscribe({
      next: () => {
        this.procesando.set(false);
        if (mensajeOk) {
          this.aviso.mostrar(mensajeOk, detalle);
          this.cargar(r.idReserva);
          return;
        }
        this.reservaService.obtener(r.idReserva).subscribe({
          next: (actualizada) => {
            this.reserva.set(actualizada);
            void this.confirmacion.informar({
              ...explicarResultado(actualizada, 'anfitrion', PAGOS_SIMULADOS),
              aceptar: 'Continuar',
            });
          },
          error: () => this.cargar(r.idReserva),
        });
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
