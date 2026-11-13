import { Component, inject, input } from '@angular/core';
import { ConfirmacionService } from '../../core/services/confirmacion.service';

/// Boton "i" al lado del titulo de un grafico: al tocarlo, Kaza explica en la
/// carta de avisos que muestra el grafico y como se calcula. Va dentro de un
/// .titulo-info (styles.css), junto al <h2> de la seccion.
@Component({
  selector: 'app-info-grafico',
  standalone: true,
  template: `
    <button type="button" class="info-grafico" [attr.aria-label]="'Qué muestra: ' + titulo()" (click)="explicar()">
      <span aria-hidden="true">i</span>
    </button>
  `,
  styles: `
    /* Area tactil de 44px; el circulo visible es mas chico y sobrio. */
    .info-grafico {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: var(--tap-size);
      height: var(--tap-size);
      padding: 0;
      border: 0;
      background: transparent;
      cursor: pointer;
    }
    span {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 24px;
      height: 24px;
      border: 1.5px solid var(--color-acero);
      border-radius: 50%;
      color: var(--color-text-muted);
      font-family: Georgia, 'Times New Roman', serif;
      font-size: 0.85rem;
      font-style: italic;
      font-weight: 700;
      line-height: 1;
      transition:
        background-color 0.15s ease,
        border-color 0.15s ease,
        color 0.15s ease;
    }
    .info-grafico:hover span,
    .info-grafico:focus-visible span {
      border-color: var(--color-ink);
      background: var(--color-dorado);
      color: var(--color-ink);
    }
  `,
})
export class InfoGrafico {
  private readonly confirmacion = inject(ConfirmacionService);

  readonly titulo = input.required<string>();
  /// Uno o mas parrafos cortos.
  readonly texto = input.required<string[]>();

  explicar(): void {
    void this.confirmacion.informar({ titulo: this.titulo(), mensaje: this.texto(), aceptar: 'Entendido' });
  }
}
