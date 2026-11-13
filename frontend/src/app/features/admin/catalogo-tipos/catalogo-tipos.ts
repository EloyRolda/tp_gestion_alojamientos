import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { TipoAlojamiento } from '../../../core/models/tipo-alojamiento.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { coincide } from '../../../core/utils/busqueda.util';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';

/// SCRUM-43 / RF09. Catalogo de tipos de alojamiento: alta, edicion (renombrar
/// en el lugar), fusionar duplicados (ReemplazoCatalogoDTO: todo lo que usaba
/// idOrigen pasa a idDestino) y eliminar uno sin uso.
@Component({
  selector: 'app-catalogo-tipos',
  standalone: true,
  imports: [FormsModule, Esqueleto, BuscadorLista],
  templateUrl: './catalogo-tipos.html',
  styleUrl: './catalogo-tipos.css',
})
export class CatalogoTipos {
  private readonly catalogoService = inject(CatalogoService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly aviso = inject(AvisoService);

  readonly tipos = signal<TipoAlojamiento[]>([]);
  /// Busca por nombre (sin tildes ni mayusculas).
  readonly busqueda = signal('');
  readonly tiposFiltrados = computed(() => this.tipos().filter((t) => coincide(this.busqueda(), t.nombreTipo)));
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly nombreNuevo = signal('');
  readonly creando = signal(false);

  readonly idEnEdicion = signal<number | null>(null);
  readonly nombreEditado = signal('');
  readonly guardandoEdicion = signal(false);

  readonly idOrigen = signal<number | null>(null);
  readonly idDestino = signal<number | null>(null);
  readonly fusionando = signal(false);

  constructor() {
    this.cargar();
  }

  crear(): void {
    const nombre = this.nombreNuevo().trim();
    if (!nombre) return;

    this.error.set(null);
    this.creando.set(true);
    this.catalogoService.crearTipo(nombre).subscribe({
      next: () => {
        this.creando.set(false);
        this.nombreNuevo.set('');
        this.aviso.mostrar('Tipo creado');
        this.cargar();
      },
      error: (err) => {
        this.creando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  empezarEdicion(t: TipoAlojamiento): void {
    this.idEnEdicion.set(t.idTipoAlojamiento);
    this.nombreEditado.set(t.nombreTipo);
  }

  cancelarEdicion(): void {
    this.idEnEdicion.set(null);
  }

  guardarEdicion(id: number): void {
    const nombre = this.nombreEditado().trim();
    if (!nombre) return;

    this.error.set(null);
    this.guardandoEdicion.set(true);
    this.catalogoService.renombrarTipo(id, nombre).subscribe({
      next: () => {
        this.guardandoEdicion.set(false);
        this.idEnEdicion.set(null);
        this.aviso.mostrar('Cambios guardados');
        this.cargar();
      },
      error: (err) => {
        this.guardandoEdicion.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  async fusionar(): Promise<void> {
    const origen = this.idOrigen();
    const destino = this.idDestino();
    if (!origen || !destino || origen === destino) return;
    const nombre = (id: number) => this.tipos().find((x) => x.idTipoAlojamiento === id)?.nombreTipo ?? '';
    const ok = await this.confirmacion.pedir({
      titulo: 'Fusionar tipos',
      mensaje: [
        `Todo lo que hoy usa "${nombre(origen)}" va a pasar a "${nombre(destino)}", y "${nombre(origen)}" se elimina.`,
        'Esto no se puede deshacer.',
      ],
      aceptar: 'Fusionar',
      peligro: true,
    });
    if (!ok) return;

    this.error.set(null);
    this.fusionando.set(true);
    this.catalogoService.reemplazarTipo({ idOrigen: origen, idDestino: destino }).subscribe({
      next: () => {
        this.fusionando.set(false);
        this.idOrigen.set(null);
        this.idDestino.set(null);
        this.aviso.mostrar('Duplicados fusionados');
        this.cargar();
      },
      error: (err) => {
        this.fusionando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  async eliminar(id: number): Promise<void> {
    const nombre = this.tipos().find((x) => x.idTipoAlojamiento === id)?.nombreTipo ?? '';
    const ok = await this.confirmacion.pedir({
      titulo: `Eliminar "${nombre}"`,
      mensaje: ['Se quita del catálogo. Si algún alojamiento lo usa, el sistema no te va a dejar eliminarlo: en ese caso fusionalo con otro.'],
      aceptar: 'Eliminar',
      peligro: true,
    });
    if (!ok) return;
    this.error.set(null);
    this.catalogoService.eliminarTipo(id).subscribe({
      next: () => {
        this.aviso.mostrar('Tipo eliminado');
        this.cargar();
      },
      error: (err) => void this.confirmacion.informar({ titulo: 'No se pudo eliminar', mensaje: [extraerMensajeError(err)] }),
    });
  }

  private cargar(): void {
    this.cargando.set(true);
    this.catalogoService.listarTipos().subscribe({
      next: (t) => {
        this.tipos.set(t);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
