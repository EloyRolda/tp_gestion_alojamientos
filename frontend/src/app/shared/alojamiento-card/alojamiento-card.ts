import { Component, computed, input, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Alojamiento } from '../../core/models/alojamiento.model';
import { miniatura } from '../../core/utils/imagen.util';
import { Kaza } from '../kaza/kaza';

/// Tarjeta de alojamiento reusada por Inicio, Busqueda y Mis alojamientos: un
/// solo lugar para cambiar como se ve un alojamiento en un listado (SCRUM-65).
///
/// Muestra la foto principal (la primera de la galeria) en tamaño chico y con
/// carga diferida; si el alojamiento todavia no tiene fotos, aparece Kaza.
/// Toda la tarjeta lleva al detalle (el link del titulo se estira sobre la
/// tarjeta), y quien la usa puede sumarle botones abajo con [acciones].
@Component({
  selector: 'app-alojamiento-card',
  standalone: true,
  imports: [RouterLink, DecimalPipe, Kaza],
  templateUrl: './alojamiento-card.html',
  styleUrl: './alojamiento-card.css',
})
export class AlojamientoCard {
  readonly alojamiento = input.required<Alojamiento>();
  /// Etiqueta opcional arriba a la derecha de la foto (ej. "Inactivo").
  readonly aviso = input<string | null>(null);

  readonly foto = computed(() => miniatura(this.alojamiento().imagenPrincipal));
  readonly fotoCargada = signal(false);
}
