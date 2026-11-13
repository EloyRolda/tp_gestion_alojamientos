import {
  Component,
  DestroyRef,
  ElementRef,
  afterNextRender,
  computed,
  effect,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
import type { Chart as ChartJs, Plugin } from 'chart.js';

/// Como se muestran los valores en ejes, tooltip y tabla.
export type FormatoValor = 'pesos' | 'entero' | 'decimal';

const FORMATOS: Record<FormatoValor, Intl.NumberFormat> = {
  pesos: new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 0 }),
  entero: new Intl.NumberFormat('es-AR', { maximumFractionDigits: 0 }),
  decimal: new Intl.NumberFormat('es-AR', { minimumFractionDigits: 1, maximumFractionDigits: 1 }),
};

/// Pesos abreviados para el eje ($1,2 M, $350 mil): el valor exacto va en el tooltip.
const PESOS_CORTO = new Intl.NumberFormat('es-AR', {
  style: 'currency',
  currency: 'ARS',
  notation: 'compact',
  maximumFractionDigits: 1,
});

/// Largo maximo de una etiqueta en el eje de las barras horizontales.
const LARGO_ETIQUETA = 24;

/// Grafico de barras de una sola serie (Chart.js), para los paneles de
/// estadisticas. Pensado para que se entienda sin saber leer graficos:
/// - arriba, una frase con lo importante ("X encabeza con $Y, el 40% del total");
/// - cada barra tiene su valor escrito al lado (no hace falta pasar el mouse);
/// - la barra mas alta va en color tomate (la misma marca que la frase) y el
///   resto en azul. Sin leyenda: el titulo de la seccion ya dice que se mide.
///
/// - Chart.js se descarga recien cuando el grafico aparece en pantalla (import
///   dinamico): no suma peso a la carga inicial de la app.
/// - Al pasar el mouse (o tocar) una barra se ve el valor exacto.
/// - Debajo, "Ver los datos" muestra los mismos numeros en una tabla, para
///   lectores de pantalla o para copiarlos.
/// - Los colores salen de las variables de :root (styles.css).
@Component({
  selector: 'app-grafico-barras',
  standalone: true,
  templateUrl: './grafico-barras.html',
  styleUrl: './grafico-barras.css',
})
export class GraficoBarras {
  private readonly lienzo = viewChild.required<ElementRef<HTMLCanvasElement>>('lienzo');

  readonly etiquetas = input.required<string[]>();
  readonly valores = input.required<number[]>();
  /// Que representa cada barra (encabezado de la tabla y descripcion del grafico).
  readonly descripcion = input.required<string>();
  readonly nombreEtiqueta = input('');
  readonly formato = input<FormatoValor>('entero');
  /// "horizontal" para rankings con nombres largos; "vertical" para meses.
  readonly orientacion = input<'vertical' | 'horizontal'>('vertical');
  /// Tope del eje de valores (por ejemplo 5 para el rating).
  readonly maximo = input<number | null>(null);
  /// true cuando las barras son TODO el universo (todos mis alojamientos, todos
  /// los motivos): la frase dice que parte del total es la mas alta. En un
  /// "top 10" eso no seria cierto, asi que se compara contra la segunda.
  readonly parteDelTotal = input(false);

  readonly filas = computed(() => this.etiquetas().map((e, i) => ({ etiqueta: e, valor: this.valores()[i] ?? 0 })));
  readonly alto = computed(() =>
    this.orientacion() === 'horizontal' ? Math.max(160, this.etiquetas().length * 38 + 48) : 280,
  );
  readonly error = signal(false);

  /// Posiciones de la(s) barra(s) mas alta(s) (puede haber empate).
  private readonly destacadas = computed(() => {
    const v = this.valores();
    const max = Math.max(0, ...v);
    const idx = max > 0 ? v.map((x, i) => (x === max ? i : -1)).filter((i) => i >= 0) : [];
    // Si todas empatan, no hay ninguna que resaltar.
    return idx.length > 1 && idx.length === v.length ? [] : idx;
  });

