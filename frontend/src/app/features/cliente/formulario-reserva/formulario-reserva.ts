import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { ReservaService } from '../../../core/services/reserva.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { calcularNoches, calcularTotal } from '../../../core/utils/reserva.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';

/// SCRUM-29 / RF18, RF19. "id" se bindea solo desde la ruta :id (withComponentInputBinding).
/// El calculo de precio es en vivo del lado del cliente (noches * precioNoche,
/// mismo criterio que PagoService.calcularMontoBase en el backend); la
/// validacion de solapamiento de fechas NO se duplica aca -- se manda el
/// POST y se muestra el error real que devuelve la API (ReservaService.crear
/// ya hace ese chequeo contra la base real, con lock pesimista).
@Component({
  selector: 'app-formulario-reserva',
  standalone: true,
  imports: [FormsModule, RouterLink, DecimalPipe, Esqueleto],
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
  readonly fechaMinima = new Date().toISOString().slice(0, 10);

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
          this.aviso.mostrar('Solicitud enviada al anfitrión');
          this.router.navigate(['/mis-reservas', reserva.idReserva]);
        },
        error: (err) => {
          this.enviando.set(false);
          this.error.set(extraerMensajeError(err));
        },
      });
  }
}
