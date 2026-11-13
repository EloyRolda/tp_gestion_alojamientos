import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { ReservaService } from '../../../core/services/reserva.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { CalendarioRango, RangoOcupado } from '../../../shared/calendario-rango/calendario-rango';
import { hoyIso } from '../../../core/utils/fecha.util';

/// SCRUM-29 / RF18, RF19. "id" se bindea solo desde la ruta :id (withComponentInputBinding).
/// Las fechas se eligen en un calendario de rango (app-calendario-rango). En
/// gris quedan los dias pasados y las noches que el propio cliente ya tiene
/// reservadas en este alojamiento; las reservas de otras personas no las
/// expone la API, asi que ese caso lo sigue resolviendo el backend al enviar.
/// El calculo de precio es en vivo del lado del cliente (noches * precioNoche,
/// mismo criterio que PagoService.calcularMontoBase en el backend); la
/// validacion de solapamiento de fechas NO se duplica aca -- se manda el
/// POST y se muestra el error real que devuelve la API (ReservaService.crear
/// ya hace ese chequeo contra la base real, con lock pesimista).
@Component({
  selector: 'app-formulario-reserva',
  standalone: true,
  imports: [RouterLink, DecimalPipe, Esqueleto, CalendarioRango],
  templateUrl: './formulario-reserva.html',
  styleUrl: './formulario-reserva.css',
})
export class FormularioReserva {
  readonly id = input.required<string>();

  private readonly alojamientoService = inject(AlojamientoService);
  private readonly aviso = inject(AvisoService);
  private readonly reservaService = inject(ReservaService);
  private readonly router = inject(Router);

  readonly alojamiento = signal<Alojamiento | null>(null);
  readonly cargando = signal(true);
  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);

  readonly fechaInicio = signal('');
  readonly fechaFin = signal('');
  /// Fecha local (no UTC): a la noche, toISOString() ya devolveria mañana.
  readonly fechaMinima = hoyIso();
  /// Noches de este alojamiento que el cliente ya tiene reservadas (no canceladas).
  readonly ocupados = signal<RangoOcupado[]>([]);

  readonly noches = computed(() => {
    if (!this.fechaInicio() || !this.fechaFin()) return 0;
    return Math.max(0, calcularNoches(this.fechaInicio(), this.fechaFin()));
  });

  readonly total = computed(() => {
    const a = this.alojamiento();
    if (!a || !this.fechaInicio() || !this.fechaFin()) return 0;
    return Math.max(0, calcularTotal(this.fechaInicio(), this.fechaFin(), a.precioNoche));
  });

  readonly fechasValidas = computed(() => this.noches() > 0);

  constructor() {
    effect(() => {
      const idNumerico = Number(this.id());
      if (!Number.isFinite(idNumerico)) return;
      this.cargarOcupados(idNumerico);
      this.alojamientoService.obtener(idNumerico).subscribe({
        next: (a) => {
          this.alojamiento.set(a);
          this.cargando.set(false);
        },
        error: (err) => {
          this.error.set(extraerMensajeError(err));
          this.cargando.set(false);
        },
      });
    });
  }

  private cargarOcupados(idAlojamiento: number): void {
    // Si falla, el calendario solo pierde ese dato: el backend igual valida.
    this.reservaService.listarPropios().subscribe({
      next: (reservas) =>
        this.ocupados.set(
          reservas
            .filter((r) => r.alojamiento.idAlojamiento === idAlojamiento && r.estadoReserva !== 'CANCELADA')
            .map((r) => ({ inicio: r.fechaInicio, fin: r.fechaFin })),
        ),
    });
  }

  reservar(): void {
    const a = this.alojamiento();
    if (!a || !this.fechasValidas()) return;

    this.error.set(null);
    this.enviando.set(true);
    this.reservaService
      .registrar({
        idAlojamiento: a.idAlojamiento,
        fechaInicio: this.fechaInicio(),
        fechaFin: this.fechaFin(),
      })
      .subscribe({
        next: (reserva) => {
          this.enviando.set(false);
          this.aviso.mostrar('Solicitud enviada al anfitrión', [
            'Cuando la confirme, vas a tener 48 horas para pagar con MercadoPago.',
          ]);
          this.router.navigate(['/mis-reservas', reserva.idReserva]);
        },
        error: (err) => {
          this.enviando.set(false);
          this.error.set(extraerMensajeError(err));
        },
      });
  }
}
