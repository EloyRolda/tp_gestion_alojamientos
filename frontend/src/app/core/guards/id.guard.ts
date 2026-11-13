import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

/// Para las rutas con ":id" (/alojamientos/17, /mis-reservas/5...): si el id no
/// es un numero entero positivo, la pantalla quedaba cargando para siempre
/// (nunca pedia nada a la API). Ahora va directo a "Página no encontrada".
export const idNumericoGuard: CanActivateFn = (route) => {
  const id = route.paramMap.get('id') ?? '';
  return /^[1-9]\d{0,17}$/.test(id) ? true : inject(Router).createUrlTree(['/no-encontrada']);
};
