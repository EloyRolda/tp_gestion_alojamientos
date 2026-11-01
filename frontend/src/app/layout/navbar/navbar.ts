import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

/// Menu que cambia segun el rol autenticado (SCRUM-21). El toggle del menu
/// mobile es un signal booleano -- sin ninguna libreria, basta con eso.
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly estaLogueado = this.auth.estaLogueado;
  readonly usuario = this.auth.usuario;
  readonly menuAbierto = signal(false);

  toggleMenu(): void {
    this.menuAbierto.update((abierto) => !abierto);
  }

  cerrarMenu(): void {
    this.menuAbierto.set(false);
  }

  cerrarSesion(): void {
    this.auth.cerrarSesion();
    this.cerrarMenu();
    this.router.navigate(['/']);
  }
}
