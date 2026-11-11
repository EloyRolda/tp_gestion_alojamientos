import { Component, DestroyRef, HostListener, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { filter } from 'rxjs';
import { NavigationEnd, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

/// Menu que cambia segun el rol autenticado (SCRUM-21). Por debajo de 1024px se
/// muestra como menu desplegable (SCRUM-51): se abre con el boton, y se cierra al
/// navegar (incluido el boton atras del navegador) o con la tecla Escape.
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
  readonly rol = this.auth.rol;
  readonly menuAbierto = signal(false);

  constructor() {
    this.router.events
      .pipe(
        filter((e) => e instanceof NavigationEnd),
        takeUntilDestroyed(inject(DestroyRef)),
      )
      .subscribe(() => this.cerrarMenu());
  }

  @HostListener('document:keydown.escape')
  alPresionarEscape(): void {
    this.cerrarMenu();
  }

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