  /// La frase de arriba del grafico, en partes para poder resaltar nombre y valor.
  readonly conclusion = computed<{ marca?: boolean; antes: string; nombre: string; medio: string; valor: string; despues: string } | null>(() => {
    const filas = this.filas();
    const idx = this.destacadas();
    if (filas.length > 1 && filas.every((f) => f.valor === filas[0].valor) && filas[0].valor > 0) {
      const de = this.maximo() != null ? ` de ${this.maximo()}` : '';
      return { marca: false, antes: '', nombre: `Los ${filas.length}`, medio: ' están empatados con ', valor: this.formatear(filas[0].valor), despues: `${de}.` };
    }
    if (filas.length === 0 || idx.length === 0) return null;
    const top = filas[idx[0]];
    const valor = this.formatear(top.valor);
    const nombre = idx.length > 1 ? `${top.etiqueta} y ${idx.length - 1} más` : top.etiqueta;

    if (this.orientacion() === 'vertical') {
      const conDatos = filas.filter((f) => f.valor > 0).length;
      return {
        antes: idx.length > 1 ? 'Los meses más altos fueron ' : 'El mes más alto fue ',
        nombre,
        medio: ', con ',
        valor,
        despues: `. Hubo movimiento en ${conDatos} de ${filas.length} meses.`,
      };
    }
    const verbo = idx.length > 1 ? ' empatan arriba con ' : ' encabeza con ';
    if (this.maximo() != null) {
      return { antes: '', nombre, medio: verbo, valor, despues: ` de ${this.maximo()}.` };
    }
    if (this.parteDelTotal()) {
      const total = filas.reduce((a, f) => a + f.valor, 0);
      const pct = Math.round((top.valor / total) * 100);
      const despues =
        this.formato() === 'entero'
          ? ` de ${this.formatear(total)} (${pct}%).`
          : `, el ${pct}% del total (${this.formatear(total)}).`;
      return { antes: '', nombre, medio: verbo, valor, despues };
    }
    // Ranking parcial (top 10): contra la segunda barra.
    const segunda = [...filas.map((f) => f.valor)].sort((a, b) => b - a)[idx.length];
    let despues = '.';
    if (idx.length === 1 && segunda != null && segunda > 0) {
      const veces = top.valor / segunda;
      despues = veces >= 2 ? ', más del doble que el segundo.' : `, un ${Math.round((veces - 1) * 100)}% más que el segundo.`;
    }
    return { antes: '', nombre, medio: verbo, valor, despues };
  });

  private grafico: ChartJs | null = null;
  /// Se leen de las variables de :root al crear el grafico.
  private colorBarra = '#3f88c5';
  private colorDestacada = '#bf4527';

  /// Un color por barra: tomate la(s) mas alta(s), azul el resto.
  private colores(): string[] {
    const d = new Set(this.destacadas());
    return this.valores().map((_, i) => (d.has(i) ? this.colorDestacada : this.colorBarra));
  }

  constructor() {
    afterNextRender(() => {
      void this.crear();
    });

    effect(() => {
      const etiquetas = this.etiquetas();
      const valores = this.valores();
      const g = this.grafico;
      if (g) {
        g.data.labels = etiquetas;
        g.data.datasets[0].data = valores;
        g.data.datasets[0].backgroundColor = this.colores();
        g.update();
      }
    });

    inject(DestroyRef).onDestroy(() => this.grafico?.destroy());
  }

  /// Sin el espacio que Intl pone despues del "$", igual que en el resto de la app.
  formatear(valor: number): string {
    return FORMATOS[this.formato()].format(valor).replace(/^(-?)\$\s/, '$1$');
  }

