import { Component, input } from '@angular/core';
import { Kaza } from '../kaza/kaza';

/// Estado vacio (SCRUM-68): cuando una lista no tiene nada para mostrar,
/// aparece Kaza con un titulo, una explicacion y (opcional) un boton para
/// seguir, que quien lo usa pasa adentro de la etiqueta.
@Component({
  selector: 'app-vacio',
  standalone: true,
  imports: [Kaza],
  template: `
    <div class="vacio">
      <app-kaza variante="trazo" sobre="claro" />
      <h2>{{ titulo() }}</h2>
      @if (texto()) {
        <p class="text-muted">{{ texto() }}</p>
      }
      <ng-content />
    </div>
  `,
  styles: `
    :host {
      display: block;
      grid-column: 1 / -1;
    }
    .vacio {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--space-2);
      padding: var(--space-6) var(--space-4);
      text-align: center;
      background: var(--color-surface);
      border: 1px dashed var(--color-acero);
      border-radius: var(--radius);
      animation: vista-entra 0.3s var(--ease) both;
    }
    app-kaza {
      width: 92px;
      margin-bottom: var(--space-2);
    }
    p {
      max-width: 42ch;
    }
    .vacio > ::ng-deep .btn {
      margin-top: var(--space-2);
    }
  `,
})
export class Vacio {
  readonly titulo = input.required<string>();
  readonly texto = input<string>('');
}
