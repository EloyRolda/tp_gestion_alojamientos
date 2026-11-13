import { Component, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ReservaService } from '../../../core/services/reserva.service';
import { PagoService } from '../../../core/services/pago.service';
import { Reserva, UsuarioResumen } from '../../../core/models/reserva.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Estado } from '../../../shared/estado/estado';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';
import { explicarCancelacion, explicarResultado, formatearPesos, notaPrevia } from '../../../core/utils/cancelacion.util';
import { PAGOS_SIMULADOS } from '../../../core/config/api.config';
import { hoyIso } from '../../../core/utils/fecha.util';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { UsuarioService } from '../../../core/services/usuario.service';

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
  private readonly pagoService = inject(PagoService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly usuarioService = inject(UsuarioService);
  /// null = todavia no se sabe; true/false = el anfitrion tiene (o no) alias de cobro.
  /// Sin alias el backend rechaza "Finalizar" (no tiene a donde transferir).
  readonly anfitrionConAlias = signal<boolean | null>(null);
  /// Anfitrion del alojamiento (la reserva no lo trae): para el link a su ficha.
  readonly anfitrion = signal<UsuarioResumen | null>(null);

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

  /// Mismas condiciones que para el anfitrion: confirmada, pagada y con la
  /// salida ya pasada (el backend deja finalizar tambien al admin).
  puedeFinalizar(r: Reserva): boolean {
    return r.estadoReserva === 'CONFIRMADA' && r.pago?.estado === 'PAGADO' && r.fechaFin < hoyIso();
  }

  async finalizar(): Promise<void> {
    const r = this.reserva();
    if (!r?.pago) return;
    const ok = await this.confirmacion.pedir({
      titulo: 'Finalizar la estadía',
      mensaje: [
        `La reserva queda cerrada y se le transfieren ${formatearPesos(Number(r.pago.montoBase))} al anfitrión.`,
        'Después de esto el cliente puede dejar su reseña.',
      ],
      nota: PAGOS_SIMULADOS ? '[Modo desarrollo: la transferencia con MercadoPago se simula.]' : undefined,
      aceptar: 'Finalizar',
    });
    if (!ok) return;
    this.error.set(null);
    this.procesando.set(true);
    this.reservaService.finalizar(r.idReserva).subscribe({
      next: () => {
        this.procesando.set(false);
        this.mostrarResultado(r.idReserva);
      },
      error: (err) => {
        this.procesando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  puedeCancelar(r: Reserva): boolean {
    return r.estadoReserva === 'SOLICITADA' || r.estadoReserva === 'CONFIRMADA';
  }

  /// El reembolso manual solo tiene sentido si ya existe un Pago para
  /// devolver (CONFIRMADA) -- una SOLICITADA nunca tuvo Pago, asi que
  /// cancelarla con reembolsoAdmin solo cancelaria sin mover plata.
  /// Y ademas el cliente tiene que haber pagado: si el pago sigue PENDIENTE no
  /// hay nada que devolver (el backend solo cancelaria).
  puedeReembolsar(r: Reserva): boolean {
    return r.estadoReserva === 'CONFIRMADA' && r.pago?.estado === 'PAGADO';
  }

  /// Tope del reembolso manual: el monto base (los cargos nunca se devuelven).
  montoMaximo(r: Reserva): number {
    return Number(r.pago?.montoBase ?? 0);
  }

  async cancelar(): Promise<void> {
    const r = this.reserva();
    if (!r) return;
    const ok = await this.confirmacion.pedir({
      titulo: '¿Cancelar la reserva con la regla automática?',
      mensaje: explicarCancelacion(r, 'admin'),
      nota: notaPrevia(r, PAGOS_SIMULADOS),
      aceptar: 'Sí, cancelar',
      cancelar: 'No, volver',
      peligro: true,
    });
    if (!ok) return;
    this.error.set(null);
    this.procesando.set(true);
    this.reservaService.cancelar(r.idReserva).subscribe({
      next: () => {
        this.procesando.set(false);
        this.mostrarResultado(r.idReserva);
      },
      error: (err) => {
        this.procesando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  async enviarReembolso(): Promise<void> {
    const r = this.reserva();
    const monto = Number(this.montoReembolso());
    if (!r || this.montoReembolso() === null || monto < 0) return;
    const maximo = this.montoMaximo(r);
    if (monto > maximo) {
      this.errorReembolso.set(`El reembolso no puede superar el monto base (${formatearPesos(maximo)}).`);
      return;
    }
    const ok = await this.confirmacion.pedir({
      titulo: 'Cancelar con reembolso manual',
      mensaje: [
        `La reserva se cancela. El cliente recibe ${formatearPesos(monto)} y el anfitrión ${formatearPesos(maximo - monto)}.`,
        'Los cargos de servicio no se devuelven. Esto no se puede deshacer.',
      ],
      nota: notaPrevia(r, PAGOS_SIMULADOS),
      aceptar: 'Ejecutar reembolso',
      peligro: true,
    });
    if (!ok) return;

    this.errorReembolso.set(null);
    this.enviandoReembolso.set(true);
    this.pagoService.reembolsoAdmin({ idReserva: r.idReserva, montoReembolsoCliente: monto }).subscribe({
      next: () => {
        this.enviandoReembolso.set(false);
        this.montoReembolso.set(null);
        this.mostrarResultado(r.idReserva);
      },
      error: (err) => {
        this.enviandoReembolso.set(false);
        this.errorReembolso.set(extraerMensajeError(err));
      },
    });
  }

  /// Recarga la reserva y muestra el cartel con lo que paso con la plata.
  private mostrarResultado(idReserva: number): void {
    this.reservaService.obtener(idReserva).subscribe({
      next: (actualizada) => {
        this.reserva.set(actualizada);
        void this.confirmacion.informar({ ...explicarResultado(actualizada, 'admin', PAGOS_SIMULADOS), aceptar: 'Continuar' });
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
        this.cargarAnfitrion(r);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  /// Quien es el anfitrion y si ya cargo alias de cobro (sin alias no se le
  /// puede liquidar: "Finalizar" lo avisa en vez de fallar).
  private cargarAnfitrion(r: Reserva): void {
    this.anfitrionConAlias.set(null);
    this.anfitrion.set(null);
    this.alojamientoService.obtener(r.alojamiento.idAlojamiento).subscribe({
      next: (a) => {
        this.anfitrion.set(a.anfitrion);
        this.usuarioService.mostrar(a.anfitrion.id).subscribe({
          next: (u) => this.anfitrionConAlias.set(!!u.aliasCobro),
          // Si no se puede averiguar, se ofrece igual (el backend valida).
          error: () => this.anfitrionConAlias.set(true),
        });
      },
      error: () => this.anfitrionConAlias.set(true),
    });
  }
}