  private async crear(): Promise<void> {
    let Chart: typeof import('./chart-registro').Chart;
    try {
      ({ Chart } = await import('./chart-registro'));
    } catch {
      // Sin el grafico la tabla de datos sigue disponible.
      this.error.set(true);
      return;
    }

    const css = getComputedStyle(document.documentElement);
    const token = (nombre: string, respaldo: string) => css.getPropertyValue(nombre).trim() || respaldo;
    this.colorBarra = token('--color-azul', '#3f88c5');
    this.colorDestacada = token('--color-tomate-fuerte', '#bf4527');
    const colorTinta = token('--color-ink', '#080708');
    const colorTexto = token('--color-text-muted', '#5e5852');
    const colorGrilla = token('--color-acero-suave', '#eceff2');
    const fuente = token('--font', 'system-ui');

    const horizontal = this.orientacion() === 'horizontal';
    const formato = this.formato();
    const formatoEje = (v: number) =>
      (formato === 'pesos' ? PESOS_CORTO.format(v) : FORMATOS[formato].format(v)).replace(/^(-?)\$\s/, '$1$');
    const ejeValores = {
      // En las barras horizontales el valor ya esta escrito al lado de cada
      // una: el eje de valores sobra y se oculta (menos ruido).
      display: !horizontal,
      beginAtZero: true,
      max: this.maximo() ?? undefined,
      border: { display: false },
      grid: { color: colorGrilla },
      ticks: {
        color: colorTexto,
        maxTicksLimit: 6,
        precision: formato === 'decimal' ? undefined : 0,
        callback: (v: string | number) => formatoEje(Number(v)),
      },
    };
    const ejeCategorias = {
      border: { display: false },
      grid: { display: false },
      ticks: {
        color: colorTexto,
        autoSkip: false,
        // Los meses van derechos (sin inclinar) aunque el celular sea angosto.
        maxRotation: 0,
        font: { size: horizontal ? 12 : 11 },
        callback: function (this: { getLabelForValue(v: number): string }, v: string | number) {
          const texto = this.getLabelForValue(Number(v));
          return horizontal && texto.length > LARGO_ETIQUETA ? texto.slice(0, LARGO_ETIQUETA - 1) + '…' : texto;
        },
      },
    };

    const fuenteValor = `600 12px ${fuente}`;

    /// Escribe el valor de cada barra: a la derecha de la barra (horizontal) o
    /// arriba (vertical). Si en vertical no entra en el ancho de su columna (12
    /// meses en un celular), no se escribe: queda el tooltip y la tabla.
    const valoresEnBarras: Plugin<'bar'> = {
      id: 'valoresEnBarras',
      afterDatasetsDraw: (chart) => {
        const ctx = chart.ctx;
        const meta = chart.getDatasetMeta(0);
        const datos = chart.data.datasets[0].data as number[];
        const ancho = chart.chartArea.width / Math.max(1, datos.length);
        ctx.save();
        ctx.font = fuenteValor;
        ctx.fillStyle = colorTexto;
        meta.data.forEach((barra, i) => {
          const v = Number(datos[i]);
          if (!horizontal && v === 0) return;
          const texto = formatoEje(v);
          const w = ctx.measureText(texto).width;
          const { x, y } = barra.getProps(['x', 'y'], true) as { x: number; y: number };
          if (horizontal) {
            ctx.textAlign = 'left';
            ctx.textBaseline = 'middle';
            ctx.fillText(texto, x + 6, y);
          } else {
            if (w > ancho - 2) return;
            ctx.textAlign = 'center';
            ctx.textBaseline = 'bottom';
            ctx.fillText(texto, x, y - 4);
          }
        });
        ctx.restore();
      },
    };

    this.grafico = new Chart(this.lienzo().nativeElement, {
      type: 'bar',
      plugins: [valoresEnBarras],
      data: {
        labels: this.etiquetas(),
        datasets: [
          {
            label: this.descripcion(),
            data: this.valores(),
            backgroundColor: this.colores(),
            hoverBackgroundColor: colorTinta,
            // Puntas redondeadas solo en el extremo libre (la base queda recta, sobre el eje).
            borderRadius: 4,
            borderSkipped: 'start',
            maxBarThickness: 28,
            categoryPercentage: 0.8,
            barPercentage: 0.9,
          },
        ],
      },
      options: {
        indexAxis: horizontal ? 'y' : 'x',
        responsive: true,
        maintainAspectRatio: false,
        animation: window.matchMedia('(prefers-reduced-motion: reduce)').matches ? false : { duration: 400 },
        font: { family: fuente },
        // Lugar para el valor escrito: a la derecha (horizontal) o arriba (vertical).
        layout: { padding: horizontal ? { right: 72 } : { top: 20 } },
        interaction: { mode: 'index', intersect: false, axis: horizontal ? 'y' : 'x' },
        scales: horizontal ? { x: ejeValores, y: ejeCategorias } : { x: ejeCategorias, y: ejeValores },
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: token('--color-ink', '#080708'),
            padding: 10,
            cornerRadius: 8,
            displayColors: false,
            titleFont: { family: fuente, weight: 'bold' },
            bodyFont: { family: fuente },
            callbacks: {
              label: (ctx) => {
                const v = Number(ctx.parsed[horizontal ? 'x' : 'y']);
                const total = this.valores().reduce((a, b) => a + b, 0);
                const parte = this.maximo() == null && total > 0 ? ` (${Math.round((v / total) * 100)}% del total)` : '';
                return this.formatear(v) + (this.parteDelTotal() || !horizontal ? parte : '');
              },
            },
          },
        },
      },
    });
  }
}
