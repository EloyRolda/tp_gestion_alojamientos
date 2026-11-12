-- =====================================================================
-- AlojaME -- datos de prueba
--
-- Carga un catalogo de ejemplo completo para probar la app de punta a punta:
-- 7 usuarios (3 anfitriones y 4 clientes), tipos de alojamiento, servicios,
-- 8 alojamientos con direccion (y su ubicacion en el mapa), galeria e imagenes,
-- 16 reservas en todos los estados posibles, sus pagos y 5 resenas.
--
-- Todas las fechas son relativas al dia en que se ejecuta (CURDATE/NOW), asi
-- que el script sirve igual hoy que dentro de unos meses.
--
-- Contrasena de TODOS los usuarios de prueba: Prueba2026
-- (No crea administradores: el admin se crea aparte, ver el README del repositorio.)
--
-- Uso (con el backend arrancado al menos una vez, para que existan las tablas):
--   mysql -u alojame -p alojame < datos_de_prueba.sql
--
-- Se ejecuta UNA sola vez: si se corre de nuevo, falla al primer usuario
-- repetido y no carga nada (todo va en una transaccion).
-- Para sacar estos datos antes de abrir el sitio al publico:
--   mysql -u alojame -p alojame < borrar_datos_de_prueba.sql
-- =====================================================================

SET NAMES utf8mb4;
START TRANSACTION;

-- ---------------------------------------------------------------- usuarios
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('martina.gomez', 'Martina', 'Gómez', 'martina.gomez.alojame@gmail.com', '+54 9 223 555-0101', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'martina.gomez.mp', DATE_SUB(NOW(), INTERVAL 150 DAY));
SET @h_martina = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('javier.ruiz', 'Javier', 'Ruiz', 'javier.ruiz.alojame@gmail.com', '+54 9 351 555-0102', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'javier.ruiz.cobros', DATE_SUB(NOW(), INTERVAL 143 DAY));
SET @h_javier = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('sofia.pereyra', 'Sofía', 'Pereyra', 'sofia.pereyra.alojame@gmail.com', '+54 9 11 555-0103', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'sofia.pereyra.mp', DATE_SUB(NOW(), INTERVAL 136 DAY));
SET @h_sofia = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('lucia.fernandez', 'Lucía', 'Fernández', 'lucia.fernandez.alojame@gmail.com', '+54 9 11 555-0201', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 129 DAY));
SET @c_lucia = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('tomas.diaz', 'Tomás', 'Díaz', 'tomas.diaz.alojame@gmail.com', '+54 9 341 555-0202', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 122 DAY));
SET @c_tomas = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('camila.lopez', 'Camila', 'López', 'camila.lopez.alojame@gmail.com', '+54 9 261 555-0203', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 115 DAY));
SET @c_camila = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro)
VALUES ('nicolas.acosta', 'Nicolás', 'Acosta', 'nicolas.acosta.alojame@gmail.com', '+54 9 387 555-0204', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 108 DAY));
SET @c_nicolas = LAST_INSERT_ID();

-- ---------------------------------------------------------------- catalogos
-- INSERT IGNORE: si un tipo o servicio ya existe (por ejemplo, creado a mano), se reusa.
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Departamento');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Casa');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Cabaña');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Loft');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Monoambiente');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Wifi');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Aire acondicionado');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Calefacción');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Cocina equipada');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Estacionamiento');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Pileta');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Parrilla');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Lavarropas');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Admite mascotas');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Vista al mar');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Televisor smart');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Ropa de cama y toallas');

-- ---------------------------------------------------------------- alojamientos
-- Cada uno con su direccion, su galeria (la app espera una por alojamiento),
-- 3 imagenes de ejemplo (picsum.photos) y sus servicios.

-- Departamento frente al mar en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Av. Patricio Peralta Ramos', 4800, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600', -38.028, -57.533);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Departamento frente al mar en Playa Grande', 'Departamento luminoso a una cuadra de la playa, con balcon y vista al mar.\nIdeal para una escapada en pareja o con amigos. Cocina completa y cochera cubierta.', 45000, 4, 3, 2, 1, 1, @h_martina, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a1 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento frente al mar en Playa Grande', @a1);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento frente al mar en Playa Grande - foto 1', 'datos-de-prueba/a1-1', 'https://picsum.photos/id/1015/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento frente al mar en Playa Grande - foto 2', 'datos-de-prueba/a1-2', 'https://picsum.photos/id/1040/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento frente al mar en Playa Grande - foto 3', 'datos-de-prueba/a1-3', 'https://picsum.photos/id/1048/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a1, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a1, id_servicio FROM servicio WHERE nombre_servicio = 'Vista al mar';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a1, id_servicio FROM servicio WHERE nombre_servicio = 'Cocina equipada';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a1, id_servicio FROM servicio WHERE nombre_servicio = 'Estacionamiento';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a1, id_servicio FROM servicio WHERE nombre_servicio = 'Televisor smart';

