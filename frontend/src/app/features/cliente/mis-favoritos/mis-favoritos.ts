/// Pantalla «Mis favoritos» (RF41, SCRUM-77): los alojamientos que el cliente
/// guardó con el corazón, del más reciente al más viejo, en la misma grilla de
/// tarjetas que la búsqueda y con paginado.
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FavoritoService } from '../../../core/services/favorito.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { crearPaginado } from '../../../core/utils/paginado.util';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { AlojamientoCard } from '../../../shared/alojamiento-card/alojamiento-card';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';
import { Paginador } from '../../../shared/paginador/paginador';

@Component({
  selector: 'app-mis-favoritos',
  standalone: true,
  imports: [RouterLink, AlojamientoCard, Esqueleto, Vacio, Paginador],
  templateUrl: './mis-favoritos.html',
  styleUrl: './mis-favoritos.css',
})
export class MisFavoritos {
  private readonly favoritoService = inject(FavoritoService);

  /// Lo que devolvió el backend al entrar a la pantalla.
  readonly favoritos = signal<Alojamiento[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  /// Al desmarcar, el corazón actualiza FavoritoService.ids(): la lista se
  /// filtra sola y el alojamiento desaparece sin recargar la página.
  readonly visibles = computed(() => {
    const ids = this.favoritoService.ids();
    return this.favoritos().filter((a) => ids.has(a.idAlojamiento));
  });

  /// 12 por página, como la grilla de la búsqueda.
  readonly paginado = crearPaginado(this.visibles, undefined, 12);

  constructor() {
    this.cargar();
  }

  private cargar(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.favoritoService.listarMios().subscribe({
      next: (lista) => {
        this.favoritos.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
