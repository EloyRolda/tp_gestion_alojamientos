import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { GaleriaService } from '../../../core/services/galeria.service';
import { ReviewService } from '../../../core/services/review.service';
import { AuthService } from '../../../core/services/auth.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { ServicioxAlojamiento } from '../../../core/models/servicio.model';
import { Imagen } from '../../../core/models/galeria.model';
import { Review } from '../../../core/models/review.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { VisorFotos } from '../../../shared/visor-fotos/visor-fotos';

/// SCRUM-24 / RF15, RF16, RF17, RF35. "id" se bindea solo desde la ruta
/// :id gracias a withComponentInputBinding() (app.config.ts).
///
/// Responsive (SCRUM-53): en mobile todo va en una columna, la galeria es un
/// carrusel horizontal y el precio + "Reservar" quedan fijos abajo de la
/// pantalla; desde tablet la galeria es un mosaico, y en desktop la reserva
/// pasa a una columna lateral que acompana el scroll.
@Component({
  selector: 'app-detalle-alojamiento',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink, Esqueleto, VisorFotos],
  templateUrl: './detalle-alojamiento.html',
  styleUrl: './detalle-alojamiento.css',
})
export class DetalleAlojamiento {
  readonly id = input.required<string>();

  private readonly alojamientoService = inject(AlojamientoService);
  private readonly galeriaService = inject(GaleriaService);
  private readonly reviewService = inject(ReviewService);
  private readonly router = inject(Router);
  private readonly titulo = inject(Title);
  protected readonly auth = inject(AuthService);

  readonly alojamiento = signal<Alojamiento | null>(null);
  /// Reservan los clientes (y los visitantes, que primero inician sesion). Un
  /// anfitrion o un administrador no reservan: el backend lo rechaza.
  readonly puedeReservar = computed(() => !this.auth.estaLogueado() || this.auth.rol() === 'CLIENTE');
  /// El anfitrion viendo su propia publicacion: en vez de "Reservar", "Editar".
  readonly esMio = computed(() => {
    const a = this.alojamiento();
    return !!a && this.auth.rol() === 'ANFITRION' && this.auth.usuario()?.id === a.anfitrion.id;
  });
  readonly esAdmin = computed(() => this.auth.rol() === 'ADMIN');
  readonly servicios = signal<ServicioxAlojamiento[]>([]);
  readonly imagenes = signal<Imagen[]>([]);
  /// Foto abierta en el visor a pantalla completa (null = cerrado).
  readonly fotoAbierta = signal<number | null>(null);
  readonly reviews = signal<Review[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    effect(() => {
      const idNumerico = Number(this.id());
      if (Number.isFinite(idNumerico)) {
        this.cargarTodo(idNumerico);
      }
    });
  }

  reservar(): void {
    const idAlojamiento = this.alojamiento()?.idAlojamiento;
    if (!idAlojamiento) return;

    const rutaReserva = ['/alojamientos', idAlojamiento, 'reservar'];
    if (!this.auth.estaLogueado()) {
      const returnUrl = this.router.serializeUrl(this.router.createUrlTree(rutaReserva));
      this.router.navigate(['/login'], { queryParams: { returnUrl } });
      return;
    }
    this.router.navigate(rutaReserva);
  }

  private cargarTodo(id: number): void {
    this.cargando.set(true);
    this.error.set(null);

    this.alojamientoService.obtener(id).subscribe({
      next: (a) => {
        this.alojamiento.set(a);
        // La pestaña muestra el nombre del alojamiento (mejor para compartir y para buscadores).
        this.titulo.setTitle(`${a.titulo} · AlojaME`);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });

    this.alojamientoService.listarServicios(id).subscribe({ next: (s) => this.servicios.set(s) });
    this.reviewService.listarPorAlojamiento(id).subscribe({ next: (r) => this.reviews.set(r) });

    this.galeriaService.obtenerPorAlojamiento(id).subscribe({
      next: (galeria) => {
        this.galeriaService.listarImagenes(galeria.id).subscribe({ next: (imgs) => this.imagenes.set(imgs) });
      },
      error: () => {
        /* el alojamiento puede no tener galeria creada todavia -- no es un error fatal para esta vista */
      },
    });
  }
}
