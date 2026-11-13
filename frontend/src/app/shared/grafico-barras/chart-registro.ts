import { BarController, BarElement, CategoryScale, Chart, LinearScale, Tooltip } from 'chart.js';

/// Solo las piezas de Chart.js que usan los graficos de barras. Este archivo se
/// importa de forma dinamica desde GraficoBarras: asi Chart.js queda en un
/// archivo aparte que se descarga recien cuando hace falta, y sin las partes
/// que no se usan (lineas, tortas, leyendas...).
Chart.register(BarController, BarElement, CategoryScale, LinearScale, Tooltip);

export { Chart };
