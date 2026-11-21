import { Component, ElementRef, computed, effect, inject, signal, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReporteService } from '../../core/services/reporte.service';
import { ReportarService } from '../../core/services/reportar.service';
import { MotivoReporte } from '../../core/models/reporte.model';
import { AvisoService } from '../../core/services/aviso.service';
import { ErrorCampo } from '../error-campo/error-campo';
import {
  ErroresCampo,
  enfocarPrimerError,
  erroresDelBackend,
  hayErrores,
  reglas,
  validar,
} from '../../core/utils/validacion.util';

/// Carta de reporte (RF44): select de motivo filtrado por lo que se reporta,
/// textarea de detalle (obligatorio con «Otro») y errores debajo de cada campo.
/// Es una sola carta montada en app.html, igual que <app-confirmacion />: la abre
/// cualquier boton "Reportar" via ReportarService.abrir().
///
/// Los ids de los campos van con el prefijo "reporte-" porque esta carta vive
/// siempre en el DOM: asi no chocan con los id de los formularios de cada
/// pantalla, que son los que usa enfocarPrimerError().
@Component({
  selector: 'app-reportar',
  standalone: true,
  imports: [FormsModule, ErrorCampo],
  template: `
    <dialog #dialogo class="carta" aria-labelledby="carta-reporte-titulo" (close)="alCerrar()" (click)="clicFondo($event)">
      <!-- @for con "track p": cada pedido nuevo vuelve a crear la carta, asi el
           formulario arranca vacio tambien entre un reporte y el siguiente. -->
      @for (p of pedidos(); track p) {
        <form class="carta__cuerpo" (submit)="$event.preventDefault(); enviar()">
          <h2 id="carta-reporte-titulo">Reportar a {{ p.nombreObjetivo }}</h2>
          <p>Elegí el motivo y contanos qué pasó.</p>

          <div class="field">
            <label for="reporte-motivo">Motivo</label>
            <select
              id="reporte-motivo"
              name="motivo"
              [ngModel]="motivo()"
              (ngModelChange)="motivo.set($event); errores.set({}); errorGeneral.set(null)"
              [attr.aria-invalid]="errores()['reporte-motivo'] || errorGeneral() ? true : null"
              [attr.aria-describedby]="errores()['reporte-motivo'] || errorGeneral() ? 'reporte-motivo-error' : null"
            >
              <option value="">Elegí un motivo</option>
              @for (m of motivosDe(p.categoria); track m.codigo) {
                <option [value]="m.codigo">{{ m.etiqueta }}</option>
              }
            </select>
            <app-error-campo campo="reporte-motivo" [mensaje]="errores()['reporte-motivo'] ?? errorGeneral() ?? ''" />
          </div>

          <div class="field">
            <label for="reporte-detalle">Detalle ({{ motivo() === 'OTRO' ? 'obligatorio con «Otro»' : 'opcional' }})</label>
            <textarea
              id="reporte-detalle"
              name="detalle"
              rows="4"
              maxlength="500"
              [ngModel]="detalle()"
              (ngModelChange)="detalle.set($event)"
              [attr.aria-invalid]="errores()['reporte-detalle'] ? true : null"
              [attr.aria-describedby]="errores()['reporte-detalle'] ? 'reporte-detalle-error' : null"
            ></textarea>
            <app-error-campo campo="reporte-detalle" [mensaje]="errores()['reporte-detalle'] ?? ''" />
          </div>

          <div class="carta__acciones">
            <button type="button" class="btn" autofocus (click)="cerrar()" [disabled]="enviando()">Cancelar</button>
            <button type="submit" class="btn btn--primario" [disabled]="enviando()">
              {{ enviando() ? 'Enviando...' : 'Enviar reporte' }}
            </button>
          </div>
        </form>
      }
    </dialog>
  `,
})
export class Reportar {
  private readonly servicio = inject(ReportarService);
  private readonly reporteService = inject(ReporteService);
  private readonly aviso = inject(AvisoService);

  /// El pedido actual como lista de 0 o 1 elementos, para el @for de la plantilla.
  protected readonly pedidos = computed(() => {
    const p = this.servicio.pedido();
    return p ? [p] : [];
  });

