import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { TipoUsuario, UsuarioRegistro } from '../../../core/models/usuario.model';
import { ErroresCampo, enfocarPrimerError, erroresDelBackend, hayErrores, sinError, validar, CAMPOS_CUENTA, reglasCuenta } from '../../../core/utils/validacion.util';
import { ErrorCampo } from '../../../shared/error-campo/error-campo';

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
  imports: [FormsModule, RouterLink, ErrorCampo],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {
  private readonly auth = inject(AuthService);

  readonly form = signal<UsuarioRegistro>({ ...FORM_VACIO });
  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);
  /// Error de cada campo, debajo del campo (ver validacion.util.ts).
  readonly errores = signal<ErroresCampo>({});
  readonly exito = signal(false);

  actualizarCampo<K extends keyof UsuarioRegistro>(campo: K, valor: UsuarioRegistro[K]): void {
    this.form.update((actual) => ({ ...actual, [campo]: valor }));
    this.errores.update((e) => sinError(e, campo));
  }

  enviar(): void {
    this.error.set(null);
    const errores = validar(this.form(), reglasCuenta(true));
    this.errores.set(errores);
    if (hayErrores(errores)) {
      enfocarPrimerError(errores);
      return;
    }
    this.enviando.set(true);
    this.auth.registrar(this.form()).subscribe({
      next: () => {
        this.enviando.set(false);
        this.exito.set(true);
      },
      error: (err) => {
        this.enviando.set(false);
        const { porCampo, general } = erroresDelBackend(err, CAMPOS_CUENTA);
        this.errores.set(porCampo);
        this.error.set(general);
        enfocarPrimerError(porCampo);
      },
    });
  }

  readonly tipos: { valor: TipoUsuario; etiqueta: string }[] = [
    { valor: 'CLIENTE', etiqueta: 'Cliente (quiero reservar alojamientos)' },
    { valor: 'ANFITRION', etiqueta: 'Anfitrión (quiero publicar alojamientos)' },
  ];
}
