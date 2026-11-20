/// Servicio de favoritos (RF41, SCRUM-77). Guarda en memoria los ids de los
/// alojamientos que el cliente marco con el corazon y expone las operaciones
/// para listarlos, marcarlos y desmarcarlos con actualizacion optimista.
import { Injectable, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Alojamiento } from '../models/alojamiento.model';
import { AuthService } from './auth.service';
import { AvisoService } from './aviso.service';

@Injectable({ providedIn: 'root' })
export class FavoritoService {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);
  private readonly aviso = inject(AvisoService);

  /// Ids de los alojamientos favoritos del cliente logueado.
  readonly ids = signal<Set<number>>(new Set());

  constructor() {
    // El effect corre despues del constructor: nunca se dispara un pedido HTTP
    // mientras se construye un servicio root (evita NG0200). El microtask
    // aleja todavia mas el pedido del arranque.
    effect(() => {
      const rol = this.auth.rol();
      if (rol === 'CLIENTE') {
        queueMicrotask(() => this.cargarIds());
      } else {
        this.ids.set(new Set());
      }
    });
  }

  esFavorito(idAlojamiento: number): boolean {
    return this.ids().has(idAlojamiento);
  }

  /// Los alojamientos favoritos completos, del mas reciente al mas viejo.
  listarMios(): Observable<Alojamiento[]> {
    return this.http.get<Alojamiento[]>(`${API_BASE_URL}/Favorito/mios`);
  }

  /// Marca o desmarca al instante; si el pedido falla, vuelve al estado previo.
  alternar(idAlojamiento: number): void {
    const eraFavorito = this.esFavorito(idAlojamiento);
    this.aplicar(idAlojamiento, !eraFavorito);

    const pedido$ = eraFavorito
      ? this.http.delete<void>(`${API_BASE_URL}/Favorito/${idAlojamiento}`)
      : this.http.post<void>(`${API_BASE_URL}/Favorito/${idAlojamiento}`, null);

    pedido$.subscribe({
      error: () => {
        this.aplicar(idAlojamiento, eraFavorito);
        this.aviso.mostrar('No pudimos actualizar tus favoritos', ['Probá de nuevo en un momento.']);
      },
    });
  }

  private aplicar(idAlojamiento: number, favorito: boolean): void {
    this.ids.update((actuales) => {
      const nuevo = new Set(actuales);
      if (favorito) {
        nuevo.add(idAlojamiento);
      } else {
        nuevo.delete(idAlojamiento);
      }
      return nuevo;
    });
  }

  private cargarIds(): void {
    this.http.get<number[]>(`${API_BASE_URL}/Favorito/ids`).subscribe({
      next: (ids) => this.ids.set(new Set(ids)),
      error: () => this.ids.set(new Set()),
    });
  }
}
