import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

/// Destino del boton "Reservar" del detalle de alojamiento (SCRUM-24). El
/// formulario real (SCRUM-29, fechas/precio en vivo/validacion de
/// solapamiento) es del Lote 2 -- esto solo prueba que el gateo por login
/// (y por rol: roleGuard(['CLIENTE','ADMIN']) en las rutas) funciona de
/// punta a punta desde ya.
@Component({
  selector: 'app-reservar-placeholder',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './reservar-placeholder.html',
  styleUrl: './reservar-placeholder.css',
})
export class ReservarPlaceholder {
  readonly id = input.required<string>();
}
