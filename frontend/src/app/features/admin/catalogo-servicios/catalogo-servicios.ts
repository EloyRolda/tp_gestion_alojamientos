import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { Servicio } from '../../../core/models/servicio.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';

/// SCRUM-44 / RF10. Mismo criterio que SCRUM-43 (catalogo de tipos) pero para
/// el catalogo de servicios/comodidades: alta, edicion, fusionar duplicados y eliminar.
@Component({
  selector: 'app-catalogo-servicios',
  standalone: true,
  imports: [FormsModule, Esqueleto],
  templateUrl: './catalogo-servicios.html',
  styleUrl: './catalogo-servicios.css',
})
export class CatalogoServicios {
  private readonly catalogoService = inject(CatalogoService);
  private readonly aviso = inject(AvisoService);

  readonly servicios = signal<Servicio[]>([]);
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
    this.catalogoService.crearServicio(nombre).subscribe({
      next: () => {
        this.creando.set(false);
        this.nombreNuevo.set('');
        this.aviso.mostrar('Servicio creado');
        this.cargar();
      },
      error: (err) => {
        this.creando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  empezarEdicion(s: Servicio): void {
    this.idEnEdicion.set(s.idServicio);
    this.nombreEditado.set(s.nombreServicio);
  }

  cancelarEdicion(): void {
    this.idEnEdicion.set(null);
  }

  guardarEdicion(id: number): void {
    const nombre = this.nombreEditado().trim();
    if (!nombre) return;

    this.error.set(null);
    this.guardandoEdicion.set(true);
    this.catalogoService.renombrarServicio(id, nombre).subscribe({
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

  fusionar(): void {
    const origen = this.idOrigen();
    const destino = this.idDestino();
    if (!origen || !destino || origen === destino) return;

    this.error.set(null);
    this.fusionando.set(true);
    this.catalogoService.reemplazarServicio({ idOrigen: origen, idDestino: destino }).subscribe({
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

  eliminar(id: number): void {
    this.error.set(null);
    this.catalogoService.eliminarServicio(id).subscribe({
      next: () => {
        this.aviso.mostrar('Servicio eliminado');
        this.cargar();
      },
      error: (err) => this.error.set(extraerMensajeError(err)),
    });
  }

  private cargar(): void {
    this.cargando.set(true);
    this.catalogoService.listarServicios().subscribe({
      next: (s) => {
        this.servicios.set(s);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
