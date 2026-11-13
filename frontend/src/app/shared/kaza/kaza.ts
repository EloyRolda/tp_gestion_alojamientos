import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';

/// Kaza, la mascota de AlojaME: una carita adentro de una casa con techo.
/// Es la marca de la app, asi que se dibuja siempre desde aca (nunca copiada).
///
/// Regla de marca: Kaza va siempre de UN solo color, amarillo sobre fondos
/// oscuros y negro sobre fondos claros (input "sobre"). Tres versiones:
///   - "trazo": aro, ojos y sonrisa dibujados. Es la principal (logo del
///     encabezado y del pie, estados vacios); se usa de 50px para arriba.
///   - "maciza": cabeza llena con ojos y sonrisa calados del color del fondo.
///     Solo para tamaños chicos (menu, "Cargando"), donde el trazo se empasta.
///   - "avatar": la unica excepcion de color (cara amarilla con aro negro y
///     techo amarillo), porque queda mitad sobre el encabezado negro y mitad
///     sobre el fondo claro. El techo tiene su propia clase para animarlo.
///   - "insignia": la cara amarilla con aro, ojos, sonrisa y techo negros.
///     Es la insignia de las cartas de confirmacion y avisos: la cara ES el
///     circulo que sobresale de la esquina de la carta. Misma excepcion de
///     color que el avatar (amarillo + negro).
///
/// El tamaño lo decide quien la usa (ancho del host); el alto sale solo.
@Component({
  selector: 'app-kaza',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'kaza', 'aria-hidden': 'true' },
  styles: `
    :host {
      display: inline-block;
      line-height: 0;
      flex-shrink: 0;
    }
    svg {
      width: 100%;
      height: auto;
      overflow: visible;
    }
    /* Avatar del encabezado: el techo sube y la cara crece un poco al pasar el
       mouse por el boton del perfil o con el menu abierto. */
    .kaza__techo {
      transition: transform 0.25s var(--ease);
    }
    .kaza__cara {
      transition: transform 0.2s var(--ease);
      transform-origin: 138px 146px;
    }
    :host-context(.perfil__boton:hover) .kaza__techo,
    :host-context(.perfil__boton[aria-expanded='true']) .kaza__techo {
      transform: translateY(-10px);
    }
    :host-context(.perfil__boton:hover) .kaza__cara {
      transform: scale(1.03);
    }
  `,
  template: `
    @switch (variante()) {
      @case ('insignia') {
        <svg viewBox="0 -14 256 247" focusable="false">
          <path [attr.d]="cara" fill="var(--color-dorado)" />
          <g fill="var(--color-ink)">
            <path [attr.d]="aro" fill-rule="evenodd" />
            <path [attr.d]="ojo1" />
            <path [attr.d]="ojo2" />
            <path [attr.d]="sonrisa" />
            <path [attr.d]="techo" />
          </g>
        </svg>
      }
      @case ('avatar') {
        <svg viewBox="0 -14 256 247" focusable="false">
          <g class="kaza__cara">
            <path [attr.d]="cara" fill="var(--color-dorado)" />
            <g fill="var(--color-ink)">
              <path [attr.d]="aro" fill-rule="evenodd" />
              <path [attr.d]="ojo1" />
              <path [attr.d]="ojo2" />
              <path [attr.d]="sonrisa" />
            </g>
          </g>
          <path class="kaza__techo" [attr.d]="techo" fill="var(--color-dorado)" />
        </svg>
      }
      @case ('maciza') {
        <svg viewBox="0 0 256 233" focusable="false">
          <g [attr.fill]="color()">
            <path [attr.d]="cara" />
            <path [attr.d]="aro" fill-rule="evenodd" />
            <path [attr.d]="techo" />
          </g>
          <g [attr.fill]="hueco()">
            <path [attr.d]="ojo1" />
            <path [attr.d]="ojo2" />
            <path [attr.d]="sonrisa" />
          </g>
        </svg>
      }
      @default {
        <svg viewBox="0 0 256 233" focusable="false">
          <g [attr.fill]="color()">
            <path [attr.d]="aro" fill-rule="evenodd" />
            <path [attr.d]="ojo1" />
            <path [attr.d]="ojo2" />
            <path [attr.d]="sonrisa" />
            <path [attr.d]="techo" />
          </g>
        </svg>
      }
    }
  `,
})
export class Kaza {
  readonly variante = input<'trazo' | 'maciza' | 'avatar' | 'insignia'>('trazo');
  /// Fondo sobre el que se dibuja: define el color de Kaza.
  readonly sobre = input<'oscuro' | 'claro'>('claro');
  /// Solo para la version maciza: color de los ojos y la sonrisa (el del fondo).
  readonly fondo = input<string>('var(--color-surface)');

  protected readonly color = computed(() =>
    this.sobre() === 'oscuro' ? 'var(--kaza-sobre-oscuro)' : 'var(--kaza-sobre-claro)',
  );
  protected readonly hueco = computed(() => this.fondo());

  protected readonly cara = 'M147.46 222.26A80.46 87.48 83.14 1 1 128.24 62.48A80.46 87.48 83.14 1 1 147.46 222.26Z';
  protected readonly aro =
    'M147.05 230.33A88.28 95.86 84.23 1 1 129.29 54.67A88.28 95.86 84.23 1 1 147.05 230.33ZM147.56 214.19A72.64 79.09 82.06 1 1 127.49 70.29A72.64 79.09 82.06 1 1 147.56 214.19Z';
  protected readonly ojo1 = 'M80.13 134.86A20.51 22.96 174.63 1 1 120.96 131.02A20.51 22.96 174.63 1 1 80.13 134.86Z';
  protected readonly ojo2 = 'M136.24 114.18A18.19 21.19 174.51 1 1 172.46 110.69A18.19 21.19 174.51 1 1 136.24 114.18Z';
  protected readonly sonrisa =
    'M104.27 179.94C120.11 198.81 150.31 202.07 171.21 186.55C191.49 171.48 203.94 132.57 197.49 121.03C196.29 118.88 194.9 118.99 193.67 121.05C174.68 152.84 144.61 178.3 105.6 174.68C99.89 174.15 101.5 176.63 104.27 179.94Z';
  protected readonly techo =
    'M250.09 67.38L143.94 2.85A5.14 5.14 0 0 0 138.4 2.98L95.33 31.93L95.51 16.68A6.31 6.31 0 0 0 88.42 10.34L54.27 14.61A6.3 6.3 0 0 0 48.75 20.78L48.23 60.46A5.64 5.64 0 0 1 45.55 65.19L5.5 89.87A7.27 7.27 0 0 0 3.23 100.03L9.99 110.38A6.38 6.38 0 0 0 19.26 111.91C38.9 96.5 117.04 33.54 137.82 32.26C145.82 31.77 224.73 80.78 237.25 88.34A7.31 7.31 0 0 0 246.87 86.47L252.36 79.17A8.1 8.1 0 0 0 250.09 67.38Z';
}
