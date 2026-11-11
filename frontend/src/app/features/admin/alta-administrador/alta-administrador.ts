import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { UsuarioRegistro } from '../../../core/models/usuario.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

const FORM_VACIO: UsuarioRegistro = {
  nombre: '',
  apellido: '',
  usuarioLogin: '',
  email: '',
  password: '',
  telefono: '',
  tipo: 'ADMIN',
};

/// SCRUM-42 / RF07. Crear otro administrador (POST /Usuario/registrar/administrador,
/// exclusivo ADMIN) -- a diferencia del registro publico, queda ya verificado
/// (ver UsuarioService.crearAdministrador en el backend), sin pasar por email.
@Component({
  selector: 'app-alta-administrador',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './alta-administrador.html',
  styleUrl: './alta-administrador.css',
})
export class AltaAdministrador {
  private readonly usuarioService = inject(UsuarioService);

  readonly form = signal<UsuarioRegistro>({ ...FORM_VACIO });
  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);
  readonly exito = signal(false);

  actualizarCampo<K extends keyof UsuarioRegistro>(campo: K, valor: UsuarioRegistro[K]): void {
    this.form.update((actual) => ({ ...actual, [campo]: valor }));
  }

  enviar(): void {
    this.error.set(null);
    this.enviando.set(true);
    this.usuarioService.registrarAdministrador(this.form()).subscribe({
      next: () => {
        this.enviando.set(false);
        this.exito.set(true);
        this.form.set({ ...FORM_VACIO });
      },
      error: (err) => {
        this.enviando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }
}
