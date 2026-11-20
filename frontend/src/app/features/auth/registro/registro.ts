import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AvisoService } from '../../../core/services/aviso.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
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
  private readonly aviso = inject(AvisoService);

  readonly form = signal<UsuarioRegistro>({ ...FORM_VACIO });
  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);
  /// Error de cada campo, debajo del campo (ver validacion.util.ts).
  readonly errores = signal<ErroresCampo>({});
  readonly exito = signal(false);
  /// Email con el que se registro: lo usa el boton "Reenviar correo" sin pedirlo de nuevo.
  readonly emailRegistrado = signal('');
  readonly reenviando = signal(false);
  readonly errorReenvio = signal<string | null>(null);

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
        this.emailRegistrado.set(this.form().email.trim());
        this.exito.set(true);
        // El correo puede demorar unos minutos (algunos servidores de correo
        // retienen el primer mensaje de un remitente nuevo), asi que se avisa
        // con Kaza que ya salio y que se puede reenviar desde esta pantalla.
        this.aviso.mostrar('¡Correo enviado!', [
          `Te mandamos un correo a ${this.emailRegistrado()} para activar tu cuenta.`,
          'Si no te llega en unos minutos, revisá spam o tocá «Reenviar correo» e intentalo de nuevo.',
        ]);
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

  /// Reenvia el correo de verificacion al email recien registrado.
  reenviar(): void {
    this.errorReenvio.set(null);
    this.reenviando.set(true);
    this.auth.reenviarVerificacion(this.emailRegistrado()).subscribe({
      next: () => {
        this.reenviando.set(false);
        this.aviso.mostrar('¡Correo reenviado!', [
          `Te mandamos un correo nuevo a ${this.emailRegistrado()}.`,
          'Revisá también la carpeta de spam.',
        ]);
      },
      error: (err) => {
        this.reenviando.set(false);
        this.errorReenvio.set(extraerMensajeError(err));
      },
    });
  }

  readonly tipos: { valor: TipoUsuario; etiqueta: string }[] = [
    { valor: 'CLIENTE', etiqueta: 'Cliente (quiero reservar alojamientos)' },
    { valor: 'ANFITRION', etiqueta: 'Anfitrión (quiero publicar alojamientos)' },
  ];
}
