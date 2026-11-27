import { Component, HostListener, ElementRef, inject, signal, viewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { NotificacionService } from '../../core/services/notificacion.service';
import { Notificacion } from '../../core/models/notificacion.model';

/// Campana de avisos internos del encabezado.
/// Se comporta igual que el menú del avatar: se abre y cierra con Escape o
/// tocando afuera. Al abrirse carga los 30 avisos más recientes y permite
/// marcar uno o todos como leídos.
@Component({
  selector: 'app-campana',
  standalone: true,
  imports: [DatePipe],
  template: `
    <div class="campana">
      <button
        #botonCampana
        type="button"
        class="campana__boton"
        (click)="togglePanel()"
        [attr.aria-expanded]="panelAbierto()"
        aria-controls="campana-panel"
        aria-haspopup="true"
        [attr.aria-label]="etiquetaAvisos()"
      >
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
          <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
          <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
        </svg>
        @if (sinLeer() > 0) {
          <span class="campana__contador">{{ sinLeer() > 9 ? '9+' : sinLeer() }}</span>
        }
      </button>

      @if (panelAbierto()) {
        <div id="campana-panel" class="campana__panel" role="dialog" aria-label="Avisos">
          <div class="campana__cabecera">
            <h2 class="campana__titulo">Avisos</h2>
            @if (notificaciones().length) {
              <button type="button" class="campana__marcar-todas" (click)="marcarTodas()">Marcar todos como leídos</button>
            }
          </div>

          <div class="campana__lista">
            @if (cargando()) {
              <p class="campana__vacio">Cargando avisos...</p>
            } @else if (notificaciones().length === 0) {
              <p class="campana__vacio">No tenés avisos todavía.</p>
            } @else {
              @for (notificacion of notificaciones(); track notificacion.id) {
                <button
                  type="button"
                  class="campana__aviso"
                  [class.campana__aviso--no-leida]="!notificacion.leida"
                  (click)="marcarLeida(notificacion)"
                >
                  <span class="campana__texto">{{ notificacion.texto }}</span>
                  <span class="campana__fecha">{{ notificacion.fecha | date: 'dd/MM HH:mm' }}</span>
                </button>
              }
            }
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    :host {
      display: inline-block;
    }

    .campana {
      position: relative;
      display: inline-block;
    }

    .campana__boton {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 44px;
      min-width: 44px;
      min-height: 44px;
      border: 0;
      border-radius: 999px;
      background: transparent;
      color: #ffffff;
      cursor: pointer;
      position: relative;
      transition: background-color 0.15s ease;
    }

    .campana__boton:focus-visible {
      outline: 3px solid var(--color-dorado);
      outline-offset: 2px;
    }

    .campana__contador {
      position: absolute;
      top: -4px;
      right: -4px;
      min-width: 20px;
      height: 20px;
      padding: 0 4px;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 999px;
      background: var(--color-dorado);
      color: var(--color-ink);
      font-size: 0.75rem;
      font-weight: 700;
      line-height: 1;
    }

    .campana__panel {
      position: absolute;
      top: calc(100% + 12px);
      right: 0;
      z-index: 40;
      width: min(360px, calc(100vw - 2 * var(--space-3)));
      max-height: min(480px, calc(100vh - 140px));
      overflow-y: auto;
      padding: var(--space-2);
      background: var(--color-surface);
      color: var(--color-ink);
      border: 1px solid var(--color-border);
      border-radius: var(--radius);
      box-shadow: 0 14px 40px rgba(8, 7, 8, 0.2);
      transform-origin: top right;
      animation: menu-entra 0.16s var(--ease);
    }

    .campana__cabecera {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-2);
      padding-bottom: var(--space-2);
      margin-bottom: var(--space-2);
      border-bottom: 1px solid var(--color-bg-muted);
    }

    .campana__titulo {
      margin: 0;
      font-size: 1rem;
      font-weight: 700;
    }

    .campana__marcar-todas {
      border: 0;
      border-radius: var(--radius-sm);
      padding: 6px 10px;
      background: transparent;
      color: var(--color-azul-fuerte);
      font-weight: 700;
      cursor: pointer;
      min-height: var(--tap-size);
      min-width: var(--tap-size);
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }

    .campana__marcar-todas:hover {
      background: var(--color-dorado-suave);
    }

    .campana__lista {
      display: flex;
      flex-direction: column;
      gap: var(--space-1);
    }

    .campana__aviso {
      display: flex;
      flex-direction: column;
      width: 100%;
      padding: var(--space-2);
      border: 0;
      border-radius: var(--radius-sm);
      background: transparent;
      color: var(--color-ink);
      text-align: left;
      cursor: pointer;
      transition: background-color 0.12s ease;
    }

    .campana__aviso:hover {
      background: var(--color-bg);
    }

    .campana__aviso--no-leida {
      font-weight: 700;
      background: var(--color-dorado-suave);
    }

    .campana__texto {
      display: block;
      white-space: normal;
    }

    .campana__fecha {
      display: block;
      margin-top: 4px;
      color: var(--color-text-muted);
      font-size: 0.78rem;
    }

    .campana__vacio {
      margin: 0;
      padding: var(--space-3) var(--space-2);
      color: var(--color-text-muted);
      text-align: center;
    }

    @keyframes menu-entra {
      from { opacity: 0; transform: scale(0.96) translateY(-4px); }
      to   { opacity: 1; transform: none; }
    }
  `],
})
export class Campana {
  private readonly notificacionService = inject(NotificacionService);
  private readonly router = inject(Router);
  private readonly host = inject(ElementRef<HTMLElement>);
  private readonly botonCampana = viewChild<ElementRef<HTMLButtonElement>>('botonCampana');

