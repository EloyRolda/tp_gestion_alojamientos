import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

/// Centro de ayuda y legales (SCRUM-64): preguntas frecuentes, politica de
/// cancelacion, pagos, boton de arrepentimiento, terminos, privacidad y
/// accesibilidad. Cada seccion tiene un id para poder llegar desde el pie.
/// Los textos describen lo que la app hace de verdad (reglas de reembolso,
/// comisiones, plazo de pago), no promesas genericas.
@Component({
  selector: 'app-ayuda',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './ayuda.html',
  styleUrl: './ayuda.css',
})
export class Ayuda {
  private readonly auth = inject(AuthService);

  /// El arrepentimiento se hace cancelando la reserva desde su detalle: con
  /// sesion iniciada se va directo a "Mis reservas"; si no, primero a ingresar.
  readonly linkArrepentimiento = computed(() => (this.auth.estaLogueado() ? '/mis-reservas' : '/login'));
  readonly paramsArrepentimiento = computed(() =>
    this.auth.estaLogueado() ? {} : { returnUrl: '/mis-reservas' },
  );
}
