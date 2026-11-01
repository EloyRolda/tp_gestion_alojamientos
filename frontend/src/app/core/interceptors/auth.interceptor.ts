import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/// Adjunta el JWT a cada request (si hay sesion) y redirige a /login cuando la
/// API responde 401 (token vencido o invalido) -- salvo que el 401 sea la
/// propia respuesta de /auth/login (credenciales incorrectas o cuenta sin
/// verificar), que el formulario de login maneja inline, no como logout global.
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const token = auth.token();
  const request = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      const esIntentoDeLogin = req.url.endsWith('/auth/login');
      if (error.status === 401 && !esIntentoDeLogin) {
        auth.cerrarSesion();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    }),
  );
};
