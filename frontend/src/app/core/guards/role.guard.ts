import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { TipoUsuario } from '../models/usuario.model';

/// Exige sesion activa Y uno de los roles indicados. Sin sesion -> /login (con
/// returnUrl); con sesion pero rol equivocado -> /403. Misma decision 100%
/// sincronica que authGuard.
export function roleGuard(rolesPermitidos: TipoUsuario[]): CanActivateFn {
  return (_route, state) => {
    const auth = inject(AuthService);
    const router = inject(Router);

    if (!auth.estaLogueado()) {
      return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
    }
    if (!rolesPermitidos.includes(auth.rol()!)) {
      return router.createUrlTree(['/403']);
    }
    return true;
  };
}