-- Cabaña de troncos con vista al lago
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Av. Bustillo', 9500, 'San Carlos de Bariloche', 'Río Negro', 'Argentina', '8400', -41.115, -71.405);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Cabaña de troncos con vista al lago', 'Cabaña cálida en el bosque, con hogar a leña y deck sobre el lago Nahuel Huapi.\nA 15 minutos del centro de Bariloche y del Cerro Catedral.', 68000, 6, 4, 3, 2, 1, @h_martina, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Cabaña'));
SET @a2 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Cabaña de troncos con vista al lago', @a2);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Cabaña de troncos con vista al lago - foto 1', 'datos-de-prueba/a2-1', 'https://picsum.photos/id/1018/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Cabaña de troncos con vista al lago - foto 2', 'datos-de-prueba/a2-2', 'https://picsum.photos/id/1036/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Cabaña de troncos con vista al lago - foto 3', 'datos-de-prueba/a2-3', 'https://picsum.photos/id/1039/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio = 'Calefacción';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio = 'Cocina equipada';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio = 'Parrilla';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio = 'Estacionamiento';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio = 'Admite mascotas';

-- Casa con pileta en las sierras
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Los Algarrobos', 120, 'Villa Carlos Paz', 'Córdoba', 'Argentina', '5152', -31.4241, -64.4978);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Casa con pileta en las sierras', 'Casa amplia con parque, pileta y quincho. Perfecta para familias o grupos grandes.\nA 5 minutos del lago San Roque.', 82000, 8, 5, 4, 3, 1, @h_javier, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a3 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con pileta en las sierras', @a3);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con pileta en las sierras - foto 1', 'datos-de-prueba/a3-1', 'https://picsum.photos/id/1043/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con pileta en las sierras - foto 2', 'datos-de-prueba/a3-2', 'https://picsum.photos/id/1044/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con pileta en las sierras - foto 3', 'datos-de-prueba/a3-3', 'https://picsum.photos/id/1045/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio = 'Pileta';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio = 'Parrilla';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio = 'Aire acondicionado';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio = 'Estacionamiento';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio = 'Lavarropas';

-- Loft entre viñedos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Ruta Provincial 15', 3200, 'Luján de Cuyo', 'Mendoza', 'Argentina', '5507', -33.036, -68.879);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Loft entre viñedos', 'Loft de diseño rodeado de viñedos, con vista a la cordillera.\nIncluye degustación de vinos en la bodega vecina.', 55000, 2, 1, 1, 1, 1, @h_javier, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Loft'));
SET @a4 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Loft entre viñedos', @a4);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft entre viñedos - foto 1', 'datos-de-prueba/a4-1', 'https://picsum.photos/id/1050/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft entre viñedos - foto 2', 'datos-de-prueba/a4-2', 'https://picsum.photos/id/1053/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft entre viñedos - foto 3', 'datos-de-prueba/a4-3', 'https://picsum.photos/id/1057/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a4, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a4, id_servicio FROM servicio WHERE nombre_servicio = 'Aire acondicionado';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a4, id_servicio FROM servicio WHERE nombre_servicio = 'Ropa de cama y toallas';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a4, id_servicio FROM servicio WHERE nombre_servicio = 'Estacionamiento';

-- Monoambiente luminoso en Palermo
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Honduras', 4700, 'Ciudad Autónoma de Buenos Aires', 'Buenos Aires', 'Argentina', '1414', -34.588, -58.429);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Monoambiente luminoso en Palermo', 'Monoambiente moderno en Palermo Soho, cerca de bares, restaurantes y el subte.\nPerfecto para viajes de trabajo.', 32000, 2, 1, 1, 1, 1, @h_sofia, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Monoambiente'));
SET @a5 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Monoambiente luminoso en Palermo', @a5);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente luminoso en Palermo - foto 1', 'datos-de-prueba/a5-1', 'https://picsum.photos/id/1031/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente luminoso en Palermo - foto 2', 'datos-de-prueba/a5-2', 'https://picsum.photos/id/1047/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente luminoso en Palermo - foto 3', 'datos-de-prueba/a5-3', 'https://picsum.photos/id/274/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a5, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a5, id_servicio FROM servicio WHERE nombre_servicio = 'Aire acondicionado';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a5, id_servicio FROM servicio WHERE nombre_servicio = 'Televisor smart';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a5, id_servicio FROM servicio WHERE nombre_servicio = 'Lavarropas';

