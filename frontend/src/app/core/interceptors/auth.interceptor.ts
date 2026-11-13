import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/// Adjunta el JWT a cada request (si hay sesion) y redirige a /login cuando la
/// API responde 401 (token vencido o invalido) -- salvo que el 401 sea la
/// propia respuesta de /auth/login (credenciales incorrectas o cuenta sin
/// verificar), que el formulario de login maneja inline, no como logout global.
///
/// Un 403 a un pedido que llevaba token tambien puede ser una sesion que ya no
/// sirve (el backend responde 403, no 401, a un token vencido o de una cuenta
/// desactivada): AuthService.verificarSesion lo confirma y, si es asi, cierra
/// la sesion. Un 403 "normal" (sin permiso para ese recurso) no cierra nada.
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  const token = auth.token();
  const request = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      const esIntentoDeLogin = req.url.endsWith('/auth/login');
      if (error.status === 401 && !esIntentoDeLogin) {
        auth.expirar();
      } else if (error.status === 403 && token && !req.url.endsWith('/Usuario/me')) {
        auth.verificarSesion();
      }
      return throwError(() => error);
    }),
  );
};
