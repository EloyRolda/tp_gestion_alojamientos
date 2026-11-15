import { Component, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GaleriaService } from '../../../core/services/galeria.service';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { Imagen } from '../../../core/models/galeria.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { ErroresCampo, enfocarPrimerError, hayErrores, validar, reglas } from '../../../core/utils/validacion.util';
import { ErrorCampo } from '../../../shared/error-campo/error-campo';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { VisorFotos } from '../../../shared/visor-fotos/visor-fotos';
import { ConfirmacionService } from '../../../core/services/confirmacion.service';
import { exigirDuenio } from '../../../core/utils/duenio.util';

const MAXIMO_IMAGENES = 10;

/// SCRUM-37 / RF16. Gestion de la galeria de un alojamiento propio. La Galeria
/// ya existe siempre (se crea sola al publicar el alojamiento, ver
/// AlojamientoService.crear en el backend) -- aca solo se suben/borran imagenes
/// dentro de ella. Subida real contra Cloudinary (CloudinaryService del backend).
/// Mismo limite que spring.servlet.multipart.max-file-size en el backend.
const MAXIMO_BYTES = 10 * 1024 * 1024;

@Component({
  selector: 'app-galeria-alojamiento',
  standalone: true,
  imports: [FormsModule, RouterLink, Esqueleto, VisorFotos, ErrorCampo],
  templateUrl: './galeria-alojamiento.html',
  styleUrl: './galeria-alojamiento.css',
})
export class GaleriaAlojamiento {
  readonly id = input.required<string>();

  private readonly galeriaService = inject(GaleriaService);
  private readonly confirmacion = inject(ConfirmacionService);
  private readonly aviso = inject(AvisoService);
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly idDuenio = signal<number | null>(null);

  readonly tituloAlojamiento = signal('');
  readonly idGaleria = signal<number | null>(null);
  readonly imagenes = signal<Imagen[]>([]);
  /// Foto abierta en el visor a pantalla completa (null = cerrado).
  readonly fotoAbierta = signal<number | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly archivoElegido = signal<File | null>(null);
  readonly alt = signal('');
  /// Errores de la foto a subir, debajo de cada campo (ver validacion.util.ts).
  readonly errores = signal<ErroresCampo>({});
  readonly subiendo = signal(false);

  readonly maximoAlcanzado = () => this.imagenes().length >= MAXIMO_IMAGENES;

  constructor() {
    exigirDuenio(this.idDuenio);
    effect(() => {
      const idNumerico = Number(this.id());
      if (!Number.isFinite(idNumerico)) return;
      this.cargar(idNumerico);
    });
  }

  onArchivoElegido(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.archivoElegido.set(input.files?.[0] ?? null);
    this.errores.set({});
  }

  subir(): void {
    const archivo = this.archivoElegido();
    const idGaleria = this.idGaleria();
    const errores = validar({ alt: this.alt() }, { alt: [reglas.obligatorio, reglas.maximoCaracteres(255)] });
    if (!archivo) errores['archivo'] = 'Elegí una imagen.';
    else if (!archivo.type.startsWith('image/')) errores['archivo'] = 'El archivo tiene que ser una imagen (JPG, PNG o WebP).';
    else if (archivo.size > MAXIMO_BYTES) errores['archivo'] = 'La imagen pesa más de 10 MB. Elegí una más liviana.';
    this.errores.set(errores);
    if (hayErrores(errores) || !archivo || !idGaleria) {
      enfocarPrimerError(errores);
      return;
    }

    this.error.set(null);
    this.subiendo.set(true);
    this.galeriaService.subir(idGaleria, archivo, this.alt()).subscribe({
      next: () => {
        this.subiendo.set(false);
        this.archivoElegido.set(null);
        this.alt.set('');
        this.aviso.mostrar('Foto subida');
        this.recargarImagenes(idGaleria);
      },
      error: (err) => {
        this.subiendo.set(false);
        // Un error al subir (formato, tamaño, Cloudinary) es de la foto: va debajo del archivo.
        this.errores.set({ archivo: extraerMensajeError(err) });
      },
    });
  }

  async eliminar(imagen: Imagen): Promise<void> {
    const ok = await this.confirmacion.pedir({
      titulo: 'Eliminar foto',
      mensaje: ['La foto se borra de la galería y deja de verse en la publicación.'],
      aceptar: 'Eliminar foto',
      peligro: true,
    });
    if (!ok) return;
    this.galeriaService.eliminarImagen(imagen.id).subscribe({
      next: () => {
        this.aviso.mostrar('Foto eliminada');
        this.recargarImagenes(imagen.idGaleria);
      },
      error: (err) => this.error.set(extraerMensajeError(err)),
    });
  }

  private cargar(idAlojamiento: number): void {
    this.cargando.set(true);
    this.error.set(null);

    this.alojamientoService.obtener(idAlojamiento).subscribe({
      next: (a) => {
        this.tituloAlojamiento.set(a.titulo);
        this.idDuenio.set(a.anfitrion.id);
      },
    });

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
