import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { AlojamientoRanking } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

/// SCRUM-48 / RF36. Rankings globales de alojamientos: por ingresos, por
/// cantidad de reservas y por rating promedio (ver EstadisticasController.topAlojamientos).
@Component({
  selector: 'app-estadisticas-globales',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './estadisticas-globales.html',
  styleUrl: './estadisticas-globales.css',
})
export class EstadisticasGlobales {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly porIngresos = signal<AlojamientoRanking[]>([]);
  readonly porReservas = signal<AlojamientoRanking[]>([]);
  readonly porRating = signal<AlojamientoRanking[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    this.estadisticasService.topAlojamientos('ingresos', 10).subscribe({ next: (r) => this.porIngresos.set(r) });
    this.estadisticasService.topAlojamientos('reservas', 10).subscribe({ next: (r) => this.porReservas.set(r) });
    this.estadisticasService.topAlojamientos('rating', 10).subscribe({
      next: (r) => {
        this.porRating.set(r);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
