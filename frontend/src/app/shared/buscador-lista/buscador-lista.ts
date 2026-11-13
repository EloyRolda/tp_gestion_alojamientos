import { Component, input, model } from '@angular/core';

/// Buscador de un listado (usuarios, reservas, catalogos...): un campo con
/// lupa, boton para borrar y la cantidad de resultados. El filtrado lo hace
/// la pantalla con coincide() de busqueda.util, asi cada una elige por que
/// datos se busca.
@Component({
  selector: 'app-buscador-lista',
  standalone: true,
  template: `
    <div class="buscador-lista" role="search">
      <label [for]="idCampo()" class="visually-hidden">{{ etiqueta() }}</label>
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
        <circle cx="11" cy="11" r="7"></circle>
        <line x1="16.5" y1="16.5" x2="21" y2="21"></line>
      </svg>
      <input
        [id]="idCampo()"
        type="search"
        autocomplete="off"
        enterkeyhint="search"
        [placeholder]="placeholder()"
        [value]="texto()"
        (input)="texto.set($any($event.target).value)"
        (keydown.escape)="texto.set('')"
      />
      @if (texto()) {
        <button type="button" class="buscador-lista__borrar" (click)="texto.set('')" aria-label="Borrar búsqueda">×</button>
      }
    </div>
    @if (texto() && total() !== null) {
      <p class="buscador-lista__cuenta text-muted" aria-live="polite">
        {{ resultados() }} de {{ total() }} {{ total() === 1 ? 'resultado' : 'resultados' }}
      </p>
    }
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-1);
      width: 100%;
    }
    .buscador-lista {
      display: flex;
      align-items: center;
      gap: var(--space-2);
      min-height: var(--tap-size);
      padding: 0 0 0 14px;
      border: 1px solid var(--color-acero);
      border-radius: 999px;
      background: var(--color-surface);
      color: var(--color-text-muted);
      transition:
        border-color 0.15s ease,
        box-shadow 0.15s ease;
    }
    .buscador-lista:focus-within {
      border-color: var(--color-azul);
      box-shadow: 0 0 0 3px rgba(63, 136, 197, 0.25);
    }
    svg {
      flex-shrink: 0;
    }
    input {
      flex: 1;
      min-width: 0;
      min-height: var(--tap-size);
      border: 0;
      background: transparent;
      color: var(--color-ink);
      outline: none;
    }
    input::-webkit-search-cancel-button {
      display: none;
    }
    .buscador-lista__borrar {
      flex-shrink: 0;
      width: var(--tap-size);
      height: var(--tap-size);
      border: 0;
      border-radius: 50%;
      background: transparent;
      color: var(--color-ink);
      font-size: 1.2rem;
      line-height: 1;
      cursor: pointer;
    }
    .buscador-lista__borrar:hover {
      background: var(--color-bg-muted);
    }
    .buscador-lista__cuenta {
      padding-left: var(--space-3);
      font-size: 0.9rem;
    }
    @media (min-width: 768px) {
      :host {
        max-width: 420px;
      }
    }
  `,
})
export class BuscadorLista {
  readonly texto = model('');
  readonly placeholder = input('Buscar…');
  readonly etiqueta = input('Buscar en la lista');
  readonly idCampo = input('buscador-lista');
  /// Para mostrar "3 de 120 resultados" mientras hay texto escrito.
  readonly resultados = input<number>(0);
  readonly total = input<number | null>(null);
}
