import { InfoGrafico } from '../../../shared/info-grafico/info-grafico';
import { Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { ReservaService } from '../../../core/services/reserva.service';
import { ResumenCliente } from '../../../core/models/estadisticas.model';
import { Reserva } from '../../../core/models/reserva.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { GraficoBarras } from '../../../shared/grafico-barras/grafico-barras';

const MESES = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

/// SCRUM-34 / RF38. Resumen de gastos del cliente logueado, mas un grafico de
/// lo que pago mes a mes en los ultimos 12 meses.
///
/// El grafico usa el mismo criterio que el total del backend
/// (PagoRepository.resumenGastoCliente): cuenta los pagos que no estan
/// PENDIENTE ni VENCIDO, por lo que pago el cliente (montoTotalCliente).
@Component({
  selector: 'app-resumen-gastos',
  standalone: true,
  imports: [InfoGrafico, DecimalPipe, Esqueleto, GraficoBarras],
  templateUrl: './resumen-gastos.html',
  styleUrl: './resumen-gastos.css',
})
export class ResumenGastos {
  private readonly estadisticasService = inject(EstadisticasService);
  private readonly reservaService = inject(ReservaService);

  readonly resumen = signal<ResumenCliente | null>(null);
  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  /// Ultimos 12 meses (incluido el actual), del mas viejo al mas nuevo.
  private readonly ultimosMeses = computed(() => {
    const hoy = new Date();
    return Array.from({ length: 12 }, (_, i) => {
      const d = new Date(hoy.getFullYear(), hoy.getMonth() - 11 + i, 1);
      return { anio: d.getFullYear(), mes: d.getMonth() };
    });
  });

  readonly etiquetasMeses = computed(() =>
    this.ultimosMeses().map((m) => `${MESES[m.mes]} ${String(m.anio).slice(2)}`),
  );

  readonly gastoPorMes = computed(() => {
    const totales = new Map<string, number>();
    for (const r of this.reservas()) {
      const p = r.pago;
      if (!p || p.estado === 'PENDIENTE' || p.estado === 'VENCIDO') continue;
      // Sin fecha de pago registrada (datos viejos), se usa la de la estadia.
      const fecha = new Date((p.fechaPago ?? r.fechaInicio).slice(0, 10) + 'T00:00:00');
      const clave = `${fecha.getFullYear()}-${fecha.getMonth()}`;
      totales.set(clave, (totales.get(clave) ?? 0) + Number(p.montoTotalCliente));
    }
    return this.ultimosMeses().map((m) => totales.get(`${m.anio}-${m.mes}`) ?? 0);
  });

  readonly hayGastosRecientes = computed(() => this.gastoPorMes().some((v) => v > 0));

  constructor() {
    this.estadisticasService.resumenCliente().subscribe({
      next: (r) => {
        this.resumen.set(r);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
    // Si falla, solo se pierde el grafico: el resumen de arriba sigue.
    this.reservaService.listarPropios().subscribe({ next: (r) => this.reservas.set(r) });
  }
}
