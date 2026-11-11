# AlojaME — Frontend

Aplicación web de AlojaME construida íntegramente en **Angular 22**. Consume por HTTP la API REST real del backend Spring Boot (`../tp-final-alojamiento`): no usa APIs simuladas ni json-server.

## Requisitos

- Node.js 22.22.3+ / 24.15+ / 26+ y npm
- El backend corriendo (por defecto en `http://localhost:8081`); ver el `README.md` de la raíz

## Comandos

```bash
npm install        # dependencias
npm start          # servidor de desarrollo en http://localhost:4200
npm run build      # compilación de producción en dist/frontend/browser
npm run watch      # compilación continua en modo desarrollo
```

## Configuración: URL del backend

La URL de la API **no está compilada en el código**: se lee de `public/env.js`, que se carga antes de Angular.

```js
window.__ALOJAME_CONFIG__ = {
  apiUrl: 'http://localhost:8081'   // en producción detrás de Nginx: '/api'
};
```

Después de `npm run build`, el archivo queda en `dist/frontend/browser/env.js` y se puede editar directamente en el servidor sin recompilar. Si no está definido, se usa `http://localhost:8081`.

## Arquitectura

```
src/app/
├── core/
│   ├── config/         api.config.ts: URL base de la API
│   ├── guards/         authGuard (exige sesión) y roleGuard(roles) (exige un rol)
│   ├── interceptors/   auth.interceptor: agrega el JWT y maneja los 401
│   ├── models/         interfaces TypeScript de cada entidad del backend
│   ├── services/       un servicio por recurso de la API (HttpClient)
│   └── utils/          extracción de mensajes de error HTTP y helpers de reservas
├── layout/             navbar por rol y páginas 403 y 404
└── features/
    ├── public/         home, búsqueda y detalle de alojamiento
    ├── auth/           registro, verificación de cuenta e inicio de sesión
    ├── cliente/        perfil, reservas, pago, reseñas y resumen de gastos
    ├── anfitrion/      alojamientos, galería, reservas recibidas, cobros y resumen
    └── admin/          usuarios, catálogos, reservas, flujo de dinero y estadísticas
```

- **Componentes standalone** y **signals** para el estado (sin NgModules).
- **Autenticación:** el `AuthService` guarda el JWT, decodifica el rol y expone `estaLogueado()` y `rol()` como signals. El interceptor agrega `Authorization: Bearer …` a cada petición y cierra la sesión ante un 401.
- **Protección de rutas:** `authGuard` redirige a `/login` (con `returnUrl`) si no hay sesión; `roleGuard([...])` además redirige a `/403` si el rol no está permitido. La decisión es sincrónica, sin llamadas a la API.
- **Renovación de token:** si el usuario cambia su email, el backend devuelve un JWT nuevo en el encabezado `X-New-Token` y el frontend lo reemplaza sin cortar la sesión.

## Rutas por rol

| Rol | Rutas |
|---|---|
| Público | `/`, `/buscar`, `/alojamientos/:id`, `/registro`, `/verificar-cuenta`, `/login` |
| Cualquier usuario con sesión | `/perfil`, `/mis-reservas/:id`, `/pago-resultado` |
| Cliente (y admin) | `/alojamientos/:id/reservar`, `/mis-reservas`, `/mis-gastos` |
| Anfitrión (y admin) | `/anfitrion/alojamientos` (alta, edición y galería), `/anfitrion/reservas`, `/anfitrion/cobros-pendientes`, `/anfitrion/resumen` |
| Administrador | `/admin/usuarios`, `/admin/catalogo-tipos`, `/admin/catalogo-servicios`, `/admin/reservas`, `/admin/flujo-dinero`, `/admin/estadisticas` |

## CRUDs implementados

| Entidad | Rol | Operaciones |
|---|---|---|
| Tipos de alojamiento | Administrador | alta, listado, edición, eliminación y reemplazo |
| Servicios | Administrador | alta, listado, edición, eliminación y reemplazo |
| Alojamientos | Anfitrión | alta, listado, edición, activación/desactivación, servicios y galería |
| Usuarios | Administrador | listado, edición, activación/desactivación y alta de administradores |
| Reservas | Cliente, anfitrión y administrador | alta, listado, detalle y transiciones de estado (confirmar, cancelar, finalizar) |

## Convenciones

- CSS propio, sin librerías de componentes. Los tokens globales de espaciado y breakpoints están en `src/styles.css`.
- Responsive mobile-first: base para celular (se prueba en 360px), `min-width: 768px` para tablet y `min-width: 1024px` para desktop. `src/styles.css` trae las utilidades que reusa toda la app: `.form-grid` (formularios en 1 o 2 columnas), `.form-actions` (botones apilados en mobile), `.table-cards` + `data-label` (tablas que se ven como tarjetas debajo de 1024px), `.kpi-grid` (tarjetas de métricas), `.page-header` y `.datos` (listas etiqueta/valor).
- Cada archivo nuevo lleva un comentario `///` que explica su propósito.
- Los errores del backend llegan en dos formatos (texto plano o el objeto de Spring Security); `http-error.util.ts` los normaliza a un mensaje legible para la interfaz.
