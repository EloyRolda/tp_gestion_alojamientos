import { nombreTipoUsuario } from '../../../core/utils/tipo-usuario.util';
import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { AuthService } from '../../../core/services/auth.service';
import { BajaUsuarioService, PlanBaja } from '../../../core/services/baja-usuario.service';
import { Usuario } from '../../../core/models/usuario.model';

interface FormUsuario {
  nombre: string;
  apellido: string;
  usuarioLogin: string;
  email: string;
  telefono: string;
  activo: boolean;
  password: string;
}

/// Formulario para editar los datos de un usuario (se llega desde su ficha,
/// FichaUsuario, con "Editar datos").
///
/// SCRUM-42 / RF06, RF07. El admin edita los datos de CUALQUIER usuario
/// (PUT /Usuario/actualizar/admin), a diferencia de Mi Perfil (Lote 2) que
/// solo deja a cada uno editar lo propio. Puede tocar "activo" ademas (el
/// propio usuario no).
@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [FormsModule, RouterLink, Esqueleto],
  templateUrl: './usuario-form.html',
  styleUrl: './usuario-form.css',
})
export class UsuarioForm {
  readonly nombreTipo = nombreTipoUsuario;
  readonly id = input.required<string>();

  private readonly usuarioService = inject(UsuarioService);
  private readonly aviso = inject(AvisoService);
  private readonly auth = inject(AuthService);
  private readonly baja = inject(BajaUsuarioService);
  /// Usuario tal como esta guardado (para saber si se desactiva o reactiva).
  readonly usuario = signal<Usuario | null>(null);
  /// Un administrador no puede desactivarse a si mismo (se quedaria afuera).
  readonly esMiCuenta = computed(() => Number(this.id()) === this.auth.usuario()?.id);

  readonly form = signal<FormUsuario | null>(null);
  readonly tipo = signal('');
  readonly cargando = signal(true);
  readonly guardando = signal(false);
  readonly error = signal<string | null>(null);
  readonly exito = signal(false);

  constructor() {
    effect(() => {
      const idNumerico = Number(this.id());
      if (!Number.isFinite(idNumerico)) return;
      this.cargando.set(true);
      this.usuarioService.mostrar(idNumerico).subscribe({
        next: (u) => {
          this.usuario.set(u);
          this.form.set({
            nombre: u.nombre,
            apellido: u.apellido,
            usuarioLogin: u.usuarioLogin,
            email: u.email,
            telefono: u.telefono,
            activo: u.activo,
            password: '',
          });
          this.tipo.set(u.tipo);
          this.cargando.set(false);
        },
        error: (err) => {
          this.error.set(extraerMensajeError(err));
          this.cargando.set(false);
        },
      });
    });
  }

  actualizarCampo<K extends keyof FormUsuario>(campo: K, valor: FormUsuario[K]): void {
    this.form.update((actual) => (actual ? { ...actual, [campo]: valor } : actual));
  }

  async guardar(): Promise<void> {
    const f = this.form();
    if (!f) return;
    const u = this.usuario();
    // Desmarcar "Cuenta activa" es una baja en cascada: se confirma antes.
    let plan: PlanBaja | null = null;
    if (u && u.activo && !f.activo) {
      plan = await this.baja.confirmarBaja(u);
      if (!plan) return;
    }
    const reactiva = !!u && !u.activo && f.activo;

    this.error.set(null);
    this.exito.set(false);
    this.guardando.set(true);

    this.usuarioService
      .actualizarComoAdmin({
        id: Number(this.id()),
        nombre: f.nombre,
        apellido: f.apellido,
        usuarioLogin: f.usuarioLogin,
        email: f.email,
        telefono: f.telefono,
        activo: f.activo,
        ...(f.password.trim() ? { password: f.password } : {}),
      })
      .subscribe({
        next: async () => {
          this.guardando.set(false);
          this.exito.set(true);
          this.actualizarCampo('password', '');
          if (plan && u) await this.baja.aplicarCascada(u, plan);
          else if (reactiva && u) await this.baja.avisarReactivacion(u);
          else this.aviso.mostrar('Cambios guardados');
          if (u) {
            const { password: _sinPassword, ...datos } = f;
            this.usuario.set({ ...u, ...datos });
          }
        },
        error: (err) => {
          this.guardando.set(false);
          this.error.set(extraerMensajeError(err));
        },
      });
  }
}
