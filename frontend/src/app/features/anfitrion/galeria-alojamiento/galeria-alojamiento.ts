import { Component, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GaleriaService } from '../../../core/services/galeria.service';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { Imagen } from '../../../core/models/galeria.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

const MAXIMO_IMAGENES = 10;

/// SCRUM-37 / RF16. Gestion de la galeria de un alojamiento propio. La Galeria
/// ya existe siempre (se crea sola al publicar el alojamiento, ver
/// AlojamientoService.crear en el backend) -- aca solo se suben/borran imagenes
/// dentro de ella. Subida real contra Cloudinary (CloudinaryService del backend).
@Component({
  selector: 'app-galeria-alojamiento',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './galeria-alojamiento.html',
  styleUrl: './galeria-alojamiento.css',
})
export class GaleriaAlojamiento {
  readonly id = input.required<string>();

  private readonly galeriaService = inject(GaleriaService);
  private readonly alojamientoService = inject(AlojamientoService);

  readonly tituloAlojamiento = signal('');
  readonly idGaleria = signal<number | null>(null);
  readonly imagenes = signal<Imagen[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly archivoElegido = signal<File | null>(null);
  readonly alt = signal('');
  readonly subiendo = signal(false);

  readonly maximoAlcanzado = () => this.imagenes().length >= MAXIMO_IMAGENES;

  constructor() {
    effect(() => {
      const idNumerico = Number(this.id());
      if (!Number.isFinite(idNumerico)) return;
      this.cargar(idNumerico);
    });
  }

  onArchivoElegido(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.archivoElegido.set(input.files?.[0] ?? null);
  }

  subir(): void {
    const archivo = this.archivoElegido();
    const idGaleria = this.idGaleria();
    if (!archivo || !idGaleria) return;

    this.error.set(null);
    this.subiendo.set(true);
    this.galeriaService.subir(idGaleria, archivo, this.alt()).subscribe({
      next: () => {
        this.subiendo.set(false);
        this.archivoElegido.set(null);
        this.alt.set('');
        this.recargarImagenes(idGaleria);
      },
      error: (err) => {
        this.subiendo.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  eliminar(imagen: Imagen): void {
    this.galeriaService.eliminarImagen(imagen.id).subscribe({
      next: () => this.recargarImagenes(imagen.idGaleria),
      error: (err) => this.error.set(extraerMensajeError(err)),
    });
  }

  private cargar(idAlojamiento: number): void {
    this.cargando.set(true);
    this.error.set(null);

    this.alojamientoService.obtener(idAlojamiento).subscribe({ next: (a) => this.tituloAlojamiento.set(a.titulo) });

    this.galeriaService.obtenerPorAlojamiento(idAlojamiento).subscribe({
      next: (g) => {
        this.idGaleria.set(g.id);
        this.recargarImagenes(g.id);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  private recargarImagenes(idGaleria: number): void {
    this.galeriaService.listarImagenes(idGaleria).subscribe({
      next: (imgs) => {
        this.imagenes.set(imgs);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
