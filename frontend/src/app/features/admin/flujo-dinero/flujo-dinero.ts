import { InfoGrafico } from '../../../shared/info-grafico/info-grafico';
import { Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CalendarioRango } from '../../../shared/calendario-rango/calendario-rango';
import { desdeIso } from '../../../core/utils/fecha.util';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { CancelacionPorMotivo, FlujoDinero, IngresoMensual } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { GraficoBarras } from '../../../shared/grafico-barras/grafico-barras';
import { MotivoCancelacion } from '../../../core/models/pago.model';

/// Motivos de cancelacion en palabras cortas, para que entren en el eje del
/// grafico (en la base se guardan como codigo).
const MOTIVOS: Record<MotivoCancelacion, string> = {
  ANFITRION: 'Anfitrión',
  CLIENTE_ANTICIPADA: 'Cliente, anticipada',
  CLIENTE_TARDIA: 'Cliente, < 48 h',
  VENCIMIENTO_PAGO: 'Pago vencido',
  ADMIN: 'Administrador',
};

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
  imports: [InfoGrafico, DecimalPipe, FormsModule, Esqueleto, GraficoBarras, CalendarioRango],
  templateUrl: './flujo-dinero.html',
  styleUrl: './flujo-dinero.css',
})
export class FlujoDineroComponent {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly flujo = signal<FlujoDinero | null>(null);
  readonly cargandoFlujo = signal(true);
  readonly error = signal<string | null>(null);

  /// Periodo aplicado (vacio = todo el historial).
  readonly desde = signal('');
  readonly hasta = signal('');
  /// Calendario del periodo (con el calendario de la app, no el del navegador).
  readonly eligiendoPeriodo = signal(false);
  readonly desdeElegido = signal('');
  readonly hastaElegido = signal('');
  readonly textoPeriodo = computed(() => {
    const f = new Intl.DateTimeFormat('es-AR', { day: '2-digit', month: '2-digit', year: 'numeric' });
    if (!this.desde() || !this.hasta()) return 'todo el historial';
    return `del ${f.format(desdeIso(this.desde()))} al ${f.format(desdeIso(this.hasta()))}`;
  });

  readonly anio = signal(new Date().getFullYear());
  readonly ingresosMensuales = signal<IngresoMensual[]>([]);
  readonly cargandoIngresos = signal(true);

  readonly totalAnual = computed(() => this.ingresosMensuales().reduce((suma, i) => suma + Number(i.totalFacturado), 0));
  readonly etiquetasMeses = computed(() => this.ingresosMensuales().map((i) => MESES[i.mes - 1]));
  readonly montosMeses = computed(() => this.ingresosMensuales().map((i) => Number(i.totalFacturado)));

  readonly cancelaciones = signal<CancelacionPorMotivo[]>([]);
  readonly cargandoCancelaciones = signal(true);

  readonly etiquetasCancelaciones = computed(() => this.cancelaciones().map((c) => MOTIVOS[c.motivo] ?? c.motivo));
  readonly cantidadesCancelaciones = computed(() => this.cancelaciones().map((c) => Number(c.cantidad)));

  constructor() {
    this.cargarFlujo();
    this.cargarIngresosMensuales();
    this.cargarCancelaciones();
  }

  /// Al elegir el ultimo dia se aplica el filtro y se cierra el calendario.
  alElegirFin(fin: string): void {
    this.hastaElegido.set(fin);
    if (!fin || !this.desdeElegido()) return;
    this.desde.set(this.desdeElegido());
    this.hasta.set(fin);
    this.eligiendoPeriodo.set(false);
    this.aplicarFiltroFechas();
  }

  verTodo(): void {
    this.desde.set('');
    this.hasta.set('');
    this.desdeElegido.set('');
    this.hastaElegido.set('');
    this.eligiendoPeriodo.set(false);
    this.aplicarFiltroFechas();
  }

  aplicarFiltroFechas(): void {
    this.cargarFlujo();
    this.cargarCancelaciones();
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
