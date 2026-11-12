import { Component, computed, input } from '@angular/core';
import { Kaza } from '../kaza/kaza';

type TipoEsqueleto = 'tarjetas' | 'tabla' | 'kpis' | 'panel' | 'detalle' | 'formulario';

const CANTIDAD_POR_DEFECTO: Record<TipoEsqueleto, number> = {
  tarjetas: 6,
  tabla: 5,
  kpis: 3,
  panel: 4,
  detalle: 4,
  formulario: 5,
};

/// Esqueleto de carga (SCRUM-68): mientras llegan los datos, cada pantalla
/// muestra la forma de lo que va a aparecer (tarjetas, filas de una tabla,
/// metricas o un formulario) con un brillo suave, en vez de un texto suelto.
/// Arriba va Kaza con "Cargando…" para quien usa lector de pantalla.
@Component({
  selector: 'app-esqueleto',
  standalone: true,
  imports: [Kaza],
  templateUrl: './esqueleto.html',
  styleUrl: './esqueleto.css',
})
export class Esqueleto {
  readonly tipo = input<TipoEsqueleto>('tarjetas');
  readonly cantidad = input<number | null>(null);

  readonly items = computed(() =>
    Array.from({ length: this.cantidad() ?? CANTIDAD_POR_DEFECTO[this.tipo()] }, (_, i) => i),
  );
}
