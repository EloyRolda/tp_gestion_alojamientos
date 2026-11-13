import { Component, ElementRef, computed, effect, inject, viewChild } from '@angular/core';
import { ConfirmacionService } from '../../core/services/confirmacion.service';
import { Kaza } from '../kaza/kaza';

/// Tarjeta de confirmaciones y avisos de toda la app (ver ConfirmacionService):
/// la carta en el centro de la pantalla, con la cara de Kaza (amarilla, rasgos
/// negros) como insignia sobre la esquina superior derecha, el texto y los
/// botones abajo.
///
/// Entrada en dos tiempos: primero la carta y, con un pequeño desfase, Kaza
/// "salta" a su lugar; despues se balancea suave, girando sobre el centro de
/// su cara. Se ve igual aunque el sistema pida "reducir movimiento" (decision de diseño).
///
/// Los estilos de la carta (.carta, .carta__cuerpo, .carta__kaza...) estan en
/// styles.css: los reusa tambien el selector de servicios de la busqueda.
///
/// Usa <dialog> nativo: atrapa el foco y se cierra con Escape (= "no"). En las
/// confirmaciones el foco arranca en "Volver", asi un Enter apurado nunca
/// ejecuta la accion peligrosa.
@Component({
  selector: 'app-confirmacion',
  standalone: true,
  imports: [Kaza],
  template: `
    <dialog #dialogo class="carta" aria-labelledby="carta-titulo" (close)="alCerrar()" (click)="clicFondo($event)">
      <!-- @for con "track p": cada pedido nuevo vuelve a crear la carta, asi la
           entrada se repite tambien entre una confirmacion y su resultado. -->
      @for (p of pedidos(); track p) {
        <div class="carta__cuerpo">
          <span class="carta__kaza" aria-hidden="true">
            <app-kaza variante="insignia" />
          </span>
          <h2 id="carta-titulo">{{ p.titulo }}</h2>
          @for (parrafo of p.mensaje; track $index) {
            <p>{{ parrafo }}</p>
          }
          @if (p.nota) {
            <p class="carta__nota">{{ p.nota }}</p>
          }
          <div class="carta__acciones">
            @if (p.conCancelar) {
              <button type="button" class="btn" autofocus (click)="p.responder(false)">{{ p.cancelar ?? 'Volver' }}</button>
            }
            <button
              type="button"
              class="btn"
              [class.btn--peligro]="p.peligro"
              [class.btn--primario]="!p.peligro"
              [attr.autofocus]="p.conCancelar ? null : ''"
              (click)="p.responder(true)"
            >
              {{ p.aceptar }}
            </button>
          </div>
        </div>
      }
    </dialog>
  `,
})
export class Confirmacion {
  protected readonly servicio = inject(ConfirmacionService);
  /// El pedido actual como lista de 0 o 1 elementos, para el @for de la plantilla.
  protected readonly pedidos = computed(() => {
    const p = this.servicio.pedido();
    return p ? [p] : [];
  });
  private readonly dialogo = viewChild.required<ElementRef<HTMLDialogElement>>('dialogo');
  /// El evento "close" del <dialog> llega despues (en otra vuelta del navegador).
  /// Si la carta se cerro por codigo (porque ya se respondio) y enseguida se
  /// abrio otra (por ejemplo, confirmar y despues mostrar el resultado), ese
  /// "close" atrasado no tiene que contestar -y cerrar- la carta nueva.
  private cierresPorCodigo = 0;

  constructor() {
    effect(() => {
      const d = this.dialogo().nativeElement;
      const hay = this.servicio.pedido() != null;
      if (hay && !d.open) d.showModal();
      if (!hay && d.open) {
        this.cierresPorCodigo++;
        d.close();
      }
    });
  }

  /// Escape o cierre por fuera: en una confirmacion cuenta como "no"; en un
  /// aviso, como "Aceptar".
  alCerrar(): void {
    if (this.cierresPorCodigo > 0) {
      this.cierresPorCodigo--;
      return;
    }
    const p = this.servicio.pedido();
    p?.responder(!p.conCancelar);
  }

  clicFondo(evento: MouseEvent): void {
    if (evento.target === evento.currentTarget) this.alCerrar();
  }
}