  readonly sinLeer = this.notificacionService.sinLeer;
  readonly panelAbierto = signal(false);
  readonly cargando = signal(false);
  readonly notificaciones = signal<Notificacion[]>([]);

  readonly etiquetaAvisos = () => `Avisos, ${this.sinLeer()} sin leer`;

  togglePanel(): void {
    this.panelAbierto.update((abierto) => !abierto);
    if (this.panelAbierto()) {
      this.cargarNotificaciones();
    }
  }

  cerrarPanel(): void {
    this.panelAbierto.set(false);
  }

  @HostListener('document:keydown.escape')
  alPresionarEscape(): void {
    if (this.panelAbierto()) {
      this.cerrarPanel();
      this.botonCampana()?.nativeElement.focus();
    }
  }

  @HostListener('document:click', ['$event'])
  alHacerClick(evento: MouseEvent): void {
    if (this.panelAbierto() && !this.host.nativeElement.contains(evento.target as Node)) {
      this.cerrarPanel();
    }
  }

  private cargarNotificaciones(): void {
    this.cargando.set(true);
    this.notificacionService.listar().subscribe({
      next: (lista) => {
        this.notificaciones.set(lista);
        this.cargando.set(false);
      },
      error: () => {
        this.notificaciones.set([]);
        this.cargando.set(false);
      },
    });
  }

  marcarLeida(notificacion: Notificacion): void {
    if (!notificacion.leida) {
      // El contador baja enseguida, sin esperar a la proxima consulta periodica.
      this.notificacionService.marcarLeida(notificacion.id).subscribe({
        next: () => {
          this.notificaciones.update((lista) =>
            lista.map((n) => (n.id === notificacion.id ? { ...n, leida: true } : n)),
          );
          this.sinLeer.update((cantidad) => Math.max(0, cantidad - 1));
        },
        error: () => {},
      });
    }

    if (notificacion.enlace) {
      this.cerrarPanel();
      this.router.navigateByUrl(notificacion.enlace);
    }
  }

  marcarTodas(): void {
    if (this.notificaciones().length === 0) return;

    this.notificacionService.marcarTodas().subscribe({
      next: () => {
        this.notificaciones.update((lista) =>
          lista.map((n) => ({ ...n, leida: true })),
        );
        this.sinLeer.set(0);
      },
      error: () => {},
    });
  }
}
