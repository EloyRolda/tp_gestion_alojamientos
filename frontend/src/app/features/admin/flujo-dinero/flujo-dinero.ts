import { Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { CancelacionPorMotivo, FlujoDinero, IngresoMensual } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

const MESES = [
  'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic',
];

/// SCRUM-47 / RF32. Panel financiero del admin: facturacion, comisiones,
/// liquidado a anfitriones y reembolsado a clientes (con filtro opcional de
/// fechas), ingresos mensuales del año, y cancelaciones agrupadas por motivo.
/// Responsive (SCRUM-56): los ingresos mensuales se muestran como barras.
@Component({
  selector: 'app-flujo-dinero',
  standalone: true,
  imports: [DecimalPipe, FormsModule],
  templateUrl: './flujo-dinero.html',
  styleUrl: './flujo-dinero.css',
})
export class FlujoDineroComponent {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly flujo = signal<FlujoDinero | null>(null);
  readonly cargandoFlujo = signal(true);
  readonly error = signal<string | null>(null);

  readonly desde = signal('');
  readonly hasta = signal('');

  readonly anio = signal(new Date().getFullYear());
  readonly ingresosMensuales = signal<IngresoMensual[]>([]);
  readonly cargandoIngresos = signal(true);

  readonly totalAnual = computed(() => this.ingresosMensuales().reduce((suma, i) => suma + Number(i.totalFacturado), 0));
  private readonly maximoMensual = computed(() => Math.max(0, ...this.ingresosMensuales().map((i) => Number(i.totalFacturado))));

  readonly cancelaciones = signal<CancelacionPorMotivo[]>([]);
  readonly cargandoCancelaciones = signal(true);

  readonly meses = MESES;

  constructor() {
    this.cargarFlujo();
    this.cargarIngresosMensuales();
    this.cargarCancelaciones();
  }

  aplicarFiltroFechas(): void {
    this.cargarFlujo();
    this.cargarCancelaciones();
  }

  /// Ancho de la barra de un mes, relativo al mes de mayor facturacion del año.
  porcentajeDelMaximo(monto: number): number {
    const maximo = this.maximoMensual();
    return maximo > 0 ? (Number(monto) / maximo) * 100 : 0;
  }

  cambiarAnio(): void {
    this.cargarIngresosMensuales();
  }

  private cargarFlujo(): void {
    this.cargandoFlujo.set(true);
    this.estadisticasService.flujoDeDinero(this.desde() || undefined, this.hasta() || undefined).subscribe({
      next: (f) => {
        this.flujo.set(f);
        this.cargandoFlujo.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargandoFlujo.set(false);
      },
    });
  }

  private cargarIngresosMensuales(): void {
    this.cargandoIngresos.set(true);
    this.estadisticasService.ingresosMensuales(this.anio()).subscribe({
      next: (i) => {
        this.ingresosMensuales.set(i);
        this.cargandoIngresos.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargandoIngresos.set(false);
      },
    });
  }

  private cargarCancelaciones(): void {
    this.cargandoCancelaciones.set(true);
    this.estadisticasService.cancelacionesPorMotivo(this.desde() || undefined, this.hasta() || undefined).subscribe({
      next: (c) => {
        this.cancelaciones.set(c);
        this.cargandoCancelaciones.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargandoCancelaciones.set(false);
      },
    });
  }
}
