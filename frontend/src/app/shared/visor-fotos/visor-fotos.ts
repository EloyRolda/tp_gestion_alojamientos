import { Component, ElementRef, computed, effect, model, input, viewChild } from '@angular/core';

export interface FotoVisor {
  url: string;
  alt: string;
}

/// Distancia minima (en px) de un deslizamiento con el dedo para pasar de foto.
const UMBRAL_SWIPE = 50;

/// Visor de fotos a pantalla completa: muestra una foto por vez, con anterior y
/// siguiente (botones, flechas del teclado o deslizando con el dedo) y se cierra
/// con Escape, con la X o tocando fuera de la foto. Usa <dialog> nativo, que ya
/// atrapa el foco del teclado mientras esta abierto.
///
/// Se abre poniendo `indice` en el numero de la foto y se cierra volviendolo a null.
@Component({
  selector: 'app-visor-fotos',
  standalone: true,
  templateUrl: './visor-fotos.html',
  styleUrl: './visor-fotos.css',
})
export class VisorFotos {
  private readonly dialogo = viewChild.required<ElementRef<HTMLDialogElement>>('dialogo');

  readonly fotos = input.required<FotoVisor[]>();
  /// Foto abierta (two-way binding); null = visor cerrado.
  readonly indice = model<number | null>(null);

  readonly actual = computed(() => {
    const i = this.indice();
    return i == null ? null : (this.fotos()[i] ?? null);
  });

  private inicioToque: number | null = null;

  constructor() {
    effect(() => {
      const d = this.dialogo().nativeElement;
      const abierto = this.indice() != null;
      if (abierto && !d.open) {
        d.showModal();
      } else if (!abierto && d.open) {
        d.close();
      }
    });
  }

  anterior(): void {
    this.mover(-1);
  }

  siguiente(): void {
    this.mover(1);
  }

  cerrar(): void {
    this.indice.set(null);
  }

  /// Escape lo maneja el propio <dialog>: aca solo se sincroniza el estado.
  alCerrarDialogo(): void {
    if (this.indice() != null) {
      this.indice.set(null);
    }
  }

  teclado(evento: KeyboardEvent): void {
    if (evento.key === 'ArrowLeft') {
      evento.preventDefault();
      this.anterior();
    } else if (evento.key === 'ArrowRight') {
      evento.preventDefault();
      this.siguiente();
    }
  }

  /// Un toque en el fondo oscuro (fuera de la foto y los botones) cierra el visor.
  clicFondo(evento: MouseEvent): void {
    if (evento.target === evento.currentTarget) {
      this.cerrar();
    }
  }

  toqueInicio(evento: TouchEvent): void {
    this.inicioToque = evento.changedTouches[0]?.clientX ?? null;
  }

  toqueFin(evento: TouchEvent): void {
    if (this.inicioToque == null) return;
    const dx = (evento.changedTouches[0]?.clientX ?? this.inicioToque) - this.inicioToque;
    this.inicioToque = null;
    if (Math.abs(dx) >= UMBRAL_SWIPE) {
      this.mover(dx < 0 ? 1 : -1);
    }
  }

  /// Da la vuelta: despues de la ultima vuelve a la primera.
  private mover(paso: number): void {
    const i = this.indice();
    const total = this.fotos().length;
    if (i == null || total < 2) return;
    this.indice.set((i + paso + total) % total);
  }
}
