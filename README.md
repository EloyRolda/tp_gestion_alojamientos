# AlojaME

Plataforma web de publicación y reserva de alojamientos temporarios, con un modelo tipo marketplace: los anfitriones publican sus propiedades, los clientes las reservan y pagan a través de la plataforma, y la plataforma retiene el dinero y lo liquida al anfitrión una vez cumplida la estadía.

Trabajo Práctico Final de la Tecnicatura Universitaria en Programación (UTN — Facultad Regional Mar del Plata, 2026), articulado entre **Programación IV**, **Gestión de Desarrollo de Software** y **Metodología de Sistemas II**. Autor: **Eloy Roldán**.

| Capa | Tecnologías |
|---|---|
| Frontend | Angular 22 (componentes standalone, signals, guards, interceptor HTTP), TypeScript, CSS propio, Leaflet (mapas) |
| Backend | Java 21, Spring Boot 3.4.3 (Web, Data JPA, Security, Validation), JWT (JJWT 0.12.6), Lombok, Springdoc OpenAPI |
| Base de datos | MariaDB / MySQL |
| Servicios externos | MercadoPago (Checkout Pro, reembolsos, liquidaciones), Brevo (emails transaccionales), Cloudinary (imágenes), OpenStreetMap y Nominatim (mapas y búsqueda de direcciones) |

---

## Funcionalidades por rol

- **Visitante (sin cuenta):** navega el catálogo completo (listado con fotos, búsqueda con filtros en lista o en mapa, detalle, galería y reseñas) sin necesidad de iniciar sesión, y consulta las páginas de ayuda y legales.
- **Cliente:** registro con verificación de cuenta por email (token de un solo uso, 24 h), edición de perfil, reserva de alojamientos con control de superposición de fechas, pago con MercadoPago, detalle de reservas con comprobante, cancelación según la política vigente, reseñas tras una estadía finalizada y resumen de gastos.
- **Anfitrión:** alta, edición y activación/desactivación de alojamientos (con su ubicación marcada en el mapa), galería de imágenes en Cloudinary, servicios por alojamiento, gestión de las reservas recibidas (confirmar, cancelar, finalizar), alias/CBU/CVU de cobro validado (dígito verificador e identificación del banco), cobros pendientes de liquidar y resumen de ingresos por alojamiento.
- **Administrador:** gestión de usuarios (incluye alta de otros administradores), CRUD de los catálogos de tipos de alojamiento y servicios, todas las reservas de la plataforma, reembolsos manuales y estadísticas globales (flujo de dinero, ranking de alojamientos, cancelaciones por motivo, ingresos mensuales).

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
│   └── db/                    Scripts SQL: catálogo inicial, datos de prueba y demo de Mar del Plata
├── frontend/                  Frontend Angular 22 (ver frontend/README.md)
├── Archives/                  Documentación académica
│   ├── Archivos_Nuevos [4T]/  Consignas de Gestión, Metodología y Programación IV
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
- `datos_demo_mar_del_plata.sql`: 25 anfitriones, 50 clientes y 75 alojamientos en Mar del Plata, con reservas, pagos y reseñas.

La contraseña de todos los usuarios de prueba es `Prueba2026`. Cada script de datos tiene su `borrar_...sql` para sacarlos.

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

- **Backend:** completo y auditado en tres rondas (arquitectura, seguridad y lógica de negocio). 65 endpoints REST.
- **Frontend:** los cuatro lotes funcionales (vistas públicas, Cliente, Anfitrión y Administrador) están terminados y verificados en vivo contra el backend, incluido un pago completo en el entorno de pruebas de MercadoPago y la subida de imágenes a Cloudinary.
- **Responsive:** el frontend se adapta a celular, tablet y desktop (Lote 5, sprint de Jira SCRUM-49): verificado en las 33 pantallas con los cuatro perfiles en 360, 768, 1024 y 1280px, sin scroll horizontal y con controles táctiles de 44px.
- **Identidad visual (Lote 6, sprint de Jira SCRUM-59):** paleta y tipografía propias, encabezado con buscador fijo y menú de cuenta, pie de página, páginas de ayuda y legales, fotos en los listados, búsqueda en mapa, esqueletos de carga, estados vacíos y avisos. La marca es **Kaza**, la mascota de AlojaME: una carita adentro de una casa con techo, siempre de un solo color (amarillo sobre fondos oscuros, negro sobre fondos claros).
- **Limitaciones conocidas:** la API de liquidaciones (payouts) de MercadoPago rechaza las credenciales de prueba con `400 "Invalid signature"`; requiere una habilitación especial de la cuenta. El resto de la integración (preferencias, consulta de pagos y webhook) funciona con las mismas credenciales. No hay tests automatizados de backend por decisión de alcance.

## Documentación

- `frontend/README.md` — estructura y convenciones del frontend.
- `GA/.env.example` — variables de entorno que necesita el backend.
- `scriptv2.sql` — script SQL de referencia del esquema.
- `GA/db/` — scripts de catálogo inicial y datos de prueba.
