import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { TipoUsuario, Usuario } from '../../../core/models/usuario.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';

type FiltroTipo = TipoUsuario | 'TODOS';

/// SCRUM-42 / RF06, RF07. Gestion de usuarios: listado (con filtro por tipo,
/// en el cliente -- un solo GET /Usuario/listar alcanza), activar/desactivar
/// (soft-delete, ver UsuarioService.borrarPorId en el backend: nunca borra
/// la fila), y acceso a editar datos / crear otro administrador.
@Component({
  selector: 'app-gestion-usuarios',
  standalone: true,
  imports: [DatePipe, FormsModule, RouterLink, Esqueleto],
  templateUrl: './gestion-usuarios.html',
  styleUrl: './gestion-usuarios.css',
})
export class GestionUsuarios {
  private readonly usuarioService = inject(UsuarioService);

  readonly usuarios = signal<Usuario[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly filtro = signal<FiltroTipo>('TODOS');
  readonly cambiandoEstado = signal<number | null>(null);

  readonly tipos: FiltroTipo[] = ['TODOS', 'CLIENTE', 'ANFITRION', 'ADMIN'];

  readonly usuariosFiltrados = computed(() => {
    const f = this.filtro();
    return f === 'TODOS' ? this.usuarios() : this.usuarios().filter((u) => u.tipo === f);
  });

  constructor() {
    this.cargar();
  }

  toggleActivo(u: Usuario): void {
    this.cambiandoEstado.set(u.id);
    this.usuarioService.actualizarComoAdmin({ id: u.id, activo: !u.activo }).subscribe({
      next: () => {
        this.cambiandoEstado.set(null);
        this.cargar();
      },
      error: (err) => {
        this.cambiandoEstado.set(null);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  private cargar(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.usuarioService.listarTodos().subscribe({
      next: (u) => {
        this.usuarios.set(u);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
