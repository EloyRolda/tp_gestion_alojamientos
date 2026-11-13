import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { TipoUsuario, UsuarioRegistro } from '../../../core/models/usuario.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

const FORM_VACIO: UsuarioRegistro = {
  nombre: '',
  apellido: '',
  usuarioLogin: '',
  email: '',
  password: '',
  telefono: '',
  tipo: 'CLIENTE',
};

/// SCRUM-25 / RF01. El tipo ADMIN no se ofrece: el propio backend lo rechaza
/// desde este endpoint (ver UsuarioController.registrar / UsuarioService.crear).
@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {
  private readonly auth = inject(AuthService);

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
    this.auth.registrar(this.form()).subscribe({
      next: () => {
        this.enviando.set(false);
        this.exito.set(true);
      },
      error: (err) => {
        this.enviando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  readonly tipos: { valor: TipoUsuario; etiqueta: string }[] = [
    { valor: 'CLIENTE', etiqueta: 'Cliente (quiero reservar alojamientos)' },
    { valor: 'ANFITRION', etiqueta: 'Anfitrión (quiero publicar alojamientos)' },
  ];
}
