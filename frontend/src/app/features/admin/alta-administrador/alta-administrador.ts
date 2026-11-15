import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { UsuarioRegistro } from '../../../core/models/usuario.model';
import { ErroresCampo, enfocarPrimerError, erroresDelBackend, hayErrores, sinError, validar, CAMPOS_CUENTA, reglasCuenta } from '../../../core/utils/validacion.util';
import { ErrorCampo } from '../../../shared/error-campo/error-campo';

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
  imports: [FormsModule, RouterLink, ErrorCampo],
  templateUrl: './alta-administrador.html',
  styleUrl: './alta-administrador.css',
})
export class AltaAdministrador {
  private readonly usuarioService = inject(UsuarioService);

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
    this.usuarioService.registrarAdministrador(this.form()).subscribe({
      next: () => {
        this.enviando.set(false);
        this.exito.set(true);
        this.form.set({ ...FORM_VACIO });
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
}
