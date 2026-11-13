import { InfoGrafico } from '../../../shared/info-grafico/info-grafico';
import { Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { ResumenAlojamientoAnfitrion } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';
import { GraficoBarras } from '../../../shared/grafico-barras/grafico-barras';
import { RouterLink } from '@angular/router';

/// SCRUM-41 / RF37. Un resumen por cada alojamiento propio: ingresos,
/// cantidad de reservas y rating promedio (null si todavia no tiene reviews).
@Component({
  selector: 'app-resumen-alojamientos',
  standalone: true,
  imports: [InfoGrafico, DecimalPipe, Esqueleto, Vacio, RouterLink, GraficoBarras],
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

  /// Para los graficos: cada alojamiento ordenado de mayor a menor.
  readonly porIngresos = computed(() => [...this.resumen()].sort((a, b) => Number(b.ingresosTotales) - Number(a.ingresosTotales)));
  readonly porReservas = computed(() => [...this.resumen()].sort((a, b) => b.cantidadReservas - a.cantidadReservas));
  readonly titulosPorIngresos = computed(() => this.porIngresos().map((r) => r.titulo));
  readonly montosPorIngresos = computed(() => this.porIngresos().map((r) => Number(r.ingresosTotales)));
  readonly titulosPorReservas = computed(() => this.porReservas().map((r) => r.titulo));
  readonly cantidadesPorReservas = computed(() => this.porReservas().map((r) => r.cantidadReservas));

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