-- Departamento colonial en el centro de Salta
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Caseros', 650, 'Salta', 'Salta', 'Argentina', '4400', -24.788, -65.406);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Departamento colonial en el centro de Salta', 'Departamento en casona colonial restaurada, a dos cuadras de la plaza 9 de Julio.\nPatio interno y desayuno regional.', 38000, 3, 2, 1, 1, 1, @h_sofia, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a6 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento colonial en el centro de Salta', @a6);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento colonial en el centro de Salta - foto 1', 'datos-de-prueba/a6-1', 'https://picsum.photos/id/164/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento colonial en el centro de Salta - foto 2', 'datos-de-prueba/a6-2', 'https://picsum.photos/id/188/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento colonial en el centro de Salta - foto 3', 'datos-de-prueba/a6-3', 'https://picsum.photos/id/240/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a6, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a6, id_servicio FROM servicio WHERE nombre_servicio = 'Calefacción';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a6, id_servicio FROM servicio WHERE nombre_servicio = 'Cocina equipada';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a6, id_servicio FROM servicio WHERE nombre_servicio = 'Ropa de cama y toallas';

-- Casa de campo con parrilla
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Camino a la Movediza', 1800, 'Tandil', 'Buenos Aires', 'Argentina', '7000', -37.309, -59.155);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Casa de campo con parrilla', 'Casa de campo en las afueras de Tandil, con parque, parrilla y vista a las sierras.\nIdeal para desconectar el fin de semana.', 60000, 6, 4, 3, 2, 1, @h_javier, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a7 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa de campo con parrilla', @a7);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa de campo con parrilla - foto 1', 'datos-de-prueba/a7-1', 'https://picsum.photos/id/1029/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa de campo con parrilla - foto 2', 'datos-de-prueba/a7-2', 'https://picsum.photos/id/1067/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa de campo con parrilla - foto 3', 'datos-de-prueba/a7-3', 'https://picsum.photos/id/1059/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a7, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a7, id_servicio FROM servicio WHERE nombre_servicio = 'Parrilla';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a7, id_servicio FROM servicio WHERE nombre_servicio = 'Calefacción';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a7, id_servicio FROM servicio WHERE nombre_servicio = 'Estacionamiento';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a7, id_servicio FROM servicio WHERE nombre_servicio = 'Admite mascotas';

-- Departamento en San Telmo (pausado)
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal, latitud, longitud) VALUES ('Defensa', 900, 'Ciudad Autónoma de Buenos Aires', 'Buenos Aires', 'Argentina', '1065', -34.619, -58.372);
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento)
VALUES ('Departamento en San Telmo (pausado)', 'Departamento de estilo antiguo en San Telmo. Publicación pausada por el anfitrion:\nno aparece en el buscador (sirve para probar alojamientos inactivos).', 29000, 2, 2, 1, 1, 0, @h_sofia, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a8 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento en San Telmo (pausado)', @a8);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento en San Telmo (pausado) - foto 1', 'datos-de-prueba/a8-1', 'https://picsum.photos/id/1076/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento en San Telmo (pausado) - foto 2', 'datos-de-prueba/a8-2', 'https://picsum.photos/id/1078/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento en San Telmo (pausado) - foto 3', 'datos-de-prueba/a8-3', 'https://picsum.photos/id/1081/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a8, id_servicio FROM servicio WHERE nombre_servicio = 'Wifi';
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a8, id_servicio FROM servicio WHERE nombre_servicio = 'Calefacción';

-- ---------------------------------------------------------------- reservas, pagos y reseñas
-- Montos como los calcula el backend: monto base = noches x precio; el cliente
-- paga ademas 5% de comision de la plataforma y 6% estimado de MercadoPago.

-- R1. Finalizada, pagada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -75 DAY), @n = 4, @p = 45000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -82 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a1, @c_lucia);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -82 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -80 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -80 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -70 DAY), '09:00:00'), 'prueba-pago-1', 'prueba-payout-1', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente ubicación, a pasos de la playa. Martina fue super atenta.', TIMESTAMP(DATE_ADD(DATE_ADD(@ini, INTERVAL @n DAY), INTERVAL 1 DAY), '18:00:00'));

-- R2. Finalizada, pagada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -60 DAY), @n = 5, @p = 68000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -68 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a2, @c_tomas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -68 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -66 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -66 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -54 DAY), '09:00:00'), 'prueba-pago-2', 'prueba-payout-2', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'La cabaña es hermosa y la vista al lago increíble. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(DATE_ADD(@ini, INTERVAL @n DAY), INTERVAL 1 DAY), '18:00:00'));

-- R3. Finalizada, pagada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -45 DAY), @n = 3, @p = 82000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -52 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a3, @c_camila);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -52 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -50 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -50 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -41 DAY), '09:00:00'), 'prueba-pago-3', 'prueba-payout-3', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy linda casa y la pileta impecable. El wifi andaba un poco lento.', TIMESTAMP(DATE_ADD(DATE_ADD(@ini, INTERVAL @n DAY), INTERVAL 1 DAY), '18:00:00'));

