# AlojaME

Plataforma web de publicación y reserva de alojamientos temporarios, con un modelo tipo marketplace: los anfitriones publican sus propiedades, los clientes las reservan y pagan a través de la plataforma, y la plataforma retiene el dinero y lo liquida al anfitrión una vez cumplida la estadía.

Trabajo Práctico Final de la Tecnicatura Universitaria en Programación (UTN — Facultad Regional Mar del Plata, 2026), articulado entre **Programación IV**, **Gestión de Desarrollo de Software** y **Metodología de Sistemas II**. Autor: **Eloy Roldán**.

| Capa | Tecnologías |
|---|---|
| Frontend | Angular 22 (componentes standalone, signals, guards, interceptor HTTP), TypeScript, CSS propio |
| Backend | Java 21, Spring Boot 3.4.3 (Web, Data JPA, Security, Validation), JWT (JJWT 0.12.6), Lombok, Springdoc OpenAPI |
| Base de datos | MariaDB / MySQL |
| Servicios externos | MercadoPago (Checkout Pro, reembolsos, liquidaciones), Brevo (emails transaccionales), Cloudinary (imágenes) |

---

## Funcionalidades por rol

- **Visitante (sin cuenta):** navega el catálogo completo sin iniciar sesión: búsqueda con filtros (texto, precio, huéspedes y servicios) y paginado, vista de mapa, detalle con galería de fotos y reseñas.
- **Cliente:** registro con verificación de cuenta por email (token de un solo uso, 24 h), edición de perfil, reserva con un calendario que marca los días ocupados, pago con MercadoPago, cambio de fechas, cancelación con el reembolso explicado antes de confirmar, reseñas tras una estadía finalizada y resumen de gastos con gráfico.
- **Anfitrión:** alta, edición y pausa de alojamientos (con ubicación en el mapa), galería de imágenes en Cloudinary, servicios por alojamiento, gestión de las reservas recibidas (confirmar, cancelar, finalizar), alias/CBU/CVU de cobro validado (dígito verificador e identificación del banco), cobros pendientes y resumen de ingresos con gráficos.
- **Administrador:** gestión de usuarios con una ficha por usuario (sus números, alojamientos y reservas, con links cruzados), alta de otros administradores, baja en cascada (pausa los alojamientos y cancela las reservas sin pago), CRUD de los catálogos de tipos de alojamiento y servicios, todas las reservas de la plataforma, reembolsos manuales, flujo de dinero y estadísticas globales con gráficos.

## Reglas de negocio principales

- **Ciclo de una reserva:** `SOLICITADA → CONFIRMADA → FINALIZADA`, con `CANCELADA` posible desde `SOLICITADA` o `CONFIRMADA`. Al confirmar se genera el pago, con un plazo de 48 h; si no se paga, la reserva se cancela sola.
- **Pagos:** el anfitrión define cuánto quiere recibir; el cliente paga ese monto más la comisión de la plataforma (5 %) y la comisión estimada de MercadoPago (6 %). El anfitrión nunca opera la pasarela: cobra por liquidación a su alias o CBU.
- **Cancelaciones:** si cancela el anfitrión, reembolso total; si cancela el cliente con 48 h o más de anticipación, reembolso total; con menos de 48 h, reembolso del 65 % y el 35 % restante se liquida al anfitrión; el administrador puede definir un reembolso a medida. Las comisiones no se devuelven.
- **Seguridad:** JWT sin estado con tres roles, cuentas desactivadas que dejan de autenticar al instante, bloqueo pesimista en las transiciones de estado concurrentes, CORS con lista explícita de orígenes y ninguna credencial escrita en el código.

---

## Estructura del repositorio

```
tp_gestion_alojamientos/
├── GA/                        Backend Spring Boot
│   ├── src/main/java/...      Config, DTO, Model, Repository, RestController, Service, ...
│   ├── src/main/resources/    application.properties (todo se configura por variables de entorno)
│   ├── .env.example           Plantilla de variables de entorno
│   └── db/                    Scripts SQL: catálogo inicial, datos de prueba y demo completa
├── frontend/                  Frontend Angular 22 (ver frontend/README.md)
├── Archives/                  Documentación académica
│   ├── Archivos_Nuevos [4T]/  Consignas, dossier de Gestión, TP de Metodología y documentación del frontend
│   └── Archivos_Viejos [3T]/  Documentación de la etapa anterior (modelo conceptual, mapa de la base)
└── scriptv2.sql               Script SQL de referencia del esquema
```

