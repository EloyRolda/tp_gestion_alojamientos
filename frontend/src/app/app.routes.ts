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
  { path: 'buscar', title: 'Buscar alojamientos', component: Busqueda },
  { path: 'alojamientos/:id', title: 'Detalle del alojamiento', component: DetalleAlojamiento },
  {
    path: 'alojamientos/:id/reservar',
    title: 'Reservar',
    component: FormularioReserva,
    canActivate: [roleGuard(['CLIENTE', 'ADMIN'])],
  },
  // Paginas de texto: se descargan recien cuando alguien las abre.
  {
    path: 'sobre-nosotros',
    title: 'Sobre nosotros',
    loadComponent: () => import('./features/public/sobre-nosotros/sobre-nosotros').then((m) => m.SobreNosotros),
  },
  { path: 'ayuda', title: 'Ayuda y legales', loadComponent: () => import('./features/public/ayuda/ayuda').then((m) => m.Ayuda) },
  { path: 'registro', title: 'Crear cuenta', component: Registro },
  { path: 'verificar-cuenta', title: 'Verificar cuenta', component: Verificacion },
  { path: 'login', title: 'Iniciar sesión', component: Login },
  { path: 'perfil', title: 'Mi perfil', component: MiPerfil, canActivate: [authGuard] },
  { path: 'mis-reservas', title: 'Mis reservas', component: MisReservas, canActivate: [roleGuard(['CLIENTE', 'ADMIN'])] },
  { path: 'mis-reservas/:id', title: 'Detalle de la reserva', component: DetalleReserva, canActivate: [authGuard] },
  { path: 'pago-resultado', title: 'Resultado del pago', component: PagoResultado, canActivate: [authGuard] },
  { path: 'mis-gastos', title: 'Mis gastos', component: ResumenGastos, canActivate: [roleGuard(['CLIENTE', 'ADMIN'])] },
  // Anfitrion: solo lo usan ANFITRION y ADMIN, se descarga recien al entrar.
  {
    path: 'anfitrion/alojamientos',
    title: 'Mis alojamientos',
    loadComponent: () => import('./features/anfitrion/mis-alojamientos/mis-alojamientos').then((m) => m.MisAlojamientos),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/alojamientos/nuevo',
    title: 'Publicar alojamiento',
    loadComponent: () => import('./features/anfitrion/alojamiento-form/alojamiento-form').then((m) => m.AlojamientoForm),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/alojamientos/:id/editar',
    title: 'Editar alojamiento',
    loadComponent: () => import('./features/anfitrion/alojamiento-form/alojamiento-form').then((m) => m.AlojamientoForm),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/alojamientos/:id/galeria',
    title: 'Galería de fotos',
    loadComponent: () => import('./features/anfitrion/galeria-alojamiento/galeria-alojamiento').then((m) => m.GaleriaAlojamiento),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/reservas',
    title: 'Reservas recibidas',
    loadComponent: () => import('./features/anfitrion/reservas-anfitrion/reservas-anfitrion').then((m) => m.ReservasAnfitrion),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/reservas/:id',
    title: 'Detalle de la reserva',
    loadComponent: () => import('./features/anfitrion/detalle-reserva-anfitrion/detalle-reserva-anfitrion').then((m) => m.DetalleReservaAnfitrion),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/cobros-pendientes',
    title: 'Cobros pendientes',
    loadComponent: () => import('./features/anfitrion/cobros-pendientes/cobros-pendientes').then((m) => m.CobrosPendientes),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  {
    path: 'anfitrion/resumen',
    title: 'Resumen de mis alojamientos',
    loadComponent: () => import('./features/anfitrion/resumen-alojamientos/resumen-alojamientos').then((m) => m.ResumenAlojamientos),
    canActivate: [roleGuard(['ANFITRION', 'ADMIN'])],
  },
  // Administracion: solo la usa el rol ADMIN, se descarga recien al entrar.
  {
    path: 'admin/usuarios',
    title: 'Usuarios',
    loadComponent: () => import('./features/admin/gestion-usuarios/gestion-usuarios').then((m) => m.GestionUsuarios),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/usuarios/nuevo-admin',
    title: 'Crear administrador',
    loadComponent: () => import('./features/admin/alta-administrador/alta-administrador').then((m) => m.AltaAdministrador),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/usuarios/:id/editar',
    title: 'Editar usuario',
    loadComponent: () => import('./features/admin/usuario-form/usuario-form').then((m) => m.UsuarioForm),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/catalogo-tipos',
    title: 'Catálogo de tipos',
    loadComponent: () => import('./features/admin/catalogo-tipos/catalogo-tipos').then((m) => m.CatalogoTipos),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/catalogo-servicios',
    title: 'Catálogo de servicios',
    loadComponent: () => import('./features/admin/catalogo-servicios/catalogo-servicios').then((m) => m.CatalogoServicios),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/reservas',
    title: 'Todas las reservas',
    loadComponent: () => import('./features/admin/todas-reservas/todas-reservas').then((m) => m.TodasReservas),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/reservas/:id',
    title: 'Detalle de la reserva',
    loadComponent: () => import('./features/admin/detalle-reserva-admin/detalle-reserva-admin').then((m) => m.DetalleReservaAdmin),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/flujo-dinero',
    title: 'Flujo de dinero',
    loadComponent: () => import('./features/admin/flujo-dinero/flujo-dinero').then((m) => m.FlujoDineroComponent),
    canActivate: [roleGuard(['ADMIN'])],
  },
  {
    path: 'admin/estadisticas',
    title: 'Estadísticas',
    loadComponent: () => import('./features/admin/estadisticas-globales/estadisticas-globales').then((m) => m.EstadisticasGlobales),
    canActivate: [roleGuard(['ADMIN'])],
  },
  { path: '403', title: 'Sin permiso', component: Forbidden },
  { path: '**', title: 'Página no encontrada', component: NotFound },
];
