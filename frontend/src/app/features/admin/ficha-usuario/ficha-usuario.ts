import { Component, effect, inject, input, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Usuario } from '../../../core/models/usuario.model';
import { UsuarioService } from '../../../core/services/usuario.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { nombreTipoUsuario } from '../../../core/utils/tipo-usuario.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { ActividadUsuario } from '../actividad-usuario/actividad-usuario';

/// Ficha de un usuario para el admin (/admin/usuarios/:id): quien es y toda su
/// actividad (ActividadUsuario: numeros, alojamientos a su nombre y reservas,
/// con links para seguir el recorrido). Para cambiar sus datos, "Editar datos"
/// abre el formulario aparte (UsuarioForm, /admin/usuarios/:id/editar).
@Component({
  selector: 'app-ficha-usuario',
  standalone: true,
  imports: [DatePipe, RouterLink, Esqueleto, ActividadUsuario],
  template: `
    <section class="stack">
      <a routerLink="/admin/usuarios" class="text-muted">&larr; Volver a gestión de usuarios</a>

      @if (cargando()) {
        <app-esqueleto tipo="detalle" />
      } @else if (usuario(); as u) {
        <header class="ficha__encabezado">
          <div class="stack stack--tight">
            <h1>{{ u.nombre }} {{ u.apellido }}</h1>
            <p class="text-muted ficha__meta">
              <span>{{ nombreTipo(u.tipo) }}</span>
              <span>{{ u.activo ? 'Cuenta activa' : 'Cuenta desactivada' }}</span>
              @if (!u.emailVerificado) {
                <span>Email sin verificar</span>
              }
              <span>Desde {{ u.fechaRegistro | date: 'dd/MM/yyyy' }}</span>
            </p>
            <p class="ficha__contacto">{{ u.usuarioLogin }} · {{ u.email }} · {{ u.telefono }}</p>
          </div>
          <a [routerLink]="['/admin/usuarios', u.id, 'editar']" class="btn">Editar datos</a>
        </header>

        <app-actividad-usuario [usuario]="u" />
      } @else {
        <p class="text-danger">{{ error() }}</p>
      }
    </section>
  `,
  styles: `
    .ficha__encabezado {
      display: flex;
      flex-wrap: wrap;
      align-items: flex-start;
      justify-content: space-between;
      gap: var(--space-3);
    }
    .ficha__encabezado h1 {
      margin: 0;
    }
    /* "Anfitrion · Cuenta activa · Desde 27/11/2025" */
    .ficha__meta {
      display: flex;
      flex-wrap: wrap;
      gap: 0 var(--space-2);
      margin: 0;
    }
    .ficha__meta span + span::before {
      content: '·';
      margin-right: var(--space-2);
    }
    .ficha__contacto {
      margin: 0;
      overflow-wrap: anywhere;
    }
  `,
})
export class FichaUsuario {
  private readonly usuarioService = inject(UsuarioService);

  readonly id = input.required<string>();
  readonly nombreTipo = nombreTipoUsuario;
  readonly usuario = signal<Usuario | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    // Se recarga al pasar de una ficha a otra (por ejemplo, de un anfitrion a
    // uno de sus clientes): la ruta es la misma, solo cambia el id.
    effect(() => {
      const id = Number(this.id());
      if (!Number.isFinite(id)) return;
      this.cargando.set(true);
      this.error.set(null);
      this.usuarioService.mostrar(id).subscribe({
        next: (u) => {
          this.usuario.set(u);
          this.cargando.set(false);
        },
        error: (err) => {
          this.usuario.set(null);
          this.error.set(extraerMensajeError(err));
          this.cargando.set(false);
        },
      });
    });
  }
}
