import { Component, input } from '@angular/core';

/// Mensaje de error debajo de un campo de formulario. Se ubica justo despues
/// del input, dentro de su .field, con id "<campo>-error" para que el input lo
/// anuncie con aria-describedby (ver validacion.util.ts).
@Component({
  selector: 'app-error-campo',
  standalone: true,
  template: `
    @if (mensaje()) {
      <p class="field__error" [id]="campo() + '-error'">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" aria-hidden="true">
          <circle cx="12" cy="12" r="9" />
          <path d="M12 7.5v5.5M12 16.5v.01" />
        </svg>
        <span>{{ mensaje() }}</span>
      </p>
    }
  `,
  styles: `
    :host {
      display: contents;
    }
    .field__error {
      display: flex;
      align-items: flex-start;
      gap: 6px;
      margin: 2px 0 0;
      color: var(--color-danger);
      font-size: 0.875rem;
      font-weight: 600;
      line-height: 1.35;
      text-align: left;
    }
    .field__error svg {
      flex: none;
      margin-top: 2px;
    }
  `,
})
export class ErrorCampo {
  readonly campo = input.required<string>();
  readonly mensaje = input<string | null | undefined>(null);
}
