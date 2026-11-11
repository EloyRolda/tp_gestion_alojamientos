import { Component, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

interface FormUsuario {
  nombre: string;
  apellido: string;
  usuarioLogin: string;
  email: string;
  telefono: string;
  activo: boolean;
  password: string;
}

/// SCRUM-42 / RF06, RF07. El admin edita los datos de CUALQUIER usuario
/// (PUT /Usuario/actualizar/admin), a diferencia de Mi Perfil (Lote 2) que
/// solo deja a cada uno editar lo propio. Puede tocar "activo" ademas (el
/// propio usuario no).
@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './usuario-form.html',
  styleUrl: './usuario-form.css',
})
export class UsuarioForm {
  readonly id = input.required<string>();

  private readonly usuarioService = inject(UsuarioService);

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

  guardar(): void {
    const f = this.form();
    if (!f) return;

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
        next: () => {
          this.guardando.set(false);
          this.exito.set(true);
          this.actualizarCampo('password', '');
        },
        error: (err) => {
          this.guardando.set(false);
          this.error.set(extraerMensajeError(err));
        },
      });
  }
}
