CREATE DATABASE IF NOT EXISTS db_gestion_alojamientos;
USE  db_gestion_alojamientos;

-- Dirección física de cada alojamiento
CREATE TABLE direccion (
    id_direccion   INT          PRIMARY KEY AUTO_INCREMENT,
    pais           VARCHAR(100) NOT NULL,
    provincia      VARCHAR(100),
    ciudad         VARCHAR(100) NOT NULL,
    codigo_postal  VARCHAR(30),
    calle          VARCHAR(150) NOT NULL,
    altura         INT          NOT NULL
);

-- Tabla base de todos los usuarios del sistema
CREATE TABLE usuario (
    id_usuario      INT          PRIMARY KEY AUTO_INCREMENT,
    nombre          VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    telefono        VARCHAR(30),
    fecha_registro  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    tipo            ENUM('CLIENTE','ANFITRION','ADMINISTRADOR') NOT NULL
);

-- Datos extra del cliente
CREATE TABLE cliente (
    id_usuario    INT          PRIMARY KEY,
    metodo_pago   VARCHAR(100),
    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
);

-- Datos extra del anfitrión
CREATE TABLE anfitrion (
    id_usuario INT PRIMARY KEY,
    CONSTRAINT fk_anfitrion_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
);

-- Datos extra del administrador
CREATE TABLE administrador (
    id_usuario INT          PRIMARY KEY,
    matricula  VARCHAR(100) NOT NULL,
    CONSTRAINT fk_admin_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
);
-- Tabla de comodidades/servicios
CREATE TABLE servicio(
    id_servicio 	  INT PRIMARY KEY AUTO_INCREMENT,
    tiene_cocina          BOOLEAN      NOT NULL DEFAULT FALSE,
    tiene_lavarropa       BOOLEAN      NOT NULL DEFAULT FALSE,
    tiene_wifi            BOOLEAN      NOT NULL DEFAULT FALSE,
    tiene_estacionamiento BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Tabla base de todos los alojamientos
CREATE TABLE alojamiento (
    id_alojamiento        INT          PRIMARY KEY AUTO_INCREMENT,
    titulo                VARCHAR(150) NOT NULL,
    descripcion           TEXT,
    precio_noche          DECIMAL(10,2)       NOT NULL CHECK (precio_noche > 0),
    capacidad             TINYINT      NOT NULL CHECK (capacidad > 0),
    cant_ambientes        TINYINT      NOT NULL DEFAULT 1,
    cant_habitaciones     TINYINT      NOT NULL DEFAULT 1,
    cant_camas            TINYINT      NOT NULL DEFAULT 1,
    cant_banios           TINYINT      NOT NULL DEFAULT 1,
     activo                BOOLEAN      NOT NULL DEFAULT TRUE,
    tipo                  ENUM('CASA','DEPARTAMENTO','HOTEL') NOT NULL,
    id_servicio	 	  INT 		NOT NULL,
    id_anfitrion          INT          NOT NULL,
    id_direccion          INT          NOT NULL,
    CONSTRAINT fk_aloj_anfitrion
        FOREIGN KEY (id_anfitrion) REFERENCES anfitrion(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_aloj_direccion
        FOREIGN KEY (id_direccion) REFERENCES direccion(id_direccion),
	CONSTRAINT fk_aloj_servicios
FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
);


-- Atributos propios de una casa
CREATE TABLE casa (
    id_alojamiento INT     PRIMARY KEY,
    tiene_patio    BOOLEAN NOT NULL DEFAULT FALSE,
    tiene_pileta   BOOLEAN NOT NULL DEFAULT FALSE,
    tiene_parrilla BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_casa_alojamiento
        FOREIGN KEY (id_alojamiento) REFERENCES alojamiento(id_alojamiento)
        ON DELETE CASCADE
);

-- Atributos propios de un departamento
CREATE TABLE departamento (
    id_alojamiento     INT     PRIMARY KEY,
    piso               TINYINT NOT NULL DEFAULT 1,
    tiene_ascensor     BOOLEAN NOT NULL DEFAULT FALSE,
    expensas_incluidas BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_depto_alojamiento
        FOREIGN KEY (id_alojamiento) REFERENCES alojamiento(id_alojamiento)
        ON DELETE CASCADE
);

-- Atributos propios de un hotel
CREATE TABLE hotel (
    id_alojamiento    INT     PRIMARY KEY,
    estrellas         TINYINT NOT NULL DEFAULT 1 CHECK (estrellas BETWEEN 1 AND 5),
    incluye_desayuno  BOOLEAN NOT NULL DEFAULT FALSE,
    servicio_limpieza BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_hotel_alojamiento
        FOREIGN KEY (id_alojamiento) REFERENCES alojamiento(id_alojamiento)
        ON DELETE CASCADE
);

-- Fechas disponibles de cada alojamiento
CREATE TABLE disponibilidad (
    id_disponibilidad INT     PRIMARY KEY AUTO_INCREMENT,
    fecha             DATE    NOT NULL,
    disponible        BOOLEAN NOT NULL DEFAULT TRUE,
    id_alojamiento    INT     NOT NULL,
    CONSTRAINT fk_disp_alojamiento
        FOREIGN KEY (id_alojamiento) REFERENCES alojamiento(id_alojamiento)
        ON DELETE CASCADE,
    CONSTRAINT uq_disp_fecha UNIQUE (id_alojamiento, fecha)
);

-- Reservas de clientes sobre alojamientos
CREATE TABLE reserva (
    id_reserva     INT     PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio   DATE    NOT NULL,
    fecha_fin      DATE    NOT NULL,
    precio_total   DECIMAL(10,2)  NOT NULL CHECK (precio_total >= 0),
    estado         ENUM('PENDIENTE','CONFIRMADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    id_cliente     INT     NOT NULL,
    id_alojamiento INT     NOT NULL,
    CONSTRAINT chk_fechas CHECK (fecha_fin > fecha_inicio),
    CONSTRAINT fk_reserva_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_alojamiento
        FOREIGN KEY (id_alojamiento) REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE 
);

-- Reseñas de clientes, una por alojamiento
CREATE TABLE review (
    id_review      INT     PRIMARY KEY AUTO_INCREMENT,
    puntuacion     TINYINT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    comentario     TEXT,
    fecha          datetime not null default current_timestamp,
    id_cliente     INT     NOT NULL,
    id_alojamiento INT     NOT NULL,
    CONSTRAINT uq_review_cliente_aloj UNIQUE (id_cliente, id_alojamiento),
    CONSTRAINT fk_review_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_review_alojamiento
        FOREIGN KEY (id_alojamiento) REFERENCES alojamiento(id_alojamiento) ON DELETE CASCADE 
);



