import { Component, inject } from '@angular/core';
import { AvisoService } from '../../core/services/aviso.service';

/// Contenedor fijo abajo de la pantalla donde aparecen los avisos de
/// AvisoService (SCRUM-68). Va una sola vez, en el componente raiz.
@Component({
  selector: 'app-avisos',
  standalone: true,
  template: `
    <div class="avisos" aria-live="polite">
      @for (aviso of avisos(); track aviso.id) {
        <div class="aviso" [class.aviso--sale]="aviso.saliendo" role="status">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true">
            <path d="M20 6 9 17l-5-5" />
          </svg>
          <span>{{ aviso.texto }}</span>
          <button type="button" class="aviso__cerrar" (click)="cerrar(aviso.id)" aria-label="Cerrar aviso">×</button>
        </div>
      }
    </div>
  `,
  styles: `
    .avisos {
      position: fixed;
      left: 50%;
      bottom: calc(var(--space-4) + env(safe-area-inset-bottom, 0px));
      z-index: 50;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--space-2);
      width: min(420px, calc(100% - 2 * var(--space-3)));
      transform: translateX(-50%);
      pointer-events: none;
    }
    .aviso {
      display: flex;
      align-items: center;
      gap: var(--space-2);
      width: 100%;
      padding: 6px 6px 6px var(--space-3);
      border-radius: var(--radius-sm);
      background: var(--color-ink);
      color: #ffffff;
      font-weight: 600;
      box-shadow: 0 10px 28px rgba(8, 7, 8, 0.3);
      pointer-events: auto;
      animation: aviso-entra 0.22s var(--ease);
    }
    .aviso svg {
      flex-shrink: 0;
      color: var(--color-dorado);
    }
    .aviso span {
      flex: 1;
    }
    .aviso__cerrar {
      flex-shrink: 0;
      width: var(--tap-size);
      height: var(--tap-size);
      border: 0;
      border-radius: var(--radius-sm);
      background: transparent;
      color: #d8d3cd;
      font-size: 1.3rem;
      cursor: pointer;
    }
    .aviso__cerrar:hover {
      color: #ffffff;
      background: rgba(255, 255, 255, 0.1);
    }
    .aviso__cerrar:focus-visible {
      outline-color: var(--color-dorado);
    }
    .aviso--sale {
      animation: aviso-sale 0.2s ease forwards;
    }
    @keyframes aviso-entra {
      from {
        opacity: 0;
        transform: translateY(12px);
      }
      to {
        opacity: 1;
        transform: none;
      }
    }
    @keyframes aviso-sale {
      to {
        opacity: 0;
        transform: translateY(8px);
      }
    }
  `,
})
export class Avisos {
  private readonly servicio = inject(AvisoService);
  readonly avisos = this.servicio.avisos;

  cerrar(id: number): void {
    this.servicio.cerrar(id);
  }
}
