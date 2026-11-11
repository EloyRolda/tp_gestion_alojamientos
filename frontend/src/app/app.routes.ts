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
import { MisAlojamientos } from './features/anfitrion/mis-alojamientos/mis-alojamientos';
import { AlojamientoForm } from './features/anfitrion/alojamiento-form/alojamiento-form';
import { GaleriaAlojamiento } from './features/anfitrion/galeria-alojamiento/galeria-alojamiento';
import { ReservasAnfitrion } from './features/anfitrion/reservas-anfitrion/reservas-anfitrion';
import { DetalleReservaAnfitrion } from './features/anfitrion/detalle-reserva-anfitrion/detalle-reserva-anfitrion';
import { CobrosPendientes } from './features/anfitrion/cobros-pendientes/cobros-pendientes';
import { ResumenAlojamientos } from './features/anfitrion/resumen-alojamientos/resumen-alojamientos';
import { GestionUsuarios } from './features/admin/gestion-usuarios/gestion-usuarios';
import { UsuarioForm } from './features/admin/usuario-form/usuario-form';
import { AltaAdministrador } from './features/admin/alta-administrador/alta-administrador';
import { CatalogoTipos } from './features/admin/catalogo-tipos/catalogo-tipos';
import { CatalogoServicios } from './features/admin/catalogo-servicios/catalogo-servicios';
import { TodasReservas } from './features/admin/todas-reservas/todas-reservas';
import { DetalleReservaAdmin } from './features/admin/detalle-reserva-admin/detalle-reserva-admin';
import { FlujoDineroComponent } from './features/admin/flujo-dinero/flujo-dinero';
import { EstadisticasGlobales } from './features/admin/estadisticas-globales/estadisticas-globales';

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
  { path: 'anfitrion/alojamientos', component: MisAlojamientos, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/alojamientos/nuevo', component: AlojamientoForm, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/alojamientos/:id/editar', component: AlojamientoForm, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/alojamientos/:id/galeria', component: GaleriaAlojamiento, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/reservas', component: ReservasAnfitrion, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/reservas/:id', component: DetalleReservaAnfitrion, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/cobros-pendientes', component: CobrosPendientes, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'anfitrion/resumen', component: ResumenAlojamientos, canActivate: [roleGuard(['ANFITRION', 'ADMIN'])] },
  { path: 'admin/usuarios', component: GestionUsuarios, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/usuarios/nuevo-admin', component: AltaAdministrador, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/usuarios/:id/editar', component: UsuarioForm, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/catalogo-tipos', component: CatalogoTipos, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/catalogo-servicios', component: CatalogoServicios, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/reservas', component: TodasReservas, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/reservas/:id', component: DetalleReservaAdmin, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/flujo-dinero', component: FlujoDineroComponent, canActivate: [roleGuard(['ADMIN'])] },
  { path: 'admin/estadisticas', component: EstadisticasGlobales, canActivate: [roleGuard(['ADMIN'])] },
  { path: '403', component: Forbidden },
  { path: '**', component: NotFound },
];
