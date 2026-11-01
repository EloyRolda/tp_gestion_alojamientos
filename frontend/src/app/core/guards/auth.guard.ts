import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/// Exige sesion activa. La decision es 100% sincronica (lee el signal del JWT
/// ya decodificado, sin llamar a la API) para que el guard nunca "parpadee".
export const authGuard: CanActivateFn = (_route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.estaLogueado()) {
    return true;
  }
  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
