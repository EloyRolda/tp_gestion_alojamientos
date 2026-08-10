# Gestion de Alojamiento (GA)

Sistema web tipo Airbnb para gestion de alojamientos turisticos: busqueda con
filtros, solicitud de reserva con aprobacion del anfitrion, pago (Mercado
Pago), chat entre cliente y anfitrion, reviews, notificaciones, reportes y
panel de administracion.

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 + Spring Boot 3.2.5 | Backend |
| Spring Security + JWT | Autenticacion y autorizacion por rol (stateless) |
| Spring Data JPA + MySQL | Persistencia |
| Spring WebSocket (STOMP) | Chat en tiempo real |
| Cloudinary | Fotos de usuarios y alojamientos |
| Mercado Pago (OAuth, opcional) / modo simulado | Cobro de reservas |
| Lombok, Bean Validation, SpringDoc OpenAPI | Utilidades |
| HTML / CSS / JS (vanilla, sin build tool) | Frontend estatico |

---

## Antes de arrancar: variables de entorno

La app lee configuracion sensible desde variables de entorno (`application.properties`
usa `${VARIABLE}`). Copiar `.env.example` a `.env` (o cargar esas mismas
variables en tu IDE / shell) o el arranque falla con
`Could not resolve placeholder`.

```bash
cp GA/.env.example GA/.env
# completar DB_USER, DB_PASSWORD, JWT_SECRET, credenciales de Cloudinary, etc.
```

Como Spring Boot no lee `.env` de forma nativa, hay que exportarlas antes de
correr, por ejemplo:

```bash
export $(grep -v '^#' GA/.env | xargs) && ./mvnw spring-boot:run
```

o cargarlas en la configuracion de "Environment variables" de tu IDE.

Las variables de Mercado Pago (`MP_CLIENT_ID`, `MP_CLIENT_SECRET`) son
**opcionales**: si quedan vacias, la app arranca igual y los anfitriones
solo pueden cargar un alias/CVU manual (ver mas abajo).

---

## Como ejecutar

1. Tener MySQL corriendo y crear la base `newgestiondb` (o la que pongas en `DB_NAME`).
2. Cargar las variables de entorno (ver arriba).
3. `cd GA && ./mvnw spring-boot:run`
4. Abrir `http://localhost:8080` (o directamente `http://localhost:8080/login.html`)

Swagger: `http://localhost:8080/swagger-ui/index.html`

---

## Estructura del backend

```
src/main/java/GestionAlojamiento/
├── Config/          # Seguridad (JWT, roles), WebSocket, seed de amenities
├── DTO/             # Objetos de entrada/salida de los endpoints
├── Exception/       # Manejo global de excepciones
├── Model/           # Entidades JPA
│   └── Enums/
├── Repository/      # Spring Data JPA
├── RestController/  # Endpoints REST
└── Service/         # Logica de negocio (un service SOLO llama a otros
                      # services para datos de otro dominio, nunca a su
                      # repository/model directamente)
src/main/resources/static/   # Frontend (HTML + /js/api.js)
```

### Jerarquia de Alojamiento

`Alojamiento` es una clase abstracta con herencia JPA `JOINED`. `Casa`,
`Departamento` y `Hotel` la extienden directamente, por lo que
`AlojamientoRepository` puede listar/buscar los tres tipos mezclados (listado
estilo Airbnb) sin tocar las tablas de cada subtipo.

### Maquina de estados de Reserva

```
SOLICITADA -> ACEPTADA (48hs para pagar) -> PAGADA -> FINALIZADA
           -> RECHAZADA                 -> VENCIDA
SOLICITADA/ACEPTADA -> CANCELADA (por el cliente, antes de pagar)
```

Un `@Scheduled` (`SchedulerService`) vence automaticamente las reservas
`ACEPTADA` cuyas 48hs para pagar se cumplieron, y cierra los chats 48hs
despues de finalizada la estadia.

### Pagos

`PagoService` trabaja en modo simulado (no hay SDK de Mercado Pago instalado
todavia): genera una preferencia falsa y el propio frontend dispara la
confirmacion en `pagar-reserva.html`. Antes de poder cobrar, el anfitrion
tiene que haber configurado un alias/CVU o conectado su cuenta real via
OAuth (`mercadopago.html` / `MercadoPagoController`) — sin plata real
involucrada mientras `MP_CLIENT_ID`/`MP_CLIENT_SECRET` esten vacias.

### Amenities

Catalogo modular (`Amenity`, relacion muchos-a-muchos con `Alojamiento`) en
vez de un set fijo de booleanos. Se precarga un catalogo base al arrancar
(`DataSeeder`).

---

## Roles y permisos

| Rol | Capacidades |
|---|---|
| **ADMINISTRADOR** | Gestion total: usuarios, baja logica de alojamientos, reportes, logs de auditoria, catalogo de amenities |
| **ANFITRION** | Publicar/editar alojamientos, aceptar o rechazar solicitudes, chatear con huespedes, resenarlos, ver estadisticas de ingresos |
| **CLIENTE** | Buscar y solicitar reservas, pagar, chatear con el anfitrion, dejar reviews, ver su historial |

---

## Frontend

Paginas HTML minimas (sin frameworks ni CSS de mas) servidas como estaticos
desde `src/main/resources/static/`, con un unico helper JS compartido
(`js/api.js`) para manejar el token JWT y las llamadas a la API. Pensado asi
a proposito para que sirva de base limpia sobre la que trabajar HTML/CSS/JS
desde cero.

---

## Documentacion de la API

`http://localhost:8080/swagger-ui/index.html`

---

## Licencia

Proyecto academico.
