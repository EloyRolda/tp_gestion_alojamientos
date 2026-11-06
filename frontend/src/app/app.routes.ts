import { Routes } from '@angular/router';
import { Home } from './features/public/home/home';
import { Busqueda } from './features/public/busqueda/busqueda';
import { DetalleAlojamiento } from './features/public/detalle-alojamiento/detalle-alojamiento';
import { Registro } from './features/auth/registro/registro';
import { Verificacion } from './features/auth/verificacion/verificacion';
import { Login } from './features/auth/login/login';
import { NotFound } from './layout/not-found/not-found';
import { Forbidden } from './layout/forbidden/forbidden';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { MiPerfil } from './features/cliente/mi-perfil/mi-perfil';
import { FormularioReserva } from './features/cliente/formulario-reserva/formulario-reserva';
import { MisReservas } from './features/cliente/mis-reservas/mis-reservas';
import { DetalleReserva } from './features/cliente/detalle-reserva/detalle-reserva';
import { PagoResultado } from './features/cliente/pago-resultado/pago-resultado';
import { ResumenGastos } from './features/cliente/resumen-gastos/resumen-gastos';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'buscar', component: Busqueda },
  { path: 'alojamientos/:id', component: DetalleAlojamiento },
  {
    path: 'alojamientos/:id/reservar',
    component: FormularioReserva,
    canActivate: [roleGuard(['CLIENTE', 'ADMIN'])],
  },
  { path: 'registro', component: Registro },
  { path: 'verificar-cuenta', component: Verificacion },
  { path: 'login', component: Login },
  { path: 'perfil', component: MiPerfil, canActivate: [authGuard] },
  { path: 'mis-reservas', component: MisReservas, canActivate: [roleGuard(['CLIENTE', 'ADMIN'])] },
  { path: 'mis-reservas/:id', component: DetalleReserva, canActivate: [authGuard] },
  { path: 'pago-resultado', component: PagoResultado, canActivate: [authGuard] },
  { path: 'mis-gastos', component: ResumenGastos, canActivate: [roleGuard(['CLIENTE', 'ADMIN'])] },
  { path: '403', component: Forbidden },
  { path: '**', component: NotFound },
];
