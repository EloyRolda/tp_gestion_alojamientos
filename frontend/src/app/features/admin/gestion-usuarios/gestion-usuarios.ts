import { NOMBRE_TIPO_USUARIO } from '../../../core/utils/tipo-usuario.util';
import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { AuthService } from '../../../core/services/auth.service';
import { TipoUsuario, Usuario } from '../../../core/models/usuario.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { coincide } from '../../../core/utils/busqueda.util';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';
import { Vacio } from '../../../shared/vacio/vacio';
import { Paginador } from '../../../shared/paginador/paginador';
import { BajaUsuarioService, PlanBaja } from '../../../core/services/baja-usuario.service';

type FiltroTipo = TipoUsuario | 'TODOS';

/// SCRUM-42 / RF06, RF07. Gestion de usuarios: listado (con filtro por tipo,
/// en el cliente -- un solo GET /Usuario/listar alcanza), activar/desactivar
/// (soft-delete, ver UsuarioService.borrarPorId en el backend: nunca borra
/// la fila), y acceso a editar datos / crear otro administrador.
@Component({
  selector: 'app-gestion-usuarios',
  standalone: true,
  imports: [DatePipe, FormsModule, RouterLink, Esqueleto, BuscadorLista, Vacio, Paginador],
  templateUrl: './gestion-usuarios.html',
  styleUrl: './gestion-usuarios.css',
})
export class GestionUsuarios {
  private readonly usuarioService = inject(UsuarioService);
  private readonly baja = inject(BajaUsuarioService);
  /// La propia cuenta del administrador no se desactiva desde aca (se quedaria
  /// afuera) y se edita desde Mi perfil (que mantiene la sesion si cambia el email).
  private readonly auth = inject(AuthService);
  readonly miId = computed(() => this.auth.usuario()?.id);

  readonly usuarios = signal<Usuario[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);
  readonly filtro = signal<FiltroTipo>('TODOS');
  /// Busca por nombre, apellido, usuario, email o telefono (sin tildes ni mayusculas).
  readonly busqueda = signal('');
  readonly cambiandoEstado = signal<number | null>(null);

  readonly tipos: FiltroTipo[] = ['TODOS', 'CLIENTE', 'ANFITRION', 'ADMIN'];
  readonly nombreTipo = NOMBRE_TIPO_USUARIO;

  readonly usuariosFiltrados = computed(() => {
    const f = this.filtro();
    const b = this.busqueda();
    return this.usuarios().filter(
      (u) =>
        (f === 'TODOS' || u.tipo === f) &&
        coincide(b, u.nombre, u.apellido, u.usuarioLogin, u.email, u.telefono),
    );
  });

  /// Paginado: el admin elige ver de a 10, 20, 30 o 50 usuarios por página.
  readonly pagina = signal(1);
  readonly porPagina = signal(10);
  readonly usuariosVisibles = computed(() => {
    const lista = this.usuariosFiltrados();
    const tam = this.porPagina();
    const ultima = Math.max(1, Math.ceil(lista.length / tam));
    const pagina = Math.min(Math.max(1, this.pagina()), ultima);
    return lista.slice((pagina - 1) * tam, pagina * tam);
  });

  constructor() {
    this.cargar();
    // Buscar o cambiar el filtro vuelve a la primera página.
    effect(() => {
      this.busqueda();
      this.filtro();
      untracked(() => this.pagina.set(1));
    });
  }

  limpiarBusqueda(): void {
    this.busqueda.set('');
    this.filtro.set('TODOS');
  }

  /// Desactivar es en cascada (ver BajaUsuarioService): se pausan los
  /// alojamientos del anfitrion y se cancelan las reservas sin pago.
  async toggleActivo(u: Usuario): Promise<void> {
    let plan: PlanBaja | null = null;
    if (u.activo) {
      this.cambiandoEstado.set(u.id);
      plan = await this.baja.confirmarBaja(u);
      if (!plan) {
        this.cambiandoEstado.set(null);
        return;
      }
    }
    this.cambiandoEstado.set(u.id);
    this.usuarioService.actualizarComoAdmin({ id: u.id, activo: !u.activo }).subscribe({
      next: async () => {
        if (plan) await this.baja.aplicarCascada(u, plan);
        else await this.baja.avisarReactivacion(u);
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
