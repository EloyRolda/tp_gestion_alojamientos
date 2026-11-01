import { Routes } from '@angular/router';
import { Home } from './features/public/home/home';
import { Busqueda } from './features/public/busqueda/busqueda';
import { DetalleAlojamiento } from './features/public/detalle-alojamiento/detalle-alojamiento';
import { ReservarPlaceholder } from './features/public/reservar-placeholder/reservar-placeholder';
import { Registro } from './features/auth/registro/registro';
import { Verificacion } from './features/auth/verificacion/verificacion';
import { Login } from './features/auth/login/login';
import { NotFound } from './layout/not-found/not-found';
import { Forbidden } from './layout/forbidden/forbidden';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'buscar', component: Busqueda },
  { path: 'alojamientos/:id', component: DetalleAlojamiento },
  {
    path: 'alojamientos/:id/reservar',
    component: ReservarPlaceholder,
    canActivate: [roleGuard(['CLIENTE', 'ADMIN'])],
  },
  { path: 'registro', component: Registro },
  { path: 'verificar-cuenta', component: Verificacion },
  { path: 'login', component: Login },
  { path: '403', component: Forbidden },
  { path: '**', component: NotFound },
];
