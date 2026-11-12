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

  /// "Publica tu alojamiento" lleva directo al formulario si ya es anfitrion;
  /// si no, a crear una cuenta.
  readonly linkPublicar = computed(() => {
    const rol = this.auth.rol();
    return rol === 'ANFITRION' || rol === 'ADMIN' ? '/anfitrion/alojamientos/nuevo' : '/registro';
  });

  readonly anio = new Date().getFullYear();
}