## Requisitos

- Java 21 y Maven (o el wrapper `mvnw` incluido)
- Node.js 22.22.3+ / 24.15+ / 26+ y npm (requeridos por Angular CLI 22)
- MariaDB o MySQL
- Credenciales de Cloudinary, Brevo y MercadoPago (las de prueba alcanzan para desarrollo)

## Puesta en marcha en desarrollo

### 1. Base de datos

Crear una base vacía (por convención `testdb`). Las tablas las genera la aplicación sola en el primer arranque (`ddl-auto=update`).

Después del primer arranque del backend se pueden cargar datos desde `GA/db/`:

- `catalogo_inicial.sql`: tipos de alojamiento y servicios.
- `datos_de_prueba.sql`: 7 usuarios, 8 alojamientos y reservas en todos los estados.
- `datos_demo_completa.sql`: la demo completa, con anfitriones, clientes, alojamientos con fotos, reservas en todos los estados, pagos y reseñas. Al terminar imprime una guía con cada cuenta de prueba y el caso que muestra. Los alojamientos de la demo se ubican en Mar del Plata a propósito, para mostrar el sistema funcionando en una ciudad; la plataforma admite alojamientos en cualquier lugar. Para borrarla, `borrar_datos_demo_mar_del_plata.sql`.
- `datos_demo_para_mostrar.sql`: reservas extra para mostrar en vivo una finalización y una cancelación tardía.

La contraseña de todos los usuarios de prueba es `Prueba2026`. Los scripts no crean administradores: el primero se crea como se explica en el paso 6 del despliegue.

### 2. Backend

1. Copiar `GA/.env.example` y completar las variables. Son obligatorias las credenciales de base de datos, `JWT_SECRET` (`openssl rand -hex 32`), Cloudinary, Brevo y `MERCADOPAGO_ACCESS_TOKEN` (usar uno que empiece con `TEST-` para no mover dinero real). **Ninguna tiene valor por defecto:** si falta alguna, la aplicación no arranca.
2. Compilar y ejecutar desde `GA/`:
   ```bash
   ./mvnw -DskipTests package
   set -a; source .env; set +a
   java -jar target/tp-final-alojamiento-0.0.1-SNAPSHOT.jar
   ```
3. Queda escuchando en `http://localhost:8081` (`SERVER_PORT` para cambiarlo). La documentación interactiva de la API está en `/swagger-ui.html`.

### 3. Frontend

Desde `frontend/`:

```bash
npm install
npm start          # http://localhost:4200
```

La URL del backend se toma de `frontend/public/env.js` (por defecto `http://localhost:8081`).

### Modo de pagos simulados

MercadoPago de prueba no permite reembolsos ni liquidaciones a anfitriones. Para una demostración, con `PAGOS_SIMULADOS=true` en el backend esos dos movimientos se registran sin llamar a MercadoPago (las reservas cambian de estado igual) y, con `pagosSimulados: true` en `env.js`, el frontend lo aclara en cada aviso. El cobro al cliente sigue pasando por MercadoPago.

---

## Despliegue en un servidor

La arquitectura recomendada es un único dominio con HTTPS servido por Nginx:

```
Internet ──HTTPS──> Nginx
                     ├── /       → frontend compilado (archivos estáticos)
                     └── /api/   → backend en 127.0.0.1:8081 ──> MariaDB en 127.0.0.1:3306
```

