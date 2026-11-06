import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

type EstadoPagoResultado = 'exito' | 'error' | 'pendiente';

/// SCRUM-32 / RF29. Pantalla de retorno de MercadoPago Checkout Pro. La URL
/// exacta y el query param "estado" los define el backend (ver
/// MercadoPagoService.crearPreferencia: back_urls apunta a
/// {frontendUrl}/pago-resultado?estado=exito|error|pendiente) -- no se puede
/// cambiar el nombre de la ruta ni del param sin tocar el backend tambien.
/// El estado real del pago lo termina fijando el webhook (procesarNotificacionPago),
/// de forma asincronica: esta pantalla es solo el feedback inmediato al volver
/// de MercadoPago, no la fuente de verdad -- por eso linkea a "ver mi reserva"
/// en vez de mostrar un estado definitivo.
@Component({
  selector: 'app-pago-resultado',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './pago-resultado.html',
  styleUrl: './pago-resultado.css',
})
export class PagoResultado {
  readonly estado = input<EstadoPagoResultado>();
}
