import { InfoGrafico } from '../../../shared/info-grafico/info-grafico';
import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { AlojamientoRanking } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { GraficoBarras } from '../../../shared/grafico-barras/grafico-barras';

/// SCRUM-48 / RF36. Rankings globales de alojamientos: por ingresos, por
/// cantidad de reservas y por rating promedio (ver EstadisticasController.topAlojamientos).
@Component({
  selector: 'app-estadisticas-globales',
  standalone: true,
  imports: [InfoGrafico, Esqueleto, GraficoBarras],
  templateUrl: './estadisticas-globales.html',
  styleUrl: './estadisticas-globales.css',
})
export class EstadisticasGlobales {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly porIngresos = signal<AlojamientoRanking[]>([]);
  readonly porReservas = signal<AlojamientoRanking[]>([]);
  readonly porRating = signal<AlojamientoRanking[]>([]);
  readonly cargando = signal(true);

  /// Etiquetas y valores de cada ranking, en el formato del grafico.
  readonly ingresos = computed(() => this.serie(this.porIngresos()));
  readonly reservas = computed(() => this.serie(this.porReservas()));
  readonly rating = computed(() => this.serie(this.porRating()));
  readonly error = signal<string | null>(null);

  private serie(lista: AlojamientoRanking[]): { etiquetas: string[]; valores: number[] } {
    return { etiquetas: lista.map((a) => a.titulo), valores: lista.map((a) => Number(a.valor)) };
  }

  constructor() {
    // Los tres rankings juntos: la pantalla se muestra recien cuando llegaron
    // todos (antes, si "rating" llegaba primero, los otros dos decian "Sin datos").
    forkJoin({
      ingresos: this.estadisticasService.topAlojamientos('ingresos', 10),
      reservas: this.estadisticasService.topAlojamientos('reservas', 10),
      rating: this.estadisticasService.topAlojamientos('rating', 10),
    }).subscribe({
      next: (r) => {
        this.porIngresos.set(r.ingresos);
        this.porReservas.set(r.reservas);
        this.porRating.set(r.rating);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
