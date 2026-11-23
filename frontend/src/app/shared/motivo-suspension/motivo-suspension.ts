/// Diálogo reutilizable para pedir motivo y detalle de suspensión/ocultamiento.
/// Lo controla `MotivoSuspensionService.pedir()`. Muestra un select con los
/// motivos de la categoría indicada y un textarea opcional para el detalle.
import { Component, ElementRef, effect, inject, signal, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MotivoReporte } from '../../core/models/reporte.model';
import { MotivoSuspensionService } from '../../core/services/motivo-suspension.service';
import { ReporteService } from '../../core/services/reporte.service';
import { ErroresCampo } from '../../core/utils/validacion.util';
import { ErrorCampo } from '../error-campo/error-campo';

@Component({
  selector: 'app-motivo-suspension',
  standalone: true,
  imports: [FormsModule, ErrorCampo],
  template: `
    <dialog #dialog class="carta">
      @if (service.pedidoActual(); as pedido) {
        <div class="carta__header">
          <h2>{{ pedido.titulo }}</h2>
          <button
            type="button"
            class="btn-cerrar"
            (click)="cancelar()"
            aria-label="Cerrar"
          >×</button>
        </div>

        <div class="carta__body">
          <p>{{ pedido.texto }}</p>

          <div class="field">
            <label for="motivo-suspension">Motivo</label>
            <select id="motivo-suspension" [(ngModel)]="motivoSeleccionado">
              <option value="" disabled>Elegí un motivo</option>
              @for (motivo of motivos(); track motivo.codigo) {
                <option [value]="motivo.codigo">{{ motivo.etiqueta }}</option>
              }
            </select>
            <app-error-campo [campo]="'motivo'" [mensaje]="errores()['motivo']" />
          </div>

          <div class="field">
            <label for="detalle-suspension">Detalle (opcional)</label>
            <textarea
              id="detalle-suspension"
              [(ngModel)]="detalle"
              rows="3"
              maxlength="500"
              placeholder="Agregá un detalle si ayuda a entender el caso"
            ></textarea>
            <app-error-campo [campo]="'detalle'" [mensaje]="errores()['detalle']" />
          </div>
        </div>

        <div class="form-actions">
          <button type="button" class="btn" (click)="cancelar()">Cancelar</button>
          <button type="button" class="btn btn--primario" (click)="confirmar()">Confirmar</button>
        </div>
      }
    </dialog>
  `,
  styles: `
    :host {
      display: contents;
    }

    dialog {
      width: min(92vw, 480px);
      padding: 1.25rem;
      border: 1px solid var(--color-borde);
      border-radius: var(--radio);
      background: var(--color-surface);
      color: var(--color-texto);
    }

    .carta__header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 1rem;
      margin-bottom: 1rem;
    }

    h2 {
      margin: 0;
      font-size: 1.25rem;
    }

    .btn-cerrar {
      background: none;
      border: none;
      font-size: 1.6rem;
      line-height: 1;
      cursor: pointer;
      color: var(--color-texto-suave);
    }

    .field {
      display: flex;
      flex-direction: column;
      margin-bottom: 1rem;
    }

    label {
      margin-bottom: 0.35rem;
      font-weight: 600;
    }

    select,
    textarea {
      padding: 0.6rem;
      border: 1px solid var(--color-borde);
      border-radius: var(--radio);
      font: inherit;
    }

    textarea {
      resize: vertical;
    }

    .form-actions {
      display: flex;
      justify-content: flex-end;
      gap: 0.75rem;
      margin-top: 1rem;
    }
  `,
})
export class MotivoSuspension {
  protected readonly service = inject(MotivoSuspensionService);
  private readonly reporteService = inject(ReporteService);

  @ViewChild('dialog') private dialog!: ElementRef<HTMLDialogElement>;

  protected readonly motivos = signal<MotivoReporte[]>([]);
  protected readonly motivoSeleccionado = signal('');
  protected readonly detalle = signal('');
  protected readonly errores = signal<ErroresCampo>({});

  constructor() {
    effect(() => {
      const pedido = this.service.pedidoActual();
      if (!pedido) {
        return;
      }

      this.motivoSeleccionado.set(pedido.motivoInicial ?? '');
      this.detalle.set('');
      this.errores.set({});
      this.motivos.set([]);

      this.reporteService.motivos().subscribe({
        next: (todos) => {
          this.motivos.set(todos.filter((motivo) => motivo.aplicaA.includes(pedido.categoria)));
          this.abrir();
        },
        // Sin catalogo igual se abre (con la lista vacia): asi el admin puede
        // cancelar y la accion que espera la respuesta no queda colgada.
        error: () => {
          this.motivos.set([]);
          this.abrir();
        },
      });
    });
  }

  private abrir(): void {
    if (!this.dialog.nativeElement.open) {
      this.dialog.nativeElement.showModal();
    }
  }

  protected cancelar(): void {
    this.dialog?.nativeElement.close();
    this.service.responder(null);
  }

  protected confirmar(): void {
    const motivo = this.motivoSeleccionado();
    const detalle = this.detalle().trim();
    const errores: ErroresCampo = {};

    if (!motivo) {
      errores['motivo'] = 'Elegí un motivo.';
    }
    if (detalle.length > 500) {
      errores['detalle'] = 'El detalle no puede superar los 500 caracteres.';
    }

    if (Object.keys(errores).length > 0) {
      this.errores.set(errores);
      return;
    }

    this.dialog?.nativeElement.close();
    this.service.responder({ motivo, detalle });
  }
}
