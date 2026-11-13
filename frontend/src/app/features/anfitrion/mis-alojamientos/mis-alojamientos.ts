import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { AlojamientoCard } from '../../../shared/alojamiento-card/alojamiento-card';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';
import { AvisoService } from '../../../core/services/aviso.service';
import { coincide } from '../../../core/utils/busqueda.util';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';

/// SCRUM-35 / RF11, RF13. Listado de los alojamientos propios del anfitrión,
/// con accion de activar/desactivar (RF13) via PUT /Alojamiento/actualizar.
@Component({
  selector: 'app-mis-alojamientos',
  standalone: true,
  imports: [RouterLink, AlojamientoCard, Esqueleto, Vacio, BuscadorLista],
  templateUrl: './mis-alojamientos.html',
  styleUrl: './mis-alojamientos.css',
})
export class MisAlojamientos {
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly aviso = inject(AvisoService);

  readonly alojamientos = signal<Alojamiento[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  /// Busca por titulo, ciudad o tipo (sin tildes ni mayusculas).
  readonly busqueda = signal('');
  readonly alojamientosFiltrados = computed(() =>
    this.alojamientos().filter((a) =>
      coincide(this.busqueda(), a.titulo, a.direccion.ciudad, a.direccion.calle, a.tipoAlojamiento.nombreTipo),
    ),
  );
  readonly cambiandoEstado = signal<number | null>(null);

  constructor() {
    this.cargar();
  }

  async toggleActivo(a: Alojamiento): Promise<void> {
    if (a.activo) {
      const ok = await this.confirmacion.pedir({
        titulo: `Desactivar "${a.titulo}"`,
        mensaje: [
          'Deja de aparecer en las búsquedas y nadie puede pedir reservas nuevas.',
          'Las reservas que ya tenés siguen igual. Podés volver a activarlo cuando quieras.',
        ],
        aceptar: 'Desactivar',
        peligro: true,
      });
      if (!ok) return;
    }
    this.cambiandoEstado.set(a.idAlojamiento);
    this.alojamientoService.actualizar({ idAlojamiento: a.idAlojamiento, activo: !a.activo }).subscribe({
      next: () => {
        this.cambiandoEstado.set(null);
        this.aviso.mostrar(a.activo ? 'Alojamiento desactivado' : 'Alojamiento activado');
        this.cargar();
      },
      error: (err) => {
        this.cambiandoEstado.set(null);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  private cargar(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.alojamientoService.listarPropios().subscribe({
      next: (a) => {
        this.alojamientos.set(a);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
