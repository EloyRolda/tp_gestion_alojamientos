# Gestión de Alojamiento (GA)

Sistema web para la gestión de alojamientos turísticos que permite registrar, consultar y reservar propiedades como hoteles, casas y departamentos.

---

## > Descripción

La aplicación expone una API REST con autenticación basada en roles. Los usuarios pueden registrarse como **clientes**, **anfitriones** o **administradores**, cada uno con permisos diferenciados. Los anfitriones publican sus propiedades, los clientes realizan reservas y dejan reseñas, y los administradores gestionan todo el sistema.

---

## > Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| **Java** | 21 | Lenguaje principal |
| **Spring Boot** | 3.2.5 | Framework principal |
| **Spring Security** | — | Autenticación y autorización por roles |
| **Spring Data JPA** | — | Persistencia y acceso a datos |
| **MySQL** | — | Base de datos relacional |
| **Lombok** | — | Reducción de boilerplate |
| **Bean Validation** | — | Validación de entidades y DTOs |
| **SpringDoc OpenAPI (Swagger)** | 2.5.0 | Documentación interactiva de la API |
| **Maven** | — | Gestión de dependencias y build |
| **HTML / CSS / JS** | — | Frontend estático |

---

## > Estructura del proyecto

```
src/main/java/GestionAlojamiento/
├── Config/          # Seguridad (Spring Security, UserDetailsService)
├── DTO/             # Objetos de transferencia de datos (registro y modificación)
├── Exception/       # Manejo global de excepciones
├── Model/           # Entidades JPA (Alojamiento, Casa, Hotel, Departamento, Usuario, Reserva, Review...)
│   └── Enums/       # TipoInmueble, TipoUsuario, TipoEstado
├── Repository/      # Interfaces JPA Repository
├── RestController/  # Endpoints REST
└── Service/         # Lógica de negocio
src/main/resources/static/   # Páginas HTML del frontend
```

---

## > Roles y permisos

| Rol | Capacidades |
|---|---|
| **ADMIN** | Acceso total: gestión de usuarios, alojamientos, reservas y reseñas |
| **ANFITRION** | Registrar y gestionar sus propias propiedades |
| **CLIENTE** | Hacer reservas y dejar reseñas |

---

## > Entidades principales

- **Alojamiento** — entidad base con herencia hacia `Casa`, `Hotel` y `Departamento`
- **Usuario** — con soporte para anfitriones (matrícula) y clientes (método de pago)
- **Reserva** — vincula un cliente con un alojamiento en un rango de fechas
- **Review** — reseña de un cliente sobre un alojamiento
- **Dirección** y **Servicio** — datos complementarios del alojamiento

---

## > Cómo ejecutar

### Requisitos previos
- Java 21+
- Maven
- MySQL en ejecución [XAMPP]

### Pasos

1. Clonar el repositorio y acceder al directorio:
   ```bash
   git clone https://github.com/EloyRolda/tp_gestion_alojamientos
   cd GA
   ```

2. Configurar la base de datos en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gestion_alojamiento
   spring.datasource.username=  [tu_usuario]
   spring.datasource.password=  [tu_password]
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Compilar y ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Acceder a la aplicación en `http://localhost:8080`

---

## > Documentación de la API

Una vez levantada la aplicación, la documentación interactiva (Swagger UI) está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

---

## > Licencia

Proyecto académico.