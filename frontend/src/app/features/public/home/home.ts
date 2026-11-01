import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { AlojamientoCard } from '../../../shared/alojamiento-card/alojamiento-card';

const CANTIDAD_DESTACADOS = 8;

/// SCRUM-22 / RF14. Vista publica: listado inicial de alojamientos disponibles.
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, AlojamientoCard],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private readonly alojamientoService = inject(AlojamientoService);

  readonly destacados = signal<Alojamiento[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    this.alojamientoService.listar().subscribe({
      next: (lista) => {
        this.destacados.set(lista.slice(0, CANTIDAD_DESTACADOS));
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
