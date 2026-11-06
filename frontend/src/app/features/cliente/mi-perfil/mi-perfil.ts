import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { UsuarioService, UsuarioModificar } from '../../../core/services/usuario.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

interface FormPerfil {
  nombre: string;
  apellido: string;
  usuarioLogin: string;
  email: string;
  telefono: string;
  password: string;
}

const FORM_VACIO: FormPerfil = {
  nombre: '',
  apellido: '',
  usuarioLogin: '',
  email: '',
  telefono: '',
  password: '',
};

/// SCRUM-28 / RF04, RF05. Ver y editar los datos personales del cliente logueado.
/// La contraseña queda en blanco a proposito (nunca se precarga): si se deja
/// vacia, no se manda en el PUT (UsuarioModificarDTO acepta todos los campos
/// opcionales, solo pisa lo que venga no-nulo).
@Component({
  selector: 'app-mi-perfil',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './mi-perfil.html',
  styleUrl: './mi-perfil.css',
})
export class MiPerfil {
  private readonly auth = inject(AuthService);
  private readonly usuarioService = inject(UsuarioService);

  readonly form = signal<FormPerfil>({ ...FORM_VACIO });
  readonly cargando = signal(true);
  readonly guardando = signal(false);
  readonly error = signal<string | null>(null);
  readonly exito = signal(false);

  constructor() {
    this.auth.refrescarPerfil().subscribe({
      next: (u) => {
        this.form.set({
          nombre: u.nombre,
          apellido: u.apellido,
          usuarioLogin: u.usuarioLogin,
          email: u.email,
          telefono: u.telefono,
          password: '',
        });
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  actualizarCampo<K extends keyof FormPerfil>(campo: K, valor: FormPerfil[K]): void {
    this.form.update((actual) => ({ ...actual, [campo]: valor }));
  }

  guardar(): void {
    this.error.set(null);
    this.exito.set(false);
    this.guardando.set(true);

    const actual = this.form();
    const dto: UsuarioModificar = {
      nombre: actual.nombre,
      apellido: actual.apellido,
      usuarioLogin: actual.usuarioLogin,
      email: actual.email,
      telefono: actual.telefono,
    };
    if (actual.password.trim().length > 0) {
      dto.password = actual.password;
    }

    this.usuarioService.actualizar(dto).subscribe({
      next: () => {
        this.guardando.set(false);
        this.exito.set(true);
        this.actualizarCampo('password', '');
        this.auth.refrescarPerfil().subscribe();
      },
      error: (err) => {
        this.guardando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }
}
