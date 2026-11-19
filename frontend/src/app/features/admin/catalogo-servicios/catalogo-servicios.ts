import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { CatalogoService, UsoCatalogo } from '../../../core/services/catalogo.service';
import { Servicio } from '../../../core/models/servicio.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { ErroresCampo, enfocarPrimerError, erroresDelBackend, hayErrores, validar, reglas } from '../../../core/utils/validacion.util';
import { ErrorCampo } from '../../../shared/error-campo/error-campo';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { coincide } from '../../../core/utils/busqueda.util';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';
import { crearPaginado } from '../../../core/utils/paginado.util';
import { Paginador } from '../../../shared/paginador/paginador';

/// SCRUM-44 / RF10. Mismo criterio que SCRUM-43 (catalogo de tipos) pero para
/// el catalogo de servicios/comodidades: alta, edicion, fusionar duplicados y eliminar.
@Component({
  selector: 'app-catalogo-servicios',
  standalone: true,
  imports: [FormsModule, Esqueleto, BuscadorLista, ErrorCampo, Paginador],
  templateUrl: './catalogo-servicios.html',
  styleUrl: './catalogo-servicios.css',
})
export class CatalogoServicios {
  private readonly catalogoService = inject(CatalogoService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly aviso = inject(AvisoService);

  readonly servicios = signal<Servicio[]>([]);
  /// Busca por nombre (sin tildes ni mayusculas).
  readonly busqueda = signal('');
  readonly serviciosFiltrados = computed(() => this.servicios().filter((s) => coincide(this.busqueda(), s.nombreServicio)));
  /// Paginado de la lista (10/20/30/50 por pagina, SCRUM-74).
  readonly paginado = crearPaginado(this.serviciosFiltrados, () => this.busqueda());
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  /// Errores de crear, renombrar y fusionar, cada uno debajo de su campo
  /// (ver validacion.util.ts). Ej: "ya existe uno con ese nombre" va debajo del nombre.
  readonly errores = signal<ErroresCampo>({});
  private readonly reglaNombre = [reglas.obligatorio, reglas.maximoCaracteres(255)];

  readonly nombreNuevo = signal('');
  readonly creando = signal(false);

  readonly idEnEdicion = signal<number | null>(null);
  readonly nombreEditado = signal('');
  readonly guardandoEdicion = signal(false);

  readonly idOrigen = signal<number | null>(null);
  readonly idDestino = signal<number | null>(null);
  readonly fusionando = signal(false);

  /// Cuantos alojamientos usan cada entrada (GET /uso, solo admin). Se muestra
  /// en la tabla y en el formulario de fusion, para que se vea cual es el
  /// nombre correcto (el mas usado) y cuantos alojamientos se van a mover.
  readonly uso = signal<UsoCatalogo>({});
  usoDe(id: number | null): number {
    return id == null ? 0 : (this.uso()[id] ?? 0);
  }

  /// Resumen en vivo de la fusion elegida: que se elimina, que se conserva y
  /// cuantos alojamientos pasan. "alReves" avisa cuando se esta por eliminar
  /// el que mas se usa (lo mas probable es que se hayan invertido los campos).
  readonly vistaPrevia = computed(() => {
    const origen = this.idOrigen();
    const destino = this.idDestino();
    if (!origen || !destino || origen === destino) return null;
    return {
      eliminado: this.nombreDe(origen),
      conservado: this.nombreDe(destino),
      movidos: this.usoDe(origen),
      alReves: this.usoDe(origen) > this.usoDe(destino),
    };
  });

  private nombreDe(id: number): string {
    return this.servicios().find((x) => x.idServicio === id)?.nombreServicio ?? '';
  }

  /// Intercambia los dos campos (por si se eligieron al reves).
  invertir(): void {
    const origen = this.idOrigen();
    this.idOrigen.set(this.idDestino());
    this.idDestino.set(origen);
    this.errores.set({});
  }

  constructor() {
    this.cargar();
  }

  crear(): void {
    const nombre = this.nombreNuevo().trim();
    const errores = validar({ nombreNuevo: nombre }, { nombreNuevo: this.reglaNombre });
    this.errores.set(errores);
    if (hayErrores(errores)) {
      enfocarPrimerError(errores);
      return;
    }
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
        this.errores.set({ nombreNuevo: this.mensajeDeCampo(err) });
      },
    });
  }

  /// Un solo campo por formulario: el error del backend (de validacion o de
  /// negocio, ej. un nombre repetido) se muestra debajo de ese campo.
  private mensajeDeCampo(err: HttpErrorResponse): string {
    const { porCampo, general } = erroresDelBackend(err, {});
    return general ?? Object.values(porCampo)[0] ?? '';
  }

  empezarEdicion(s: Servicio): void {
    this.idEnEdicion.set(s.idServicio);
    this.nombreEditado.set(s.nombreServicio);
  }

  cancelarEdicion(): void {
    this.idEnEdicion.set(null);
    this.errores.set({});
  }

  guardarEdicion(id: number): void {
    const nombre = this.nombreEditado().trim();
    const errores = validar({ nombreEditado: nombre }, { nombreEditado: this.reglaNombre });
    this.errores.set(errores);
    if (hayErrores(errores)) {
      enfocarPrimerError(errores);
      return;
    }
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
        this.errores.set({ nombreEditado: this.mensajeDeCampo(err) });
      },
    });
  }

  async fusionar(): Promise<void> {
    const origen = this.idOrigen();
    const destino = this.idDestino();
    const errores: ErroresCampo = {};
    if (!origen) errores['origen'] = 'Elegí el que se elimina (el mal escrito o repetido).';
    if (!destino) errores['destino'] = 'Elegí el que se conserva (el nombre correcto).';
    else if (origen === destino) errores['destino'] = 'Tiene que ser distinto del que se elimina.';
    this.errores.set(errores);
    if (hayErrores(errores) || !origen || !destino) {
      enfocarPrimerError(errores);
      return;
    }
    const vista = this.vistaPrevia();
    if (!vista) return;
    const mensaje = [
      `${this.cantidad(vista.movidos)} que hoy ${vista.movidos === 1 ? 'usa' : 'usan'} "${vista.eliminado}" ${vista.movidos === 1 ? 'pasa' : 'pasan'} a "${vista.conservado}", y "${vista.eliminado}" se elimina del catálogo.`,
    ];
    if (vista.alReves) {
      mensaje.push(`Ojo: "${vista.eliminado}" es el más usado. Si el nombre correcto es ese, volvé e invertí los campos.`);
    }
    mensaje.push('Esto no se puede deshacer.');
    const ok = await this.confirmacion.pedir({
      titulo: 'Fusionar servicios',
      mensaje,
      aceptar: 'Fusionar',
      peligro: true,
    });
    if (!ok) return;

    this.error.set(null);
    this.fusionando.set(true);
    this.catalogoService.reemplazarServicio({ idOrigen: origen, idDestino: destino }).subscribe({
      next: (r) => {
        this.fusionando.set(false);
        this.idOrigen.set(null);
        this.idDestino.set(null);
        this.aviso.mostrar('Duplicados fusionados', [
          `${this.cantidad(r.alojamientosMovidos)} ${r.alojamientosMovidos === 1 ? 'pasó' : 'pasaron'} de "${r.eliminado}" a "${r.conservado}".`,
        ]);
        this.cargar();
      },
      error: (err) => {
        this.fusionando.set(false);
        this.errores.set({ destino: this.mensajeDeCampo(err) });
      },
    });
  }

  async eliminar(id: number): Promise<void> {
    const nombre = this.servicios().find((x) => x.idServicio === id)?.nombreServicio ?? '';
    const ok = await this.confirmacion.pedir({
      titulo: `Eliminar "${nombre}"`,
      mensaje: ['Se quita del catálogo. Si algún alojamiento lo usa, el sistema no te va a dejar eliminarlo: en ese caso fusionalo con otro.'],
      aceptar: 'Eliminar',
      peligro: true,
    });
    if (!ok) return;
    this.error.set(null);
    this.catalogoService.eliminarServicio(id).subscribe({
      next: () => {
        this.aviso.mostrar('Servicio eliminado');
        this.cargar();
      },
      error: (err) => void this.confirmacion.informar({ titulo: 'No se pudo eliminar', mensaje: [extraerMensajeError(err)] }),
    });
  }

  /// "1 alojamiento" / "3 alojamientos".
  cantidad(n: number): string {
    return `${n} ${n === 1 ? 'alojamiento' : 'alojamientos'}`;
  }

  private cargar(): void {
    this.cargando.set(true);
    forkJoin({ lista: this.catalogoService.listarServicios(), uso: this.catalogoService.usoServicios() }).subscribe({
      next: ({ lista, uso }) => {
        this.servicios.set(lista);
        this.uso.set(uso);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