-- R4. Finalizada, pagada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -30 DAY), @n = 2, @p = 32000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -37 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a5, @c_nicolas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -37 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -35 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -35 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -27 DAY), '09:00:00'), 'prueba-pago-4', 'prueba-payout-4', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Cómodo y bien ubicado para moverse por la ciudad.', TIMESTAMP(DATE_ADD(DATE_ADD(@ini, INTERVAL @n DAY), INTERVAL 1 DAY), '18:00:00'));

-- R5. Finalizada, pagada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -20 DAY), @n = 3, @p = 38000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -26 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a6, @c_lucia);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -26 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -24 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -24 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -16 DAY), '09:00:00'), 'prueba-pago-5', 'prueba-payout-5', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'La casona es preciosa y el desayuno regional, lo mejor.', TIMESTAMP(DATE_ADD(DATE_ADD(@ini, INTERVAL @n DAY), INTERVAL 1 DAY), '18:00:00'));

-- R6. Finalizada SIN reseña: Tomas puede dejarla desde el detalle
SET @ini = DATE_ADD(CURDATE(), INTERVAL -12 DAY), @n = 2, @p = 55000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -18 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a4, @c_tomas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -18 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -16 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -16 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '09:00:00'), 'prueba-pago-6', 'prueba-payout-6', NULL, NULL, @base);

-- R7. Confirmada y pagada, la estadia ya termino: el anfitrion puede finalizarla
SET @ini = DATE_ADD(CURDATE(), INTERVAL -5 DAY), @n = 3, @p = 60000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a7, @c_camila);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '15:30:00'), NULL, 'prueba-pago-7', NULL, NULL, NULL, NULL);

-- R8. Confirmada y pagada, estadia futura (aparece en cobros pendientes)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 15 DAY), @n = 5, @p = 45000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a1, @c_nicolas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '15:30:00'), NULL, 'prueba-pago-8', NULL, NULL, NULL, NULL);

-- R9. Confirmada con pago PENDIENTE (vence en unas 42 horas: lo cancela el job de vencimiento)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 25 DAY), @n = 4, @p = 82000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -6 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a3, @c_lucia);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -6 HOUR), DATE_ADD(NOW(), INTERVAL 42 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R10. Solicitada: el anfitrion la puede confirmar o rechazar
SET @ini = DATE_ADD(CURDATE(), INTERVAL 35 DAY), @n = 6, @p = 68000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a2, @c_camila);
SET @r = LAST_INSERT_ID();

-- R11. Solicitada: el anfitrion la puede confirmar o rechazar
SET @ini = DATE_ADD(CURDATE(), INTERVAL 20 DAY), @n = 2, @p = 38000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a6, @c_tomas);
SET @r = LAST_INSERT_ID();

-- R12. Solicitada: el cliente la puede cancelar sin costo
SET @ini = DATE_ADD(CURDATE(), INTERVAL 40 DAY), @n = 3, @p = 32000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a5, @c_lucia);
SET @r = LAST_INSERT_ID();

-- R13. Cancelada porque nunca se pago (pago VENCIDO)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -50 DAY), @n = 2, @p = 55000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -58 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a4, @c_nicolas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'vencido', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -58 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -56 DAY), '10:00:00'), NULL, NULL, NULL, NULL, 'vencimiento_pago', NULL, NULL);

-- R14. Cancelada por el cliente con anticipacion (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 50 DAY), @n = 3, @p = 60000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a7, @c_lucia);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '15:30:00'), NULL, 'prueba-pago-14', NULL, 'cliente_anticipada', @base, 0);

-- R15. Cancelada por el cliente a ultimo momento (65% reembolso, 35% al anfitrion)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -8 DAY), @n = 3, @p = 45000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -17 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a1, @c_tomas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -17 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -15 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -15 DAY), '15:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '09:00:00'), 'prueba-pago-15', 'prueba-payout-15', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R16. Cancelada por el anfitrion (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 60 DAY), @n = 2, @p = 82000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a3, @c_nicolas);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2), @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '15:30:00'), NULL, 'prueba-pago-16', NULL, 'anfitrion', @base, 0);

COMMIT;

-- Resumen de lo cargado
SELECT 'usuarios de prueba' AS dato, COUNT(*) AS cantidad FROM usuario WHERE email LIKE '%.alojame@gmail.com'
UNION ALL SELECT 'alojamientos', COUNT(*) FROM alojamiento a JOIN usuario u ON u.id_usuario = a.id_anfitrion WHERE u.email LIKE '%.alojame@gmail.com'
UNION ALL SELECT 'reservas', COUNT(*) FROM reserva r JOIN usuario u ON u.id_usuario = r.id_cliente WHERE u.email LIKE '%.alojame@gmail.com';
