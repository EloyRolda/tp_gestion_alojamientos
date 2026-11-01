import { Component, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { extraerMensajeError } from '../../../core/utils/http-error.util';

/// SCRUM-27 / RF03. "returnUrl" llega bindeado de la query string cuando
/// authGuard/roleGuard redirigen aca (ver app.config.ts, withComponentInputBinding).
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  readonly returnUrl = input<string>();

  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly identificador = signal('');
  readonly password = signal('');
  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);

  enviar(): void {
    this.error.set(null);
    this.enviando.set(true);
    this.auth.login(this.identificador(), this.password()).subscribe({
      next: () => {
        this.enviando.set(false);
        this.router.navigateByUrl(this.returnUrl() || '/');
      },
      error: (err) => {
        this.enviando.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }
}
