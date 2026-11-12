import { Component, DestroyRef, ElementRef, HostListener, computed, inject, signal, viewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { filter } from 'rxjs';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Kaza } from '../../shared/kaza/kaza';

/// Encabezado de la app (SCRUM-63): logo con Kaza, buscador siempre a mano y el
/// avatar con el menu de la cuenta. El menu se arma segun el rol autenticado
/// (SCRUM-21) y se cierra al navegar (incluido el boton atras del navegador), al
/// tocar afuera o con la tecla Escape.
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FormsModule, Kaza],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly host = inject(ElementRef<HTMLElement>);
  private readonly botonPerfil = viewChild<ElementRef<HTMLButtonElement>>('botonPerfil');

  readonly estaLogueado = this.auth.estaLogueado;
  readonly usuario = this.auth.usuario;
  readonly rol = this.auth.rol;
  readonly menuAbierto = signal(false);
  readonly textoBusqueda = signal('');

  readonly nombreRol = computed(() => {
    switch (this.rol()) {
      case 'CLIENTE':
        return 'Cliente';
      case 'ANFITRION':
        return 'Anfitrión';
      case 'ADMIN':
        return 'Administrador';
      default:
        return '';
    }
  });

  readonly etiquetaAvatar = computed(() =>
    this.estaLogueado() ? `Menú de ${this.usuario()?.nombre ?? 'tu cuenta'}` : 'Abrir menú de cuenta',
  );

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
    if (this.menuAbierto()) {
      this.cerrarMenu();
      this.botonPerfil()?.nativeElement.focus();
    }
  }

  @HostListener('document:click', ['$event'])
  alHacerClick(evento: MouseEvent): void {
    if (this.menuAbierto() && !this.host.nativeElement.querySelector('.perfil')?.contains(evento.target as Node)) {
      this.cerrarMenu();
    }
  }

  toggleMenu(): void {
    this.menuAbierto.update((abierto) => !abierto);
  }

  cerrarMenu(): void {
    this.menuAbierto.set(false);
  }

  buscar(): void {
    const texto = this.textoBusqueda().trim();
    this.router.navigate(['/buscar'], { queryParams: texto ? { q: texto } : {} });
  }

  cerrarSesion(): void {
    this.auth.cerrarSesion();
    this.cerrarMenu();
    this.router.navigate(['/']);
  }
}
