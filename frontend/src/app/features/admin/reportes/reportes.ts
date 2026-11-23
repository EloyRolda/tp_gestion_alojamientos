/// Pantalla «Reportes» del administrador (RF44 / SCRUM-79): lista los reportes de
/// los usuarios (pendientes primero), deja filtrar por estado y resolver cada
/// caso descartandolo, ocultando el alojamiento o desactivando la cuenta
/// reportada. Cada accion se confirma en la carta de Kaza.
import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AccionModeracion, CategoriaReporte, ReporteAdmin } from '../../../core/models/reporte.model';
import { AvisoService } from '../../../core/services/aviso.service';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';
import { ReporteService } from '../../../core/services/reporte.service';
import { MotivoSuspensionService } from '../../../core/services/motivo-suspension.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { crearPaginado } from '../../../core/utils/paginado.util';
import { firstValueFrom } from 'rxjs';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Paginador } from '../../../shared/paginador/paginador';
import { Vacio } from '../../../shared/vacio/vacio';

/// Valores del selector de estado; 'TODOS' no es un estado del backend, es "sin filtro".
type FiltroReportes = 'PENDIENTE' | 'RESUELTO' | 'TODOS';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [DatePipe, RouterLink, Esqueleto, Vacio, Paginador],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Reportes implements OnInit {
  private readonly reporteService = inject(ReporteService);
  private readonly avisos = inject(AvisoService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly motivoSuspension = inject(MotivoSuspensionService);
  private readonly usuarioService = inject(UsuarioService);
  private readonly alojamientoService = inject(AlojamientoService);

  /// Por defecto se entra a ver lo que falta resolver.
  protected readonly filtro = signal<FiltroReportes>('PENDIENTE');
  protected readonly reportes = signal<ReporteAdmin[]>([]);
  protected readonly cargando = signal(true);
  protected readonly error = signal('');
  /// Error de una accion puntual: la lista sigue visible.
  protected readonly errorAccion = signal('');
  /// Reporte cuya accion esta en curso (deshabilita sus botones).
  protected readonly resolviendo = signal<number | null>(null);

  protected readonly paginado = crearPaginado(this.reportes, () => this.filtro());

  ngOnInit(): void {
    this.cargar();
  }

  protected cambiarFiltro(valor: string): void {
    this.filtro.set(valor as FiltroReportes);
    this.cargar();
  }

  protected async resolver(reporte: ReporteAdmin, accion: AccionModeracion): Promise<void> {
    if (accion === 'DESCARTAR') {
      const confirmado = await this.pedirConfirmacion(reporte, accion);
      if (!confirmado) return;
      this.resolviendo.set(reporte.id);
      this.errorAccion.set('');
      this.reporteService.resolver(reporte.id, accion).subscribe({
        next: () => {
          this.resolviendo.set(null);
          this.avisarExito(this.mensajeExito(accion));
          this.cargar();
        },
        error: (err) => {
          this.resolviendo.set(null);
          this.errorAccion.set(mensajeDeError(err, 'No pudimos resolver el reporte.'));
        },
      });
      return;
    }

    const confirmado = await this.pedirConfirmacion(reporte, accion);
    if (!confirmado) return;

    let categoria: CategoriaReporte = 'ALOJAMIENTO';
    if (accion === 'DESACTIVAR_USUARIO') {
      try {
        const u = await firstValueFrom(this.usuarioService.mostrar(reporte.idObjetivo));
        categoria = u.tipo === 'ANFITRION' ? 'ANFITRION' : 'CLIENTE';
      } catch (err) {
        this.errorAccion.set('No pudimos determinar el tipo de la cuenta reportada.');
        return;
      }
    }

    const motivo = await this.motivoSuspension.pedir({
      titulo: accion === 'OCULTAR_ALOJAMIENTO' ? 'Ocultar alojamiento' : 'Desactivar usuario',
      texto: `Elegí el motivo por el que se va a ${accion === 'OCULTAR_ALOJAMIENTO' ? 'ocultar el alojamiento' : 'desactivar la cuenta'}.`,
      categoria,
      motivoInicial: reporte.motivo,
    });
    if (!motivo) return;

    this.resolviendo.set(reporte.id);
    this.errorAccion.set('');
    this.reporteService.resolver(reporte.id, accion, motivo.motivo, motivo.detalle).subscribe({
      next: () => {
        this.resolviendo.set(null);
        this.avisarExito(this.mensajeExito(accion));
        // Se recarga para que tambien se vean los otros reportes que cerró esta accion.
        this.cargar();
      },
      error: (err) => {
        this.resolviendo.set(null);
        this.errorAccion.set(mensajeDeError(err, 'No pudimos resolver el reporte.'));
      },
    });
  }

  /// Botón para reponer una publicación ocultada por moderación.
  protected republicar(reporte: ReporteAdmin): void {
    this.confirmacion.pedir({
      titulo: 'Volver a publicar',
      mensaje: [`¿Querés volver a publicar «${reporte.nombreObjetivo}»?`],
      aceptar: 'Volver a publicar',
      peligro: false,
    }).then((confirmado) => {
      if (!confirmado) return;
      this.errorAccion.set('');
      this.resolviendo.set(reporte.id);
      this.alojamientoService.republicar(reporte.idObjetivo).subscribe({
        next: () => {
          this.resolviendo.set(null);
          this.avisarExito('La publicación volvió a estar visible.');
          this.cargar();
        },
        error: (err) => {
          this.resolviendo.set(null);
          this.errorAccion.set(mensajeDeError(err, 'No pudimos volver a publicar.'));
        },
      });
    });
  }

  /// Etiqueta en español de la accion ya tomada (columna Estado de un resuelto).
  protected etiquetaAccion(accion: AccionModeracion | null): string {
    switch (accion) {
      case 'OCULTAR_ALOJAMIENTO':
        return 'Se ocultó el alojamiento';
      case 'DESACTIVAR_USUARIO':
        return 'Se desactivó la cuenta';
      case 'DESCARTAR':
        return 'Se descartó el reporte';
      default:
        return '';
    }
  }

  /// Un solo lugar para el aviso de exito: AvisoService.mostrar recibe un titulo
  /// y una lista de lineas, no un tipo de mensaje.
  private avisarExito(mensaje: string): void {
    this.avisos.mostrar('Reporte resuelto', [mensaje]);
  }

  private cargar(): void {
    this.cargando.set(true);
    this.error.set('');
    this.errorAccion.set('');

    this.reporteService.listarAdmin(this.filtro()).subscribe({
      next: (lista) => {
        this.reportes.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.reportes.set([]);
        this.error.set(mensajeDeError(err, 'No pudimos cargar los reportes.'));
        this.cargando.set(false);
      },
    });
  }

  private pedirConfirmacion(reporte: ReporteAdmin, accion: AccionModeracion): Promise<boolean> {
    return this.confirmacion.pedir({
      titulo: this.tituloConfirmacion(accion),
      mensaje: [this.detalleConfirmacion(reporte, accion)],
      aceptar: this.etiquetaAccion(accion),
      // Ocultar o desactivar no se deshace desde la pantalla: el boton va en rojo.
      peligro: accion !== 'DESCARTAR',
    });
  }

  private tituloConfirmacion(accion: AccionModeracion): string {
    switch (accion) {
      case 'OCULTAR_ALOJAMIENTO':
        return 'Ocultar alojamiento';
      case 'DESACTIVAR_USUARIO':
        return 'Desactivar usuario';
      default:
        return 'Descartar reporte';
    }
  }

  private detalleConfirmacion(reporte: ReporteAdmin, accion: AccionModeracion): string {
    switch (accion) {
      case 'OCULTAR_ALOJAMIENTO':
        return `«${reporte.nombreObjetivo}» va a dejar de aparecer en la búsqueda y los demás reportes pendientes sobre este alojamiento se van a cerrar con la misma acción.`;
      case 'DESACTIVAR_USUARIO':
        return `La cuenta de ${reporte.nombreObjetivo} no va a poder ingresar y los demás reportes pendientes sobre esta persona se van a cerrar con la misma acción.`;
      default:
        return 'El reporte va a quedar resuelto como descartado y el alojamiento o la persona no se modifican.';
    }
  }

  private mensajeExito(accion: AccionModeracion): string {
    switch (accion) {
      case 'OCULTAR_ALOJAMIENTO':
        return 'El alojamiento quedó oculto y el reporte, resuelto.';
      case 'DESACTIVAR_USUARIO':
        return 'La cuenta quedó desactivada y el reporte, resuelto.';
      default:
        return 'El reporte quedó descartado.';
    }
  }
}

/// Mensaje del backend si lo mando; si no, el texto generico de la pantalla.
function mensajeDeError(err: unknown, porDefecto: string): string {
  const mensaje = (err as { error?: { message?: string } })?.error?.message;
  return typeof mensaje === 'string' && mensaje.trim() ? mensaje : porDefecto;
}
