import { Component, computed, input, model } from '@angular/core';

/// Paginado de un listado largo: "Mostrar [10/20/30/50] por página", el rango
/// que se ve ("1–10 de 87") y los botones de página anterior y siguiente.
/// La pantalla corta su lista con desdeIndice()/hastaIndice() (o con su propio
/// slice); este componente solo maneja la pagina y el tamaño elegidos.
/// Mismo diseño que el paginado de la búsqueda de alojamientos.
@Component({
  selector: 'app-paginador',
  standalone: true,
  template: `
    @if (hayQuePaginar()) {
    <div class="paginador-barra" [class.paginador-barra--pie]="soloFlechas()">
      @if (!soloFlechas()) {
      <div class="paginador-barra__tamanio">
        <label [for]="idSelector()">Mostrar</label>
        <select [id]="idSelector()" [value]="porPagina()" (change)="cambiarTamanio($any($event.target).value)">
          @for (o of opciones(); track o) {
            <option [value]="o" [selected]="o === porPagina()">{{ o }}</option>
          }
        </select>
        <span>por página</span>
      </div>

      <p class="paginador-barra__rango" aria-live="polite">
        {{ desde() }}–{{ hasta() }} de {{ total() }} {{ total() === 1 ? nombreSingular() : nombrePlural() }}
      </p>
      }

      @if (totalPaginas() > 1) {
        <nav class="paginador" [attr.aria-label]="'Páginas de ' + nombrePlural()">
          <button type="button" class="paginador__btn" [disabled]="paginaActual() === 1" (click)="irA(paginaActual() - 1)" aria-label="Página anterior">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" aria-hidden="true"><path d="m15 6-6 6 6 6" /></svg>
          </button>
          <span class="paginador__texto">Página {{ paginaActual() }} de {{ totalPaginas() }}</span>
          <button type="button" class="paginador__btn" [disabled]="paginaActual() === totalPaginas()" (click)="irA(paginaActual() + 1)" aria-label="Página siguiente">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" aria-hidden="true"><path d="m9 6 6 6-6 6" /></svg>
          </button>
        </nav>
      }
    </div>
    }
  `,
  styles: `
    .paginador-barra {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-2) var(--space-3);
      scroll-margin-top: var(--space-4);
    }
    .paginador-barra--pie {
      justify-content: center;
    }
    .paginador-barra__tamanio {
      display: inline-flex;
      align-items: center;
      gap: var(--space-2);
      font-size: 0.9rem;
    }
    .paginador-barra__tamanio label,
    .paginador-barra__tamanio span {
      color: var(--color-text-muted);
      font-weight: 600;
    }
    .paginador-barra__tamanio select {
      width: auto;
      min-width: 84px;
      min-height: var(--tap-size);
      padding: 0 10px;
      border: 1px solid var(--color-acero);
      border-radius: var(--radius-sm);
      background: var(--color-surface);
      cursor: pointer;
    }
    .paginador-barra__tamanio select:focus-visible {
      outline: none;
      border-color: var(--color-azul);
      box-shadow: 0 0 0 3px rgba(63, 136, 197, 0.25);
    }
    .paginador-barra__rango {
      margin: 0;
      color: var(--color-text-muted);
      font-size: 0.9rem;
      font-variant-numeric: tabular-nums;
    }
    .paginador {
      display: inline-flex;
      align-items: center;
      gap: var(--space-1);
    }
    .paginador__btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: var(--tap-size);
      height: var(--tap-size);
      border: 1px solid var(--color-acero);
      border-radius: 50%;
      background: var(--color-surface);
      color: var(--color-ink);
      cursor: pointer;
      transition: border-color 0.15s ease;
    }
    .paginador__btn:hover:not(:disabled) {
      border-color: var(--color-ink);
    }
    .paginador__btn:disabled {
      opacity: 0.4;
      cursor: default;
    }
    .paginador__texto {
      min-width: 8.5em;
      text-align: center;
      font-size: 0.9rem;
      font-weight: 600;
      font-variant-numeric: tabular-nums;
    }
    @media (max-width: 767.98px) {
      .paginador-barra__rango {
        order: -1;
        width: 100%;
      }
    }
  `,
})
export class Paginador {
  /// Cantidad total de elementos (ya filtrados).
  readonly total = input.required<number>();
  readonly pagina = model(1);
  readonly porPagina = model(10);
  readonly opciones = input<number[]>([10, 20, 30, 50]);
  readonly idSelector = input('paginador-tamanio');
  readonly nombreSingular = input('resultado');
  readonly nombrePlural = input('resultados');
  /// Version para el pie de la lista: solo anterior / siguiente, centrados.
  readonly soloFlechas = input(false);

  /// Si la lista entra entera con el tamaño mas chico no hay nada que elegir:
  /// el paginado no se muestra (ej. un cliente con 3 reservas).
  readonly hayQuePaginar = computed(() => this.total() > Math.min(...this.opciones()));
  readonly totalPaginas = computed(() => Math.max(1, Math.ceil(this.total() / this.porPagina())));
  readonly paginaActual = computed(() => Math.min(Math.max(1, this.pagina()), this.totalPaginas()));
  readonly desde = computed(() => (this.total() === 0 ? 0 : (this.paginaActual() - 1) * this.porPagina() + 1));
  readonly hasta = computed(() => Math.min(this.paginaActual() * this.porPagina(), this.total()));

  irA(n: number): void {
    this.pagina.set(Math.min(Math.max(1, n), this.totalPaginas()));
    // El de pie de lista vuelve al principio de SU lista (al paginador de arriba,
    // que tiene el selector con este mismo id), para leer la pagina nueva desde
    // el principio. Si no lo encuentra, vuelve al principio de la pantalla.
    if (this.soloFlechas()) {
      const suave = !window.matchMedia('(prefers-reduced-motion: reduce)').matches;
      const arriba = document.getElementById(this.idSelector())?.closest('.paginador-barra');
      if (arriba) {
        arriba.scrollIntoView({ block: 'start', behavior: suave ? 'smooth' : 'auto' });
      } else {
        window.scrollTo({ top: 0, behavior: suave ? 'smooth' : 'auto' });
      }
    }
  }

  /// Al cambiar el tamaño se vuelve a la primera página.
  cambiarTamanio(valor: string): void {
    this.porPagina.set(Number(valor));
    this.pagina.set(1);
  }
}
