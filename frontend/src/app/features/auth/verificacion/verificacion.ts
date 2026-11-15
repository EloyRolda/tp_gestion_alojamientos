import { Component, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { ErroresCampo, enfocarPrimerError, erroresDelBackend, hayErrores, validar, reglas } from '../../../core/utils/validacion.util';
import { ErrorCampo } from '../../../shared/error-campo/error-campo';

type Estado = 'inicial' | 'verificando' | 'exito' | 'error';

/// SCRUM-26 / RF02. Llega aca desde el link del mail: /verificar-cuenta?token=XXXX
/// (token bindeado solo via withComponentInputBinding, ver app.config.ts). Si no
/// hay token en la URL (o el que habia vencio), ofrece reenviar el correo.
@Component({
  selector: 'app-verificacion',
  standalone: true,
  imports: [FormsModule, RouterLink, ErrorCampo],
  templateUrl: './verificacion.html',
  styleUrl: './verificacion.css',
})
export class Verificacion {
  readonly token = input<string>();

  private readonly auth = inject(AuthService);

  readonly estado = signal<Estado>('inicial');
  readonly mensaje = signal<string | null>(null);

  readonly email = signal('');
  readonly reenviando = signal(false);
  readonly reenviado = signal(false);
  readonly errorReenvio = signal<string | null>(null);
  /// Error del email, debajo del campo (ver validacion.util.ts).
  readonly errores = signal<ErroresCampo>({});

  constructor() {
    effect(() => {
      const tokenActual = this.token();
      if (tokenActual) {
        this.verificarToken(tokenActual);
      }
    });
  }

  reenviar(): void {
    this.errorReenvio.set(null);
    const errores = validar({ email: this.email() }, { email: [reglas.obligatorio, reglas.email] });
    this.errores.set(errores);
    if (hayErrores(errores)) {
      enfocarPrimerError(errores);
      return;
    }
    this.reenviando.set(true);
    this.auth.reenviarVerificacion(this.email()).subscribe({
      next: () => {
        this.reenviando.set(false);
        this.reenviado.set(true);
      },
      error: (err) => {
        this.reenviando.set(false);
        const { porCampo, general } = erroresDelBackend(err, { email: 'email' });
        this.errores.set(porCampo);
        this.errorReenvio.set(general);
      },
    });
  }

  private verificarToken(token: string): void {
    this.estado.set('verificando');
    this.auth.verificar(token).subscribe({
      next: (msg) => {
        this.estado.set('exito');
        this.mensaje.set(msg);
      },
      error: (err) => {
        this.estado.set('error');
        this.mensaje.set(extraerMensajeError(err));
      },
    });
  }
}
