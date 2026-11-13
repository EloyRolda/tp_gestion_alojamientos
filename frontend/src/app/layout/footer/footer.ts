import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Kaza } from '../../shared/kaza/kaza';

/// Pie de pagina (SCRUM-64): marca con Kaza, links institucionales, de ayuda y
/// legales, y los datos de quien desarrolla AlojaME.
@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink, Kaza],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer {
  private readonly auth = inject(AuthService);

  /// "Publica tu alojamiento": al anfitrion lo lleva directo al formulario; a un
  /// visitante, a crear su cuenta; a un cliente o un administrador (que no
  /// pueden publicar con esa cuenta), a la ayuda que explica como hacerlo.
  readonly linkPublicar = computed(() => {
    const rol = this.auth.rol();
    if (rol === 'ANFITRION') return '/anfitrion/alojamientos/nuevo';
    return this.auth.estaLogueado() ? '/ayuda' : '/registro';
  });
  readonly fragmentoPublicar = computed(() =>
    this.auth.estaLogueado() && this.auth.rol() !== 'ANFITRION' ? 'ayuda' : undefined,
  );

  readonly anio = new Date().getFullYear();
}
