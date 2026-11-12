import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { Alojamiento } from '../../core/models/alojamiento.model';

/// Tarjeta de alojamiento reusada por Home y Busqueda -- un solo lugar para
/// cambiar como se ve un alojamiento en un listado.
@Component({
  selector: 'app-alojamiento-card',
  standalone: true,
  imports: [RouterLink, DecimalPipe],
  templateUrl: './alojamiento-card.html',
  styleUrl: './alojamiento-card.css',
})
export class AlojamientoCard {
  readonly alojamiento = input.required<Alojamiento>();
}
