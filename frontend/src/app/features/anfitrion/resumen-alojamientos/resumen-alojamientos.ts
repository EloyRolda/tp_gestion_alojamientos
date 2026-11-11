import { Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { ResumenAlojamientoAnfitrion } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

/// SCRUM-41 / RF37. Un resumen por cada alojamiento propio: ingresos,
/// cantidad de reservas y rating promedio (null si todavia no tiene reviews).
@Component({
  selector: 'app-resumen-alojamientos',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './resumen-alojamientos.html',
  styleUrl: './resumen-alojamientos.css',
})
export class ResumenAlojamientos {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly resumen = signal<ResumenAlojamientoAnfitrion[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  /// Totales de todos los alojamientos, para las tarjetas de arriba (SCRUM-56).
  readonly ingresosTotales = computed(() => this.resumen().reduce((suma, r) => suma + Number(r.ingresosTotales), 0));
  readonly reservasTotales = computed(() => this.resumen().reduce((suma, r) => suma + r.cantidadReservas, 0));

  constructor() {
    this.estadisticasService.resumenAnfitrion().subscribe({
      next: (r) => {
        this.resumen.set(r);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