  protected readonly motivo = signal('');
  protected readonly detalle = signal('');
  protected readonly errores = signal<ErroresCampo>({});
  protected readonly errorGeneral = signal<string | null>(null);
  protected readonly enviando = signal(false);

  private readonly motivos = signal<MotivoReporte[]>([]);
  /// Bandera simple (no signal) para no hacer que el effect dependa de un
  /// signal que el propio effect escribe.
  private motivosPedidos = false;

  private readonly dialogo = viewChild.required<ElementRef<HTMLDialogElement>>('dialogo');
  /// El evento "close" del <dialog> llega despues (en otra vuelta del navegador).
  /// Si la carta se cerro por codigo (porque ya se envio) y enseguida se abre otro
  /// reporte, ese "close" atrasado no tiene que cerrar la carta nueva.
  private cierresPorCodigo = 0;

  constructor() {
    effect(() => {
      const d = this.dialogo().nativeElement;
      const hay = this.servicio.pedido() != null;

      if (hay && !d.open) {
        this.motivo.set('');
        this.detalle.set('');
        this.errores.set({});
        this.errorGeneral.set(null);
        this.enviando.set(false);
        d.showModal();
        this.cargarMotivos();
      }
      if (!hay && d.open) {
        this.cierresPorCodigo++;
        d.close();
      }
    });
  }

  /// Motivos que se pueden usar para ese tipo de objetivo.
  protected motivosDe(categoria: string): MotivoReporte[] {
    return this.motivos().filter((m) => (m.aplicaA as string[]).includes(categoria));
  }

  protected enviar(): void {
    const p = this.servicio.pedido();
    if (!p || this.enviando()) return;

    const detalle = this.detalle().trim();
    const reglasDetalle =
      this.motivo() === 'OTRO'
        ? [reglas.obligatorio, reglas.maximoCaracteres(500)]
        : [reglas.maximoCaracteres(500)];

    const errores = validar(
      { 'reporte-motivo': this.motivo(), 'reporte-detalle': detalle },
      { 'reporte-motivo': [reglas.obligatorio], 'reporte-detalle': reglasDetalle },
    );
    this.errores.set(errores);
    this.errorGeneral.set(null);
    if (hayErrores(errores)) {
      enfocarPrimerError(errores);
      return;
    }

    this.enviando.set(true);
    this.reporteService
      .crear({
        tipo: p.tipo,
        idObjetivo: p.idObjetivo,
        motivo: this.motivo(),
        detalle: detalle || null,
      })
      .subscribe({
        next: () => {
          this.enviando.set(false);
          this.servicio.cerrar();
          this.aviso.mostrar('¡Reporte enviado!', [
            'Lo vamos a revisar y, si corresponde, tomaremos medidas.',
          ]);
        },
        error: (err) => {
          this.enviando.set(false);
          // El nombre del campo en el DTO -> id del input de esta carta.
          const { porCampo, general } = erroresDelBackend(err, {
            motivo: 'reporte-motivo',
            detalle: 'reporte-detalle',
          });
          this.errores.set(porCampo);
          this.errorGeneral.set(general);
        },
      });
  }

  protected cerrar(): void {
    this.servicio.cerrar();
  }

  protected alCerrar(): void {
    if (this.cierresPorCodigo > 0) {
      this.cierresPorCodigo--;
      return;
    }
    this.servicio.cerrar();
  }

  protected clicFondo(evento: MouseEvent): void {
    if (evento.target === evento.currentTarget) this.cerrar();
  }

  /// El catalogo de motivos es fijo: el servicio lo cachea, pero se evita el
  /// pedido repetido si la carta se abre y se cierra varias veces.
  private cargarMotivos(): void {
    if (this.motivosPedidos) return;
    this.motivosPedidos = true;
    this.reporteService.motivos().subscribe({
      next: (lista) => this.motivos.set(lista),
      error: () => {
        this.motivosPedidos = false;
        this.errorGeneral.set('No pudimos cargar los motivos. Probá de nuevo en un rato.');
      },
    });
  }
}
