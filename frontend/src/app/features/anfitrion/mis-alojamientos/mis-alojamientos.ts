import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

/// SCRUM-35 / RF11, RF13. Listado de los alojamientos propios del anfitrión,
/// con accion de activar/desactivar (RF13) via PUT /Alojamiento/actualizar.
@Component({
  selector: 'app-mis-alojamientos',
  standalone: true,
  imports: [DecimalPipe, RouterLink],
  templateUrl: './mis-alojamientos.html',
  styleUrl: './mis-alojamientos.css',
})
export class MisAlojamientos {
  private readonly alojamientoService = inject(AlojamientoService);

  readonly alojamientos = signal<Alojamiento[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly cambiandoEstado = signal<number | null>(null);

  constructor() {
    this.cargar();
  }

  toggleActivo(a: Alojamiento): void {
    this.cambiandoEstado.set(a.idAlojamiento);
    this.alojamientoService.actualizar({ idAlojamiento: a.idAlojamiento, activo: !a.activo }).subscribe({
      next: () => {
        this.cambiandoEstado.set(null);
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
