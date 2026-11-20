/// Corazón de favoritos (RF41, SCRUM-77): marca y desmarca un alojamiento desde
/// la tarjeta o desde el detalle. Lo ven el cliente y el visitante sin sesión
/// (que va al login); el anfitrión y el administrador no lo ven.
import { ChangeDetectionStrategy, Component, computed, inject, input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { FavoritoService } from '../../core/services/favorito.service';

@Component({
  selector: 'app-boton-favorito',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (visible()) {
      <button
        type="button"
        class="favorito"
        [class.favorito--activo]="activo()"
        [attr.aria-pressed]="activo()"
        [attr.aria-label]="activo() ? 'Quitar de favoritos' : 'Guardar en favoritos'"
        [title]="activo() ? 'Quitar de favoritos' : 'Guardar en favoritos'"
        (click)="tocar($event)"
      >
        <svg
          viewBox="0 0 24 24"
          width="22"
          height="22"
          aria-hidden="true"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          [attr.fill]="activo() ? 'currentColor' : 'none'"
        >
          <path d="M12 20.7 4.9 13.6a4.6 4.6 0 0 1 0-6.5 4.6 4.6 0 0 1 6.5 0l.6.6.6-.6a4.6 4.6 0 0 1 6.5 0 4.6 4.6 0 0 1 0 6.5Z" />
        </svg>
      </button>
    }
  `,
  styles: `
    /* El posicionamiento (arriba de la foto en la tarjeta, al lado del título
       en el detalle) lo define quien lo usa; acá solo la pieza en sí. */
    :host {
      display: inline-flex;
      flex-shrink: 0;
      line-height: 0;
    }

    /* El enlace estirado de la tarjeta usa z-index 1: el corazón va por encima. */
    .favorito {
      position: relative;
      z-index: 2;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 44px;
      height: 44px;
      padding: 0;
      border: 0;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.94);
      color: var(--color-ink);
      cursor: pointer;
      box-shadow: 0 2px 8px rgba(8, 7, 8, 0.18);
      transition:
        transform 0.15s var(--ease),
        color 0.15s var(--ease),
        background 0.15s var(--ease);
    }

    .favorito:hover {
      transform: scale(1.06);
    }

    .favorito:active {
      transform: scale(0.95);
    }

    .favorito:focus-visible {
      outline: 3px solid var(--color-azul);
      outline-offset: 2px;
    }

    .favorito--activo {
      color: var(--color-tomate);
    }

    .favorito svg {
      display: block;
    }
  `,
})
export class BotonFavorito {
  /// Alojamiento que se marca o se desmarca.
  readonly idAlojamiento = input.required<number>();

  private readonly favorito = inject(FavoritoService);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  /// El visitante sin sesión también ve el corazón: al tocarlo va al login.
  protected readonly visible = computed(() => {
    if (!this.auth.estaLogueado()) {
      return true;
    }
    return this.auth.rol() === 'CLIENTE';
  });

  protected readonly activo = computed(() => this.favorito.ids().has(this.idAlojamiento()));

  protected tocar(evento: Event): void {
    // La tarjeta tiene un enlace estirado: sin esto, tocar el corazón abriría el detalle.
    evento.preventDefault();
    evento.stopPropagation();

    if (!this.auth.estaLogueado()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    this.favorito.alternar(this.idAlojamiento());
  }
}
