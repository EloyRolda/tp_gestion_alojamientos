# Gestion de Alojamiento (GA)

Sistema web tipo Airbnb para gestion de alojamientos turisticos: busqueda con
filtros, solicitud de reserva con aprobacion del anfitrion, pago (simulado,
listo para Mercado Pago), chat en tiempo real por reserva, notificaciones,
reviews, reportes y estadisticas para anfitriones y clientes.

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 + Spring Boot 3.2.5 | Backend |
| Spring Security + JWT | Autenticacion y autorizacion por roles |
| Spring Data JPA + MySQL | Persistencia |
| Spring WebSocket (STOMP) | Chat en tiempo real |
| Cloudinary | Almacenamiento de imagenes (carpeta por entidad) |
| Lombok / Bean Validation | Reduccion de boilerplate y validaciones |
| SpringDoc OpenAPI (Swagger) | Documentacion interactiva |
| HTML / CSS / JS (vanilla) | Frontend estatico minimo |

---

## Como ejecutar

### Requisitos
- Java 21+
- Maven (o el `mvnw` incluido)
- MySQL en ejecucion

### 1. Variables de entorno
Copiar `GA/.env.example` a `GA/.env` (o cargar esas mismas variables en tu IDE:
Run Configuration -> Environment variables) con los datos de tu base y tus
credenciales. Sin esto la app no arranca (falla con
`Could not resolve placeholder`).

### 2. Base de datos
No hace falta correr ningun script SQL a mano: con `spring.jpa.hibernate.ddl-auto=update`
Hibernate genera el esquema a partir de las entidades la primera vez que
arranca la app. Solo hay que tener la base `newgestiondb` creada (vacia) en MySQL.

### 3. Levantar el backend
```bash
cd GA
./mvnw spring-boot:run
```

### 4. Acceder
- Frontend: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

---

## Estructura del backend

```
src/main/java/GestionAlojamiento/
├── Config/          # Seguridad (JWT, roles), WebSocket, seed de amenities
├── DTO/             # Objetos de transferencia (registro/modificacion)
├── Exception/       # Manejo global de excepciones
├── Model/           # Entidades JPA
│   └── Enums/       # EstadoReserva, CategoriaAmenity, EstadoPago, etc.
├── Repository/       # Interfaces Spring Data JPA
├── RestController/   # Endpoints REST
└── Service/          # Logica de negocio (cada Service solo llama a OTROS
                       # Services para datos que no son suyos, nunca a un
                       # Repository ajeno)
src/main/resources/static/   # Frontend: HTML minimo + js/api.js (helper de fetch)
```

## Roles

| Rol | Capacidades |
|---|---|
| ADMINISTRADOR | Gestion total: usuarios, alojamientos, reservas, reportes, logs |
| ANFITRION | Publica y gestiona sus alojamientos, acepta/rechaza reservas, chatea, ve sus estadisticas |
| CLIENTE | Busca y reserva alojamientos, paga, chatea, deja reviews, ve su historial |

## Modelo de datos (resumen)

- **Alojamiento** (abstracta, herencia JPA `JOINED`) -> `Casa`, `Departamento`, `Hotel`
- **Amenity**: catalogo de comodidades, relacion muchos-a-muchos con Alojamiento
- **Reserva**: maquina de estados `SOLICITADA -> ACEPTADA (48hs para pagar) -> PAGADA -> FINALIZADA`
  (con `RECHAZADA`, `CANCELADA`, `VENCIDA` como estados terminales alternativos)
- **Pago**: traza el pago de Mercado Pago de una reserva (hoy en modo simulado)
- **Review**: reseña de un cliente sobre UNA estadia puntual (no sobre el alojamiento en general)
- **ReviewHuesped**: reseña que el anfitrion deja sobre el huesped
- **Chat / Mensaje**: 1 chat por reserva, se abre al pagar, se cierra 48hs despues de finalizada la estadia
- **Notificacion**: eventos automaticos (solicitud creada, aceptada, pago confirmado, etc.)
- **Log**: auditoria de acciones, visible solo para el admin
- **Reporte**: un usuario puede reportar a otro usuario o a un alojamiento

## Nota sobre el pago

Todavia no hay credenciales de Mercado Pago cargadas: `PagoService` trabaja en
modo simulado (el "checkout" es un boton que el propio frontend dispara). Esta
aislado en un unico archivo para que conectar el SDK real de Mercado Pago no
requiera tocar el resto del flujo de reservas/chat/notificaciones.

## Licencia

Proyecto academico.