1. **Compilar:** `./mvnw -DskipTests package` (backend) y `npm run build` (frontend, salida en `frontend/dist/frontend/browser`).
2. **Frontend:** copiar el contenido de `dist/frontend/browser` al directorio que sirve Nginx y editar `env.js` con `apiUrl: '/api'`. No hace falta recompilar para cambiar la URL de la API.
3. **Nginx:** servir los estáticos con `try_files $uri $uri/ /index.html` (necesario para las rutas de Angular) y reenviar `/api/` a `http://127.0.0.1:8081/`. Conviene bloquear `/api/actuator/` y `/api/swagger-ui` hacia afuera.
4. **Backend:** ejecutarlo como servicio (por ejemplo, con systemd) con las variables de entorno de producción:
   - `FRONTEND_URL=https://tu-dominio` (se usa en los emails de verificación y en el regreso desde MercadoPago)
   - `MERCADOPAGO_NOTIFICATION_URL=https://tu-dominio/api/Pago/webhook` (con HTTPS público, MercadoPago notifica los pagos solo)
   - `JPA_SHOW_SQL=false` y `HEALTH_SHOW_DETAILS=never` (valores por defecto)
5. **Credenciales:** usar credenciales nuevas en producción. Las de desarrollo no deben reutilizarse.
6. **Primer administrador:** el alta de administradores exige ya serlo, así que el primero se crea registrando una cuenta normal, verificándola y cambiándole el rol en la base:
   ```sql
   UPDATE usuario SET tipo = 'admin' WHERE email = 'tu-email@dominio.com';
   ```

---

## Estado del proyecto

- **Backend:** completo y auditado en tres rondas (arquitectura, seguridad y lógica de negocio).
- **Frontend:** terminado y verificado contra el backend con recorridos completos por cada perfil, incluido un pago en el entorno de pruebas de MercadoPago y la subida de imágenes a Cloudinary. Además de las pantallas de cada perfil (vistas públicas, Cliente, Anfitrión y Administrador), suma identidad visual propia, gráficos con Chart.js, mapa, calendario de reservas, ficha de usuario para el administrador y modo de pagos simulados para demostraciones.
- **Responsive:** se adapta a celular, tablet y desktop; verificado con los cuatro perfiles en 360, 768, 1024 y 1280 px, sin scroll horizontal y con controles táctiles de 44 px.
- **Limitaciones conocidas:** la API de liquidaciones (payouts) de MercadoPago rechaza las credenciales de prueba con `400 "Invalid signature"` (requiere una habilitación especial de la cuenta); para demostraciones está el modo de pagos simulados. Quedan como mejoras a futuro, porque necesitan cambios en el backend: búsqueda por fechas disponibles, recuperación de contraseña, cantidad de huéspedes por reserva y avisos por correo. No hay tests automatizados por decisión de alcance.

## Documentación

- `frontend/README.md` — comandos, estructura y convenciones del frontend.
- `capturas/` — capturas de la aplicación en escritorio y en celular, por perfil (visitante, cliente, anfitrión y administrador).
- `Archives/Archivos_Nuevos [4T]/Programacion/Documentacion_Frontend_AlojaME.pdf` — qué se construyó, cumplimiento de la consigna y las decisiones de diseño con su porqué.
- `Archives/Archivos_Nuevos [4T]/Programacion/Manual_de_Marca_AlojaME.pdf` — identidad visual: logotipo, Kaza, paleta, tipografía, componentes y tono de voz.
- `Archives/Archivos_Nuevos [4T]/Programacion/Bitacora_Tecnica_AlojaME.pdf` — arquitectura, reglas de negocio y errores encontrados y resueltos (causa y solución).
- `Archives/Archivos_Nuevos [4T]/Programacion/Plan_Desarrollo_Frontend_AlojaME.pdf` — desarrollo del frontend por etapas (maquetado, responsive, estilo y mejoras) y sprints, con lo verificado en cada una.
- `Archives/Archivos_Nuevos [4T]/Gestion de desarrollo/` — dossier de gestión del proyecto (alcance, C4, EDT, estimaciones, riesgos, seguimiento y cierre).
- `Archives/Archivos_Nuevos [4T]/Metodologia/` — auditoría de código con propuestas de mejora, diagramas UML y diseño de pruebas.
- `GA/.env.example` — variables de entorno que necesita el backend.
- `scriptv2.sql` — script SQL de referencia del esquema.
- `GA/db/` — scripts de catálogo inicial y datos de prueba.
