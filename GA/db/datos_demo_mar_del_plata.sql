-- =====================================================================
-- AlojaME -- datos de demostracion: Mar del Plata
--
-- 150 entradas, todas ubicadas en Mar del Plata:
--   25 anfitriones + 50 clientes + 75 alojamientos (con direccion, galeria,
--   3 imagenes y servicios cada uno), repartidos en 12 barrios.
-- Ademas, para que la demo tenga movimiento: reservas en todos los estados,
-- sus pagos (con la misma cuenta que hace el backend) y resenas.
--
-- Todas las fechas son relativas al dia en que se ejecuta (CURDATE/NOW).
-- Contrasena de TODOS los usuarios: Prueba2026
-- Los emails terminan en .mdp.alojame@gmail.com (no reciben correo).
-- No crea administradores.
--
-- Uso (con el backend arrancado al menos una vez, para que existan las tablas):
--   mysql -u alojame -p alojame < datos_demo_mar_del_plata.sql
-- Se puede cargar junto con datos_de_prueba.sql (no comparten usuarios).
-- Se ejecuta UNA sola vez (todo va en una transaccion: si algo falla, no carga nada).
-- Para sacarlos: borrar_datos_demo_mar_del_plata.sql
-- =====================================================================

SET NAMES utf8mb4;
START TRANSACTION;

-- ---------------------------------------------------------------- catalogos
-- INSERT IGNORE: si ya existen (por ejemplo, cargados con catalogo_inicial.sql), se reusan.
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Cabaña');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Casa');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Casa de playa');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Casa quinta');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Chalet');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Departamento');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Dúplex');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Estudio');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Loft');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Monoambiente');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Penthouse');
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES ('Propiedad horizontal');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Acceso a la playa');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Admite mascotas');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Aire acondicionado');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Apto para niños');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Ascensor');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Balcón');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Cafetera');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Calefacción');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Centro de la ciudad');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Cochera cubierta');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Cocina equipada');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Deck');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Ducha exterior');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Escritorio para trabajar');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Estacionamiento');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Hogar a leña');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Jacuzzi');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Jardín');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Lavarropas');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Microondas');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Parrilla');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Patio');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Pileta');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Quincho');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Ropa de cama y toallas');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Streaming');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Televisor smart');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Terraza');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Vista al mar');
INSERT IGNORE INTO servicio (nombre_servicio) VALUES ('Wifi');

-- ---------------------------------------------------------------- anfitriones (25) y clientes (50)
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('hernan.aguirre', 'Hernán', 'Aguirre', 'hernan.aguirre.mdp.alojame@gmail.com', '+54 9 223 556-1001', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'hernan.aguirre.mp', DATE_SUB(NOW(), INTERVAL 362 DAY));
SET @h1 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('silvina.suarez', 'Silvina', 'Suárez', 'silvina.suarez.mdp.alojame@gmail.com', '+54 9 223 556-1002', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'silvina.suarez.mp', DATE_SUB(NOW(), INTERVAL 389 DAY));
SET @h2 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('gaston.gimenez', 'Gastón', 'Giménez', 'gaston.gimenez.mdp.alojame@gmail.com', '+54 9 223 556-1003', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'gaston.gimenez.mp', DATE_SUB(NOW(), INTERVAL 228 DAY));
SET @h3 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('lourdes.paz', 'Lourdes', 'Paz', 'lourdes.paz.mdp.alojame@gmail.com', '+54 9 223 556-1004', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'lourdes.paz.mp', DATE_SUB(NOW(), INTERVAL 294 DAY));
SET @h4 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('maximiliano.correa', 'Maximiliano', 'Correa', 'maximiliano.correa.mdp.alojame@gmail.com', '+54 9 223 556-1005', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'maximiliano.correa', DATE_SUB(NOW(), INTERVAL 301 DAY));
SET @h5 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('emiliano.zarate', 'Emiliano', 'Zárate', 'emiliano.zarate.mdp.alojame@gmail.com', '+54 9 223 556-1006', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'emiliano.zarate.mp', DATE_SUB(NOW(), INTERVAL 337 DAY));
SET @h6 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('josefina.arias', 'Josefina', 'Arias', 'josefina.arias.mdp.alojame@gmail.com', '+54 9 223 556-1007', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'josefina.arias.mp', DATE_SUB(NOW(), INTERVAL 245 DAY));
SET @h7 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('andres.sanchez', 'Andrés', 'Sánchez', 'andres.sanchez.mdp.alojame@gmail.com', '+54 9 223 556-1008', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'andres.sanchez.mp', DATE_SUB(NOW(), INTERVAL 332 DAY));
SET @h8 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('ulises.coronel', 'Ulises', 'Coronel', 'ulises.coronel.mdp.alojame@gmail.com', '+54 9 223 556-1009', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'ulises.coronel.mp', DATE_SUB(NOW(), INTERVAL 396 DAY));
SET @h9 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('sebastian.acuna', 'Sebastián', 'Acuña', 'sebastian.acuna.mdp.alojame@gmail.com', '+54 9 223 556-1010', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'sebastian.acuna.mp', DATE_SUB(NOW(), INTERVAL 403 DAY));
SET @h10 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('micaela.quiroga', 'Micaela', 'Quiroga', 'micaela.quiroga.mdp.alojame@gmail.com', '+54 9 223 556-1011', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'micaela.quiroga.mp', DATE_SUB(NOW(), INTERVAL 420 DAY));
SET @h11 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('agustina.funes', 'Agustina', 'Funes', 'agustina.funes.mdp.alojame@gmail.com', '+54 9 223 556-1012', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'agustina.funes.mp', DATE_SUB(NOW(), INTERVAL 351 DAY));
SET @h12 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('facundo.herrera', 'Facundo', 'Herrera', 'facundo.herrera.mdp.alojame@gmail.com', '+54 9 223 556-1013', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'facundo.herrera.mp', DATE_SUB(NOW(), INTERVAL 286 DAY));
SET @h13 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('rocio.gimenez', 'Rocío', 'Giménez', 'rocio.gimenez.mdp.alojame@gmail.com', '+54 9 223 556-1014', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'rocio.gimenez.mp', DATE_SUB(NOW(), INTERVAL 335 DAY));
SET @h14 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('micaela.alvarez', 'Micaela', 'Álvarez', 'micaela.alvarez.mdp.alojame@gmail.com', '+54 9 223 556-1015', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'micaela.alvarez.mp', DATE_SUB(NOW(), INTERVAL 266 DAY));
SET @h15 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('candela.morales', 'Candela', 'Morales', 'candela.morales.mdp.alojame@gmail.com', '+54 9 223 556-1016', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'candela.morales.mp', DATE_SUB(NOW(), INTERVAL 328 DAY));
SET @h16 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('sebastian.morales', 'Sebastián', 'Morales', 'sebastian.morales.mdp.alojame@gmail.com', '+54 9 223 556-1017', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'sebastian.morales.mp', DATE_SUB(NOW(), INTERVAL 406 DAY));
SET @h17 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('valentina.ojeda', 'Valentina', 'Ojeda', 'valentina.ojeda.mdp.alojame@gmail.com', '+54 9 223 556-1018', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'valentina.ojeda.mp', DATE_SUB(NOW(), INTERVAL 342 DAY));
SET @h18 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('ignacio.rios', 'Ignacio', 'Ríos', 'ignacio.rios.mdp.alojame@gmail.com', '+54 9 223 556-1019', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'ignacio.rios.mp', DATE_SUB(NOW(), INTERVAL 236 DAY));
SET @h19 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('florencia.cabrera', 'Florencia', 'Cabrera', 'florencia.cabrera.mdp.alojame@gmail.com', '+54 9 223 556-1020', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'florencia.cabrera.mp', DATE_SUB(NOW(), INTERVAL 386 DAY));
SET @h20 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('bautista.ferreyra', 'Bautista', 'Ferreyra', 'bautista.ferreyra.mdp.alojame@gmail.com', '+54 9 223 556-1021', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'bautista.ferreyra.mp', DATE_SUB(NOW(), INTERVAL 288 DAY));
SET @h21 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('rodrigo.suarez', 'Rodrigo', 'Suárez', 'rodrigo.suarez.mdp.alojame@gmail.com', '+54 9 223 556-1022', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'rodrigo.suarez.mp', DATE_SUB(NOW(), INTERVAL 347 DAY));
SET @h22 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('fernanda.escobar', 'Fernanda', 'Escobar', 'fernanda.escobar.mdp.alojame@gmail.com', '+54 9 223 556-1023', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'fernanda.escobar.mp', DATE_SUB(NOW(), INTERVAL 314 DAY));
SET @h23 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('pilar.bustos', 'Pilar', 'Bustos', 'pilar.bustos.mdp.alojame@gmail.com', '+54 9 223 556-1024', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'pilar.bustos.mp', DATE_SUB(NOW(), INTERVAL 244 DAY));
SET @h24 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('facundo.zarate', 'Facundo', 'Zárate', 'facundo.zarate.mdp.alojame@gmail.com', '+54 9 223 556-1025', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'anfitrion', 1, 1, 'facundo.zarate.mp', DATE_SUB(NOW(), INTERVAL 269 DAY));
SET @h25 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('victoria.torres', 'Victoria', 'Torres', 'victoria.torres.mdp.alojame@gmail.com', '+54 9 223 557-2026', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 341 DAY));
SET @c1 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('rocio.ortiz', 'Rocío', 'Ortiz', 'rocio.ortiz.mdp.alojame@gmail.com', '+54 9 223 557-2027', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 369 DAY));
SET @c2 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('joaquin.lucero', 'Joaquín', 'Lucero', 'joaquin.lucero.mdp.alojame@gmail.com', '+54 9 223 557-2028', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 255 DAY));
SET @c3 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('noelia.rojas', 'Noelia', 'Rojas', 'noelia.rojas.mdp.alojame@gmail.com', '+54 9 223 557-2029', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 187 DAY));
SET @c4 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('belen.vega', 'Belén', 'Vega', 'belen.vega.mdp.alojame@gmail.com', '+54 9 223 557-2030', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 265 DAY));
SET @c5 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('bruno.rojas', 'Bruno', 'Rojas', 'bruno.rojas.mdp.alojame@gmail.com', '+54 9 223 557-2031', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 384 DAY));
SET @c6 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('gonzalo.duarte', 'Gonzalo', 'Duarte', 'gonzalo.duarte.mdp.alojame@gmail.com', '+54 9 223 557-2032', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 153 DAY));
SET @c7 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('pablo.molina', 'Pablo', 'Molina', 'pablo.molina.mdp.alojame@gmail.com', '+54 9 223 557-2033', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 173 DAY));
SET @c8 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('facundo.godoy', 'Facundo', 'Godoy', 'facundo.godoy.mdp.alojame@gmail.com', '+54 9 223 557-2034', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 150 DAY));
SET @c9 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('maximiliano.blanco', 'Maximiliano', 'Blanco', 'maximiliano.blanco.mdp.alojame@gmail.com', '+54 9 223 557-2035', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 265 DAY));
SET @c10 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('andres.romero', 'Andrés', 'Romero', 'andres.romero.mdp.alojame@gmail.com', '+54 9 223 557-2036', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 298 DAY));
SET @c11 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('franco.herrera', 'Franco', 'Herrera', 'franco.herrera.mdp.alojame@gmail.com', '+54 9 223 557-2037', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 289 DAY));
SET @c12 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('lautaro.suarez', 'Lautaro', 'Suárez', 'lautaro.suarez.mdp.alojame@gmail.com', '+54 9 223 557-2038', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 388 DAY));
SET @c13 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('bautista.acuna', 'Bautista', 'Acuña', 'bautista.acuna.mdp.alojame@gmail.com', '+54 9 223 557-2039', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 364 DAY));
SET @c14 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('lourdes.romero', 'Lourdes', 'Romero', 'lourdes.romero.mdp.alojame@gmail.com', '+54 9 223 557-2040', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 171 DAY));
SET @c15 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('ignacio.toledo', 'Ignacio', 'Toledo', 'ignacio.toledo.mdp.alojame@gmail.com', '+54 9 223 557-2041', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 306 DAY));
SET @c16 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('paula.luna', 'Paula', 'Luna', 'paula.luna.mdp.alojame@gmail.com', '+54 9 223 557-2042', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 353 DAY));
SET @c17 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('belen.peralta', 'Belén', 'Peralta', 'belen.peralta.mdp.alojame@gmail.com', '+54 9 223 557-2043', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 200 DAY));
SET @c18 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('silvina.toledo', 'Silvina', 'Toledo', 'silvina.toledo.mdp.alojame@gmail.com', '+54 9 223 557-2044', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 213 DAY));
SET @c19 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('bautista.benitez', 'Bautista', 'Benítez', 'bautista.benitez.mdp.alojame@gmail.com', '+54 9 223 557-2045', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 180 DAY));
SET @c20 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('ulises.ibarra', 'Ulises', 'Ibarra', 'ulises.ibarra.mdp.alojame@gmail.com', '+54 9 223 557-2046', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 308 DAY));
SET @c21 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('candela.dominguez', 'Candela', 'Domínguez', 'candela.dominguez.mdp.alojame@gmail.com', '+54 9 223 557-2047', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 161 DAY));
SET @c22 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('noelia.toledo', 'Noelia', 'Toledo', 'noelia.toledo.mdp.alojame@gmail.com', '+54 9 223 557-2048', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 342 DAY));
SET @c23 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('lourdes.maidana', 'Lourdes', 'Maidana', 'lourdes.maidana.mdp.alojame@gmail.com', '+54 9 223 557-2049', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 289 DAY));
SET @c24 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('gabriela.rojas', 'Gabriela', 'Rojas', 'gabriela.rojas.mdp.alojame@gmail.com', '+54 9 223 557-2050', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 394 DAY));
SET @c25 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('emiliano.molina', 'Emiliano', 'Molina', 'emiliano.molina.mdp.alojame@gmail.com', '+54 9 223 557-2051', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 218 DAY));
SET @c26 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('romina.juarez', 'Romina', 'Juárez', 'romina.juarez.mdp.alojame@gmail.com', '+54 9 223 557-2052', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 321 DAY));
SET @c27 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('estefania.herrera', 'Estefanía', 'Herrera', 'estefania.herrera.mdp.alojame@gmail.com', '+54 9 223 557-2053', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 343 DAY));
SET @c28 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('fernanda.ponce', 'Fernanda', 'Ponce', 'fernanda.ponce.mdp.alojame@gmail.com', '+54 9 223 557-2054', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 312 DAY));
SET @c29 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('pablo.morales', 'Pablo', 'Morales', 'pablo.morales.mdp.alojame@gmail.com', '+54 9 223 557-2055', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 241 DAY));
SET @c30 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('romina.rojas', 'Romina', 'Rojas', 'romina.rojas.mdp.alojame@gmail.com', '+54 9 223 557-2056', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 189 DAY));
SET @c31 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('valentin.vazquez', 'Valentín', 'Vázquez', 'valentin.vazquez.mdp.alojame@gmail.com', '+54 9 223 557-2057', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 151 DAY));
SET @c32 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('paula.zarate', 'Paula', 'Zárate', 'paula.zarate.mdp.alojame@gmail.com', '+54 9 223 557-2058', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 230 DAY));
SET @c33 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('ignacio.maidana', 'Ignacio', 'Maidana', 'ignacio.maidana.mdp.alojame@gmail.com', '+54 9 223 557-2059', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 320 DAY));
SET @c34 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('santiago.zarate', 'Santiago', 'Zárate', 'santiago.zarate.mdp.alojame@gmail.com', '+54 9 223 557-2060', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 299 DAY));
SET @c35 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('bautista.lucero', 'Bautista', 'Lucero', 'bautista.lucero.mdp.alojame@gmail.com', '+54 9 223 557-2061', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 297 DAY));
SET @c36 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('silvina.ponce', 'Silvina', 'Ponce', 'silvina.ponce.mdp.alojame@gmail.com', '+54 9 223 557-2062', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 346 DAY));
SET @c37 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('romina.godoy', 'Romina', 'Godoy', 'romina.godoy.mdp.alojame@gmail.com', '+54 9 223 557-2063', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 387 DAY));
SET @c38 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('lautaro.navarro', 'Lautaro', 'Navarro', 'lautaro.navarro.mdp.alojame@gmail.com', '+54 9 223 557-2064', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 393 DAY));
SET @c39 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('rocio.sosa', 'Rocío', 'Sosa', 'rocio.sosa.mdp.alojame@gmail.com', '+54 9 223 557-2065', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 370 DAY));
SET @c40 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('agustina.castro', 'Agustina', 'Castro', 'agustina.castro.mdp.alojame@gmail.com', '+54 9 223 557-2066', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 275 DAY));
SET @c41 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('lourdes.correa', 'Lourdes', 'Correa', 'lourdes.correa.mdp.alojame@gmail.com', '+54 9 223 557-2067', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 326 DAY));
SET @c42 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('pablo.toledo', 'Pablo', 'Toledo', 'pablo.toledo.mdp.alojame@gmail.com', '+54 9 223 557-2068', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 334 DAY));
SET @c43 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('valentin.romero', 'Valentín', 'Romero', 'valentin.romero.mdp.alojame@gmail.com', '+54 9 223 557-2069', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 390 DAY));
SET @c44 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('gonzalo.navarro', 'Gonzalo', 'Navarro', 'gonzalo.navarro.mdp.alojame@gmail.com', '+54 9 223 557-2070', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 184 DAY));
SET @c45 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('paula.ponce', 'Paula', 'Ponce', 'paula.ponce.mdp.alojame@gmail.com', '+54 9 223 557-2071', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 289 DAY));
SET @c46 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('sebastian.ortiz', 'Sebastián', 'Ortiz', 'sebastian.ortiz.mdp.alojame@gmail.com', '+54 9 223 557-2072', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 207 DAY));
SET @c47 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('silvina.coronel', 'Silvina', 'Coronel', 'silvina.coronel.mdp.alojame@gmail.com', '+54 9 223 557-2073', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 1, NULL, DATE_SUB(NOW(), INTERVAL 271 DAY));
SET @c48 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('candela.romero', 'Candela', 'Romero', 'candela.romero.mdp.alojame@gmail.com', '+54 9 223 557-2074', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 0, 1, NULL, DATE_SUB(NOW(), INTERVAL 300 DAY));  -- cuenta desactivada por el admin (no puede iniciar sesion)
SET @c49 = LAST_INSERT_ID();
INSERT INTO usuario (usuario_login, nombre, apellido, email, telefono, password, tipo, activo, email_verificado, alias_cobro, fecha_registro) VALUES ('bautista.molina', 'Bautista', 'Molina', 'bautista.molina.mdp.alojame@gmail.com', '+54 9 223 557-2075', '$2a$10$vftxrAflAzQVgNcHBJeEk.F3VGiP46kedU0xnWgNtMfpnCqjYx6om', 'cliente', 1, 0, NULL, DATE_SUB(NOW(), INTERVAL 183 DAY));  -- registrado sin verificar el email (no puede iniciar sesion)
SET @c50 = LAST_INSERT_ID();

-- ---------------------------------------------------------------- alojamientos (75)
-- Casa con parrilla en Los Troncos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Garay', 3192, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con parrilla en Los Troncos', 'Casa de 4 ambientes, en Los Troncos, el barrio de chalets de piedra.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 129500, 5, 4, 3, 2, 1, @h1, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a1 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con parrilla en Los Troncos', @a1);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Los Troncos - foto 1', 'datos-demo-mdp/a1-1', 'https://picsum.photos/id/1011/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Los Troncos - foto 2', 'datos-demo-mdp/a1-2', 'https://picsum.photos/id/1013/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Los Troncos - foto 3', 'datos-demo-mdp/a1-3', 'https://picsum.photos/id/1015/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a1, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Hogar a leña', 'Parrilla', 'Lavarropas', 'Apto para niños', 'Estacionamiento', 'Admite mascotas');

-- Departamento luminoso en el Centro
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Santiago del Estero', 1758, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en el Centro', 'Departamento de 4 ambientes, a cuatro cuadras de la Rambla y el Casino.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 59500, 6, 4, 3, 2, 1, @h2, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a2 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en el Centro', @a2);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Centro - foto 1', 'datos-demo-mdp/a2-1', 'https://picsum.photos/id/1016/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Centro - foto 2', 'datos-demo-mdp/a2-2', 'https://picsum.photos/id/1018/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Centro - foto 3', 'datos-demo-mdp/a2-3', 'https://picsum.photos/id/1019/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a2, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Televisor smart');

-- Casa familiar en Constitución
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Félix U. Camet', 4048, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa familiar en Constitución', 'Casa de 6 ambientes, a cuatro cuadras de las playas del norte.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 134500, 8, 6, 4, 3, 1, @h3, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a3 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa familiar en Constitución', @a3);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa familiar en Constitución - foto 1', 'datos-demo-mdp/a3-1', 'https://picsum.photos/id/1020/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa familiar en Constitución - foto 2', 'datos-demo-mdp/a3-2', 'https://picsum.photos/id/1021/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa familiar en Constitución - foto 3', 'datos-demo-mdp/a3-3', 'https://picsum.photos/id/1022/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a3, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Pileta', 'Jardín', 'Lavarropas', 'Apto para niños', 'Admite mascotas', 'Televisor smart', 'Estacionamiento');

-- Casa con parrilla en Stella Maris
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Gascón', 1962, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con parrilla en Stella Maris', 'Casa de 6 ambientes, con vista a la bahía de Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 112000, 8, 6, 4, 3, 1, @h4, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a4 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con parrilla en Stella Maris', @a4);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Stella Maris - foto 1', 'datos-demo-mdp/a4-1', 'https://picsum.photos/id/1023/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Stella Maris - foto 2', 'datos-demo-mdp/a4-2', 'https://picsum.photos/id/1024/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Stella Maris - foto 3', 'datos-demo-mdp/a4-3', 'https://picsum.photos/id/1025/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a4, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Terraza', 'Vista al mar', 'Lavarropas', 'Apto para niños', 'Aire acondicionado', 'Admite mascotas');

-- Departamento luminoso en el Centro, Córdoba
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Córdoba', 2062, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en el Centro, Córdoba', 'Departamento de 2 ambientes, a dos cuadras de la peatonal San Martín.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 45000, 2, 2, 1, 1, 1, @h5, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a5 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en el Centro, Córdoba', @a5);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Centro, Córdoba - foto 1', 'datos-demo-mdp/a5-1', 'https://picsum.photos/id/1026/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Centro, Córdoba - foto 2', 'datos-demo-mdp/a5-2', 'https://picsum.photos/id/1027/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Centro, Córdoba - foto 3', 'datos-demo-mdp/a5-3', 'https://picsum.photos/id/1028/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a5, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Ascensor', 'Centro de la ciudad', 'Streaming', 'Aire acondicionado');

-- Dúplex con patio en Punta Mogotes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Martínez de Hoz', 4054, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex con patio en Punta Mogotes', 'Dúplex de 3 ambientes, a metros de los balnearios de Punta Mogotes.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 82000, 4, 3, 2, 2, 1, @h6, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a6 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex con patio en Punta Mogotes', @a6);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Punta Mogotes - foto 1', 'datos-demo-mdp/a6-1', 'https://picsum.photos/id/1029/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Punta Mogotes - foto 2', 'datos-demo-mdp/a6-2', 'https://picsum.photos/id/1031/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Punta Mogotes - foto 3', 'datos-demo-mdp/a6-3', 'https://picsum.photos/id/1032/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a6, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Televisor smart');

-- Monoambiente práctico en La Perla
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Hipólito Yrigoyen', 1258, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Monoambiente práctico en La Perla', 'Monoambiente de 1 ambiente, cerca del Paseo Hermitage y la Rambla.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 30000, 2, 1, 1, 1, 1, @h7, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Monoambiente'));
SET @a7 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Monoambiente práctico en La Perla', @a7);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente práctico en La Perla - foto 1', 'datos-demo-mdp/a7-1', 'https://picsum.photos/id/1033/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente práctico en La Perla - foto 2', 'datos-demo-mdp/a7-2', 'https://picsum.photos/id/1035/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente práctico en La Perla - foto 3', 'datos-demo-mdp/a7-3', 'https://picsum.photos/id/1036/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a7, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Vista al mar', 'Escritorio para trabajar', 'Streaming', 'Estacionamiento');

-- Casa con parrilla en Constitución
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Constitución', 5958, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con parrilla en Constitución', 'Casa de 5 ambientes, cerca de la Av. Constitución y sus comercios.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 135500, 6, 5, 3, 2, 1, @h8, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a8 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con parrilla en Constitución', @a8);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Constitución - foto 1', 'datos-demo-mdp/a8-1', 'https://picsum.photos/id/1037/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Constitución - foto 2', 'datos-demo-mdp/a8-2', 'https://picsum.photos/id/1038/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Constitución - foto 3', 'datos-demo-mdp/a8-3', 'https://picsum.photos/id/1039/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a8, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Jardín', 'Parrilla', 'Lavarropas', 'Apto para niños', 'Cafetera', 'Televisor smart');

-- Casa a metros del mar en Constitución
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Constitución', 5766, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa a metros del mar en Constitución', 'Casa de playa de 5 ambientes, a minutos de Playa Chica y del Paseo Costanero Norte.\nPensado para familias o grupos grandes. Capacidad para 7 personas.', 128500, 7, 5, 3, 2, 1, @h9, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa de playa'));
SET @a9 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa a metros del mar en Constitución', @a9);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa a metros del mar en Constitución - foto 1', 'datos-demo-mdp/a9-1', 'https://picsum.photos/id/1040/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa a metros del mar en Constitución - foto 2', 'datos-demo-mdp/a9-2', 'https://picsum.photos/id/1041/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa a metros del mar en Constitución - foto 3', 'datos-demo-mdp/a9-3', 'https://picsum.photos/id/1042/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a9, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Apto para niños', 'Admite mascotas', 'Estacionamiento');

-- Departamento con balcón en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Aristóbulo del Valle', 2950, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento con balcón en Playa Grande', 'Departamento de 3 ambientes, a una cuadra de Playa Grande.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 45500, 4, 3, 2, 1, 1, @h10, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a10 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento con balcón en Playa Grande', @a10);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Playa Grande - foto 1', 'datos-demo-mdp/a10-1', 'https://picsum.photos/id/1043/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Playa Grande - foto 2', 'datos-demo-mdp/a10-2', 'https://picsum.photos/id/1044/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Playa Grande - foto 3', 'datos-demo-mdp/a10-3', 'https://picsum.photos/id/1045/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a10, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Cochera cubierta', 'Vista al mar', 'Lavarropas', 'Microondas');

-- Dúplex familiar en Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Güemes', 3032, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex familiar en Güemes', 'Dúplex de 4 ambientes, en plena zona de Güemes, con locales, cafés y restaurantes.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 90000, 6, 4, 3, 2, 1, @h11, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a11 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex familiar en Güemes', @a11);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Güemes - foto 1', 'datos-demo-mdp/a11-1', 'https://picsum.photos/id/1047/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Güemes - foto 2', 'datos-demo-mdp/a11-2', 'https://picsum.photos/id/1048/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Güemes - foto 3', 'datos-demo-mdp/a11-3', 'https://picsum.photos/id/1049/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a11, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Terraza', 'Balcón', 'Admite mascotas');

-- Cabaña con hogar a leña en el Bosque Peralta Ramos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 5064, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Cabaña con hogar a leña en el Bosque Peralta Ramos', 'Cabaña de 3 ambientes, en el Bosque Peralta Ramos, rodeado de pinos.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 56000, 5, 3, 2, 2, 1, @h12, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Cabaña'));
SET @a12 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Cabaña con hogar a leña en el Bosque Peralta Ramos', @a12);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Cabaña con hogar a leña en el Bosque Peralta Ramos - foto 1', 'datos-demo-mdp/a12-1', 'https://picsum.photos/id/1050/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Cabaña con hogar a leña en el Bosque Peralta Ramos - foto 2', 'datos-demo-mdp/a12-2', 'https://picsum.photos/id/1051/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Cabaña con hogar a leña en el Bosque Peralta Ramos - foto 3', 'datos-demo-mdp/a12-3', 'https://picsum.photos/id/1052/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a12, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Admite mascotas', 'Hogar a leña', 'Parrilla', 'Deck', 'Televisor smart', 'Streaming', 'Estacionamiento');

-- Loft con doble altura en Plaza Mitre
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('San Luis', 2420, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Loft con doble altura en Plaza Mitre', 'Loft de 2 ambientes, frente a la Plaza Mitre.\nCómodo para una pareja con un hijo o dos amigos. Capacidad para 3 personas.', 53000, 3, 2, 1, 1, 1, @h13, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Loft'));
SET @a13 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Loft con doble altura en Plaza Mitre', @a13);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft con doble altura en Plaza Mitre - foto 1', 'datos-demo-mdp/a13-1', 'https://picsum.photos/id/1053/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft con doble altura en Plaza Mitre - foto 2', 'datos-demo-mdp/a13-2', 'https://picsum.photos/id/1054/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft con doble altura en Plaza Mitre - foto 3', 'datos-demo-mdp/a13-3', 'https://picsum.photos/id/1055/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a13, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Centro de la ciudad', 'Balcón', 'Escritorio para trabajar', 'Lavarropas', 'Streaming', 'Admite mascotas');

-- Departamento reciclado en el Centro
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Córdoba', 2152, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento reciclado en el Centro', 'Departamento de 3 ambientes, a cuatro cuadras de la Rambla y el Casino.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 53000, 5, 3, 2, 2, 1, @h14, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a14 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento reciclado en el Centro', @a14);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en el Centro - foto 1', 'datos-demo-mdp/a14-1', 'https://picsum.photos/id/1056/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en el Centro - foto 2', 'datos-demo-mdp/a14-2', 'https://picsum.photos/id/1057/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en el Centro - foto 3', 'datos-demo-mdp/a14-3', 'https://picsum.photos/id/1058/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a14, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Escritorio para trabajar', 'Estacionamiento', 'Aire acondicionado');

-- Departamento reciclado en Stella Maris
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Patricio Peralta Ramos', 3420, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento reciclado en Stella Maris', 'Departamento de 4 ambientes, con vista a la bahía de Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 79000, 6, 4, 3, 2, 1, @h15, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a15 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento reciclado en Stella Maris', @a15);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en Stella Maris - foto 1', 'datos-demo-mdp/a15-1', 'https://picsum.photos/id/1059/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en Stella Maris - foto 2', 'datos-demo-mdp/a15-2', 'https://picsum.photos/id/1060/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en Stella Maris - foto 3', 'datos-demo-mdp/a15-3', 'https://picsum.photos/id/1061/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a15, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Vista al mar', 'Acceso a la playa', 'Estacionamiento');

-- Chalet de piedra en Los Troncos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alvear', 3450, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Chalet de piedra en Los Troncos', 'Chalet de 6 ambientes, cerca de la Villa Victoria Ocampo.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 166500, 8, 6, 4, 3, 1, @h16, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Chalet'));
SET @a16 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Chalet de piedra en Los Troncos', @a16);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en Los Troncos - foto 1', 'datos-demo-mdp/a16-1', 'https://picsum.photos/id/1062/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en Los Troncos - foto 2', 'datos-demo-mdp/a16-2', 'https://picsum.photos/id/1063/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en Los Troncos - foto 3', 'datos-demo-mdp/a16-3', 'https://picsum.photos/id/1064/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a16, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Hogar a leña', 'Jardín', 'Televisor smart', 'Admite mascotas', 'Cafetera');

-- Estudio moderno en el Centro
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Belgrano', 2330, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Estudio moderno en el Centro', 'Estudio de 1 ambiente, cerca de la Catedral y de la Plaza San Martín.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 38500, 2, 1, 1, 1, 1, @h17, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Estudio'));
SET @a17 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Estudio moderno en el Centro', @a17);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Estudio moderno en el Centro - foto 1', 'datos-demo-mdp/a17-1', 'https://picsum.photos/id/1065/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Estudio moderno en el Centro - foto 2', 'datos-demo-mdp/a17-2', 'https://picsum.photos/id/1066/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Estudio moderno en el Centro - foto 3', 'datos-demo-mdp/a17-3', 'https://picsum.photos/id/1067/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a17, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Balcón', 'Estacionamiento', 'Televisor smart');

-- Departamento luminoso en Punta Mogotes  (pausado)
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Martínez de Hoz', 3744, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en Punta Mogotes', 'Departamento de 3 ambientes, a diez minutos del Faro de Punta Mogotes.\nPensado para familias o grupos chicos. Capacidad para 4 personas.\nPublicación pausada por el anfitrión: no aparece en el buscador.', 77000, 4, 3, 2, 1, 0, @h18, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a18 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en Punta Mogotes', @a18);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Punta Mogotes - foto 1', 'datos-demo-mdp/a18-1', 'https://picsum.photos/id/1068/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Punta Mogotes - foto 2', 'datos-demo-mdp/a18-2', 'https://picsum.photos/id/1069/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Punta Mogotes - foto 3', 'datos-demo-mdp/a18-3', 'https://picsum.photos/id/1070/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a18, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Parrilla', 'Estacionamiento', 'Televisor smart');

-- Chalet de piedra en el Bosque Peralta Ramos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 4918, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Chalet de piedra en el Bosque Peralta Ramos', 'Chalet de 6 ambientes, en un barrio tranquilo con mucho verde.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 180000, 8, 6, 4, 3, 1, @h19, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Chalet'));
SET @a19 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Chalet de piedra en el Bosque Peralta Ramos', @a19);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en el Bosque Peralta Ramos - foto 1', 'datos-demo-mdp/a19-1', 'https://picsum.photos/id/1071/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en el Bosque Peralta Ramos - foto 2', 'datos-demo-mdp/a19-2', 'https://picsum.photos/id/1072/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en el Bosque Peralta Ramos - foto 3', 'datos-demo-mdp/a19-3', 'https://picsum.photos/id/1073/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a19, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Hogar a leña', 'Parrilla', 'Jardín', 'Lavarropas', 'Aire acondicionado', 'Televisor smart');

-- Departamento reciclado en La Perla
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Rawson', 1044, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento reciclado en La Perla', 'Departamento de 2 ambientes, a tres cuadras de las playas de La Perla.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 72000, 2, 2, 1, 1, 1, @h20, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a20 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento reciclado en La Perla', @a20);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en La Perla - foto 1', 'datos-demo-mdp/a20-1', 'https://picsum.photos/id/1074/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en La Perla - foto 2', 'datos-demo-mdp/a20-2', 'https://picsum.photos/id/1075/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento reciclado en La Perla - foto 3', 'datos-demo-mdp/a20-3', 'https://picsum.photos/id/1076/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a20, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Vista al mar', 'Acceso a la playa', 'Balcón', 'Streaming');

-- Dúplex con patio en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alem', 3238, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex con patio en Playa Grande', 'Dúplex de 3 ambientes, con vista a la escollera de Playa Grande.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 72000, 4, 3, 2, 2, 1, @h21, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a21 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex con patio en Playa Grande', @a21);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Playa Grande - foto 1', 'datos-demo-mdp/a21-1', 'https://picsum.photos/id/1077/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Playa Grande - foto 2', 'datos-demo-mdp/a21-2', 'https://picsum.photos/id/1078/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Playa Grande - foto 3', 'datos-demo-mdp/a21-3', 'https://picsum.photos/id/1079/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a21, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Microondas', 'Televisor smart');

-- Casa con jardín en Stella Maris, Falucho
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Falucho', 1936, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con jardín en Stella Maris, Falucho', 'Casa de 4 ambientes, frente a Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 143000, 5, 4, 3, 2, 1, @h22, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a22 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con jardín en Stella Maris, Falucho', @a22);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris, Falucho - foto 1', 'datos-demo-mdp/a22-1', 'https://picsum.photos/id/1080/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris, Falucho - foto 2', 'datos-demo-mdp/a22-2', 'https://picsum.photos/id/1081/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris, Falucho - foto 3', 'datos-demo-mdp/a22-3', 'https://picsum.photos/id/1082/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a22, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Lavarropas', 'Apto para niños', 'Estacionamiento', 'Cafetera');

-- Chalet con jardín en el Bosque Peralta Ramos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 5108, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Chalet con jardín en el Bosque Peralta Ramos', 'Chalet de 5 ambientes, a diez minutos de las playas del sur, entre árboles.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 124500, 6, 5, 3, 2, 1, @h23, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Chalet'));
SET @a23 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Chalet con jardín en el Bosque Peralta Ramos', @a23);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet con jardín en el Bosque Peralta Ramos - foto 1', 'datos-demo-mdp/a23-1', 'https://picsum.photos/id/1083/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet con jardín en el Bosque Peralta Ramos - foto 2', 'datos-demo-mdp/a23-2', 'https://picsum.photos/id/1084/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet con jardín en el Bosque Peralta Ramos - foto 3', 'datos-demo-mdp/a23-3', 'https://picsum.photos/id/164/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a23, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Quincho', 'Pileta', 'Hogar a leña', 'Jardín', 'Streaming', 'Aire acondicionado');

-- Departamento luminoso en el Puerto
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Juan B. Justo', 646, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en el Puerto', 'Departamento de 3 ambientes, a pocas cuadras de la reserva de lobos marinos.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 68500, 4, 3, 2, 1, 1, @h24, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a24 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en el Puerto', @a24);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Puerto - foto 1', 'datos-demo-mdp/a24-1', 'https://picsum.photos/id/188/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Puerto - foto 2', 'datos-demo-mdp/a24-2', 'https://picsum.photos/id/240/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en el Puerto - foto 3', 'datos-demo-mdp/a24-3', 'https://picsum.photos/id/274/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a24, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Lavarropas', 'Admite mascotas');

-- Departamento con balcón en el Centro, Córdoba
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Córdoba', 1776, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento con balcón en el Centro, Córdoba', 'Departamento de 2 ambientes, cerca de la Catedral y de la Plaza San Martín.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 59500, 2, 2, 1, 1, 1, @h25, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a25 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento con balcón en el Centro, Córdoba', @a25);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en el Centro, Córdoba - foto 1', 'datos-demo-mdp/a25-1', 'https://picsum.photos/id/279/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en el Centro, Córdoba - foto 2', 'datos-demo-mdp/a25-2', 'https://picsum.photos/id/299/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en el Centro, Córdoba - foto 3', 'datos-demo-mdp/a25-3', 'https://picsum.photos/id/306/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a25, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Centro de la ciudad', 'Ascensor', 'Aire acondicionado');

-- Departamento luminoso en Chauvín
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Catamarca', 2910, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en Chauvín', 'Departamento de 4 ambientes, a cinco cuadras de la Plaza Mitre.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 57000, 6, 4, 3, 2, 1, @h1, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a26 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en Chauvín', @a26);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Chauvín - foto 1', 'datos-demo-mdp/a26-1', 'https://picsum.photos/id/308/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Chauvín - foto 2', 'datos-demo-mdp/a26-2', 'https://picsum.photos/id/311/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Chauvín - foto 3', 'datos-demo-mdp/a26-3', 'https://picsum.photos/id/315/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a26, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Lavarropas', 'Parrilla', 'Aire acondicionado', 'Streaming');

-- Dúplex familiar en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alem', 3354, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex familiar en Playa Grande', 'Dúplex de 3 ambientes, a pasos de la calle Alem y sus bares.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 109500, 4, 3, 2, 2, 1, @h2, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a27 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex familiar en Playa Grande', @a27);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Playa Grande - foto 1', 'datos-demo-mdp/a27-1', 'https://picsum.photos/id/318/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Playa Grande - foto 2', 'datos-demo-mdp/a27-2', 'https://picsum.photos/id/326/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Playa Grande - foto 3', 'datos-demo-mdp/a27-3', 'https://picsum.photos/id/337/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a27, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Vista al mar', 'Cochera cubierta', 'Estacionamiento', 'Aire acondicionado', 'Lavarropas');

-- Casa con jardín en Chauvín
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Catamarca', 2830, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con jardín en Chauvín', 'Casa de 5 ambientes, en Chauvín, a pocas cuadras del centro.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 129500, 6, 5, 3, 2, 1, @h3, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a28 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con jardín en Chauvín', @a28);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Chauvín - foto 1', 'datos-demo-mdp/a28-1', 'https://picsum.photos/id/342/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Chauvín - foto 2', 'datos-demo-mdp/a28-2', 'https://picsum.photos/id/349/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Chauvín - foto 3', 'datos-demo-mdp/a28-3', 'https://picsum.photos/id/362/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a28, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Patio', 'Parrilla', 'Lavarropas', 'Apto para niños', 'Admite mascotas');

-- Dúplex moderno en Punta Mogotes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 358, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex moderno en Punta Mogotes', 'Dúplex de 4 ambientes, a metros de los balnearios de Punta Mogotes.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 98500, 6, 4, 3, 2, 1, @h4, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a29 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex moderno en Punta Mogotes', @a29);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex moderno en Punta Mogotes - foto 1', 'datos-demo-mdp/a29-1', 'https://picsum.photos/id/364/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex moderno en Punta Mogotes - foto 2', 'datos-demo-mdp/a29-2', 'https://picsum.photos/id/366/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex moderno en Punta Mogotes - foto 3', 'datos-demo-mdp/a29-3', 'https://picsum.photos/id/1011/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a29, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Apto para niños', 'Estacionamiento', 'Admite mascotas', 'Microondas');

-- Casa amplia en Constitución
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Félix U. Camet', 3218, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa amplia en Constitución', 'Casa de 6 ambientes, a minutos de Playa Chica y del Paseo Costanero Norte.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 97500, 8, 6, 4, 3, 1, @h5, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a30 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa amplia en Constitución', @a30);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Constitución - foto 1', 'datos-demo-mdp/a30-1', 'https://picsum.photos/id/1013/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Constitución - foto 2', 'datos-demo-mdp/a30-2', 'https://picsum.photos/id/1015/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Constitución - foto 3', 'datos-demo-mdp/a30-3', 'https://picsum.photos/id/1016/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a30, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Apto para niños', 'Pileta', 'Parrilla', 'Lavarropas', 'Estacionamiento');

-- Dúplex con patio en Punta Mogotes, Av. Martínez de Hoz
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Martínez de Hoz', 4204, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex con patio en Punta Mogotes, Av. Martínez de Hoz', 'Dúplex de 3 ambientes, a diez minutos del Faro de Punta Mogotes.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 68000, 4, 3, 2, 2, 1, @h6, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a31 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex con patio en Punta Mogotes, Av. Martínez de Hoz', @a31);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Punta Mogotes, Av. Martínez de Hoz - foto 1', 'datos-demo-mdp/a31-1', 'https://picsum.photos/id/1018/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Punta Mogotes, Av. Martínez de Hoz - foto 2', 'datos-demo-mdp/a31-2', 'https://picsum.photos/id/1019/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Punta Mogotes, Av. Martínez de Hoz - foto 3', 'datos-demo-mdp/a31-3', 'https://picsum.photos/id/1020/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a31, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Ducha exterior', 'Parrilla', 'Televisor smart');

-- Casa con parrilla en Chauvín
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Independencia', 3114, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con parrilla en Chauvín', 'Casa de 4 ambientes, cerca del Teatro Auditorium y la costa.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 139000, 5, 4, 3, 2, 1, @h7, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a32 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con parrilla en Chauvín', @a32);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Chauvín - foto 1', 'datos-demo-mdp/a32-1', 'https://picsum.photos/id/1021/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Chauvín - foto 2', 'datos-demo-mdp/a32-2', 'https://picsum.photos/id/1022/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con parrilla en Chauvín - foto 3', 'datos-demo-mdp/a32-3', 'https://picsum.photos/id/1023/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a32, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Parrilla', 'Lavarropas', 'Apto para niños', 'Cafetera');

-- Departamento luminoso en La Perla
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Rawson', 1550, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en La Perla', 'Departamento de 2 ambientes, a pocos metros de la costa de La Perla.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 76000, 2, 2, 1, 1, 1, @h8, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a33 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en La Perla', @a33);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en La Perla - foto 1', 'datos-demo-mdp/a33-1', 'https://picsum.photos/id/1024/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en La Perla - foto 2', 'datos-demo-mdp/a33-2', 'https://picsum.photos/id/1025/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en La Perla - foto 3', 'datos-demo-mdp/a33-3', 'https://picsum.photos/id/1026/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a33, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Balcón', 'Vista al mar', 'Aire acondicionado', 'Cafetera', 'Estacionamiento');

-- PH con patio en Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Olavarría', 2934, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('PH con patio en Güemes', 'PH de 3 ambientes, en plena zona de Güemes, con locales, cafés y restaurantes.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 61000, 4, 3, 2, 1, 1, @h9, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Propiedad horizontal'));
SET @a34 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de PH con patio en Güemes', @a34);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en Güemes - foto 1', 'datos-demo-mdp/a34-1', 'https://picsum.photos/id/1027/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en Güemes - foto 2', 'datos-demo-mdp/a34-2', 'https://picsum.photos/id/1028/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en Güemes - foto 3', 'datos-demo-mdp/a34-3', 'https://picsum.photos/id/1029/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a34, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Terraza', 'Balcón', 'Aire acondicionado', 'Estacionamiento', 'Microondas');

-- Monoambiente reciclado en el Centro
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Rivadavia', 2648, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Monoambiente reciclado en el Centro', 'Monoambiente de 1 ambiente, a dos cuadras de la peatonal San Martín.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 35500, 2, 1, 1, 1, 1, @h10, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Monoambiente'));
SET @a35 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Monoambiente reciclado en el Centro', @a35);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente reciclado en el Centro - foto 1', 'datos-demo-mdp/a35-1', 'https://picsum.photos/id/1031/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente reciclado en el Centro - foto 2', 'datos-demo-mdp/a35-2', 'https://picsum.photos/id/1032/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente reciclado en el Centro - foto 3', 'datos-demo-mdp/a35-3', 'https://picsum.photos/id/1033/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a35, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Balcón', 'Escritorio para trabajar', 'Streaming');

-- Chalet con jardín en Los Troncos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alvear', 3388, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Chalet con jardín en Los Troncos', 'Chalet de 5 ambientes, en Los Troncos, el barrio de chalets de piedra.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 146500, 6, 5, 3, 2, 1, @h11, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Chalet'));
SET @a36 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Chalet con jardín en Los Troncos', @a36);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet con jardín en Los Troncos - foto 1', 'datos-demo-mdp/a36-1', 'https://picsum.photos/id/1035/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet con jardín en Los Troncos - foto 2', 'datos-demo-mdp/a36-2', 'https://picsum.photos/id/1036/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet con jardín en Los Troncos - foto 3', 'datos-demo-mdp/a36-3', 'https://picsum.photos/id/1037/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a36, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Parrilla', 'Jardín', 'Estacionamiento', 'Hogar a leña', 'Televisor smart', 'Lavarropas', 'Cafetera');

-- Departamento familiar en el Centro
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Belgrano', 2652, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento familiar en el Centro', 'Departamento de 3 ambientes, a dos cuadras de la peatonal San Martín.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 47000, 5, 3, 2, 2, 1, @h12, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a37 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento familiar en el Centro', @a37);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en el Centro - foto 1', 'datos-demo-mdp/a37-1', 'https://picsum.photos/id/1038/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en el Centro - foto 2', 'datos-demo-mdp/a37-2', 'https://picsum.photos/id/1039/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en el Centro - foto 3', 'datos-demo-mdp/a37-3', 'https://picsum.photos/id/1040/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a37, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Centro de la ciudad', 'Lavarropas');

-- Loft de diseño en Plaza Mitre
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Colón', 2434, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Loft de diseño en Plaza Mitre', 'Loft de 2 ambientes, cerca de la Av. Colón y del Torreón del Monje.\nCómodo para una pareja con un hijo o dos amigos. Capacidad para 3 personas.', 63000, 3, 2, 1, 1, 1, @h13, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Loft'));
SET @a38 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Loft de diseño en Plaza Mitre', @a38);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft de diseño en Plaza Mitre - foto 1', 'datos-demo-mdp/a38-1', 'https://picsum.photos/id/1041/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft de diseño en Plaza Mitre - foto 2', 'datos-demo-mdp/a38-2', 'https://picsum.photos/id/1042/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft de diseño en Plaza Mitre - foto 3', 'datos-demo-mdp/a38-3', 'https://picsum.photos/id/1043/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a38, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Centro de la ciudad', 'Escritorio para trabajar', 'Cafetera', 'Aire acondicionado');

-- Chalet clásico marplatense en Los Troncos, Garay  (pausado)
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Garay', 3000, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Chalet clásico marplatense en Los Troncos, Garay', 'Chalet de 5 ambientes, cerca de la Villa Victoria Ocampo.\nPensado para familias o grupos grandes. Capacidad para 6 personas.\nPublicación pausada por el anfitrión: no aparece en el buscador.', 114000, 6, 5, 3, 2, 0, @h14, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Chalet'));
SET @a39 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Chalet clásico marplatense en Los Troncos, Garay', @a39);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet clásico marplatense en Los Troncos, Garay - foto 1', 'datos-demo-mdp/a39-1', 'https://picsum.photos/id/1044/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet clásico marplatense en Los Troncos, Garay - foto 2', 'datos-demo-mdp/a39-2', 'https://picsum.photos/id/1045/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet clásico marplatense en Los Troncos, Garay - foto 3', 'datos-demo-mdp/a39-3', 'https://picsum.photos/id/1047/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a39, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Parrilla', 'Jardín', 'Hogar a leña', 'Admite mascotas');

-- Departamento amplio en Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Güemes', 2712, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento amplio en Güemes', 'Departamento de 4 ambientes, cerca del Paseo Aldrey y de Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 65500, 6, 4, 3, 2, 1, @h15, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a40 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento amplio en Güemes', @a40);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Güemes - foto 1', 'datos-demo-mdp/a40-1', 'https://picsum.photos/id/1048/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Güemes - foto 2', 'datos-demo-mdp/a40-2', 'https://picsum.photos/id/1049/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Güemes - foto 3', 'datos-demo-mdp/a40-3', 'https://picsum.photos/id/1050/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a40, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Terraza', 'Cafetera');

-- Dúplex moderno en Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Olavarría', 2764, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex moderno en Güemes', 'Dúplex de 4 ambientes, cerca del Paseo Aldrey y de Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 95000, 6, 4, 3, 2, 1, @h16, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a41 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex moderno en Güemes', @a41);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex moderno en Güemes - foto 1', 'datos-demo-mdp/a41-1', 'https://picsum.photos/id/1051/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex moderno en Güemes - foto 2', 'datos-demo-mdp/a41-2', 'https://picsum.photos/id/1052/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex moderno en Güemes - foto 3', 'datos-demo-mdp/a41-3', 'https://picsum.photos/id/1053/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a41, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Terraza', 'Admite mascotas');

-- Departamento con balcón en Playa Grande, Alem
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alem', 3692, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento con balcón en Playa Grande, Alem', 'Departamento de 2 ambientes, a una cuadra de Playa Grande.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 45500, 2, 2, 1, 1, 1, @h17, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a42 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento con balcón en Playa Grande, Alem', @a42);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Playa Grande, Alem - foto 1', 'datos-demo-mdp/a42-1', 'https://picsum.photos/id/1054/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Playa Grande, Alem - foto 2', 'datos-demo-mdp/a42-2', 'https://picsum.photos/id/1055/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Playa Grande, Alem - foto 3', 'datos-demo-mdp/a42-3', 'https://picsum.photos/id/1056/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a42, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Balcón', 'Cochera cubierta', 'Lavarropas');

-- Departamento familiar en Punta Mogotes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 774, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento familiar en Punta Mogotes', 'Departamento de 3 ambientes, a metros de los balnearios de Punta Mogotes.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 79500, 5, 3, 2, 2, 1, @h18, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a43 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento familiar en Punta Mogotes', @a43);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en Punta Mogotes - foto 1', 'datos-demo-mdp/a43-1', 'https://picsum.photos/id/1057/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en Punta Mogotes - foto 2', 'datos-demo-mdp/a43-2', 'https://picsum.photos/id/1058/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en Punta Mogotes - foto 3', 'datos-demo-mdp/a43-3', 'https://picsum.photos/id/1059/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a43, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Apto para niños', 'Estacionamiento', 'Streaming', 'Admite mascotas');

-- Loft industrial en Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Olavarría', 2784, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Loft industrial en Güemes', 'Loft de 1 ambiente, a una cuadra de la calle Güemes.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 45500, 2, 1, 1, 1, 1, @h19, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Loft'));
SET @a44 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Loft industrial en Güemes', @a44);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft industrial en Güemes - foto 1', 'datos-demo-mdp/a44-1', 'https://picsum.photos/id/1060/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft industrial en Güemes - foto 2', 'datos-demo-mdp/a44-2', 'https://picsum.photos/id/1061/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Loft industrial en Güemes - foto 3', 'datos-demo-mdp/a44-3', 'https://picsum.photos/id/1062/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a44, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Escritorio para trabajar', 'Terraza', 'Aire acondicionado', 'Admite mascotas');

-- Departamento con balcón en Chauvín
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Catamarca', 2770, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento con balcón en Chauvín', 'Departamento de 2 ambientes, a cinco cuadras de la Plaza Mitre.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 60500, 2, 2, 1, 1, 1, @h20, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a45 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento con balcón en Chauvín', @a45);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Chauvín - foto 1', 'datos-demo-mdp/a45-1', 'https://picsum.photos/id/1063/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Chauvín - foto 2', 'datos-demo-mdp/a45-2', 'https://picsum.photos/id/1064/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Chauvín - foto 3', 'datos-demo-mdp/a45-3', 'https://picsum.photos/id/1065/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a45, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Patio', 'Microondas', 'Estacionamiento', 'Televisor smart');

-- Dúplex familiar en Punta Mogotes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 640, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex familiar en Punta Mogotes', 'Dúplex de 4 ambientes, a diez minutos del Faro de Punta Mogotes.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 107500, 6, 4, 3, 2, 1, @h21, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a46 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex familiar en Punta Mogotes', @a46);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Punta Mogotes - foto 1', 'datos-demo-mdp/a46-1', 'https://picsum.photos/id/1066/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Punta Mogotes - foto 2', 'datos-demo-mdp/a46-2', 'https://picsum.photos/id/1067/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Punta Mogotes - foto 3', 'datos-demo-mdp/a46-3', 'https://picsum.photos/id/1068/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a46, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Acceso a la playa', 'Cafetera');

-- Departamento luminoso en Plaza Mitre
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('San Luis', 2020, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en Plaza Mitre', 'Departamento de 3 ambientes, cerca de la Av. Colón y del Torreón del Monje.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 49000, 5, 3, 2, 2, 1, @h22, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a47 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en Plaza Mitre', @a47);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Plaza Mitre - foto 1', 'datos-demo-mdp/a47-1', 'https://picsum.photos/id/1069/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Plaza Mitre - foto 2', 'datos-demo-mdp/a47-2', 'https://picsum.photos/id/1070/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Plaza Mitre - foto 3', 'datos-demo-mdp/a47-3', 'https://picsum.photos/id/1071/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a47, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Lavarropas', 'Microondas');

-- Dúplex con patio en Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Olavarría', 3092, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex con patio en Güemes', 'Dúplex de 4 ambientes, en plena zona de Güemes, con locales, cafés y restaurantes.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 81000, 6, 4, 3, 2, 1, @h23, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a48 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex con patio en Güemes', @a48);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Güemes - foto 1', 'datos-demo-mdp/a48-1', 'https://picsum.photos/id/1072/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Güemes - foto 2', 'datos-demo-mdp/a48-2', 'https://picsum.photos/id/1073/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Güemes - foto 3', 'datos-demo-mdp/a48-3', 'https://picsum.photos/id/1074/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a48, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Terraza', 'Balcón', 'Escritorio para trabajar', 'Admite mascotas', 'Estacionamiento');

-- Casa amplia en Los Troncos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alvear', 3376, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa amplia en Los Troncos', 'Casa de 4 ambientes, a cinco cuadras de Playa Grande, en una calle arbolada.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 132500, 5, 4, 3, 2, 1, @h24, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a49 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa amplia en Los Troncos', @a49);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Los Troncos - foto 1', 'datos-demo-mdp/a49-1', 'https://picsum.photos/id/1075/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Los Troncos - foto 2', 'datos-demo-mdp/a49-2', 'https://picsum.photos/id/1076/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Los Troncos - foto 3', 'datos-demo-mdp/a49-3', 'https://picsum.photos/id/1077/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a49, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Jardín', 'Parrilla', 'Hogar a leña', 'Lavarropas', 'Apto para niños', 'Televisor smart', 'Estacionamiento');

-- Monoambiente práctico en el Puerto
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('12 de Octubre', 3664, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Monoambiente práctico en el Puerto', 'Monoambiente de 1 ambiente, a pocas cuadras de la reserva de lobos marinos.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 30500, 2, 1, 1, 1, 1, @h25, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Monoambiente'));
SET @a50 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Monoambiente práctico en el Puerto', @a50);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente práctico en el Puerto - foto 1', 'datos-demo-mdp/a50-1', 'https://picsum.photos/id/1078/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente práctico en el Puerto - foto 2', 'datos-demo-mdp/a50-2', 'https://picsum.photos/id/1079/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente práctico en el Puerto - foto 3', 'datos-demo-mdp/a50-3', 'https://picsum.photos/id/1080/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a50, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Parrilla', 'Escritorio para trabajar', 'Lavarropas', 'Televisor smart');

-- Penthouse frente al mar en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Aristóbulo del Valle', 3016, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Penthouse frente al mar en Playa Grande', 'Penthouse de 5 ambientes, a una cuadra de Playa Grande.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 158500, 6, 5, 3, 3, 1, @h4, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Penthouse'));
SET @a51 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Penthouse frente al mar en Playa Grande', @a51);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Penthouse frente al mar en Playa Grande - foto 1', 'datos-demo-mdp/a51-1', 'https://picsum.photos/id/1081/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Penthouse frente al mar en Playa Grande - foto 2', 'datos-demo-mdp/a51-2', 'https://picsum.photos/id/1082/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Penthouse frente al mar en Playa Grande - foto 3', 'datos-demo-mdp/a51-3', 'https://picsum.photos/id/1083/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a51, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Vista al mar', 'Terraza', 'Jacuzzi', 'Admite mascotas');

-- PH reciclado en Chauvín
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Catamarca', 2870, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('PH reciclado en Chauvín', 'PH de 2 ambientes, en Chauvín, a pocas cuadras del centro.\nCómodo para una pareja con un hijo o dos amigos. Capacidad para 3 personas.', 53500, 3, 2, 1, 1, 1, @h2, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Propiedad horizontal'));
SET @a52 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de PH reciclado en Chauvín', @a52);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH reciclado en Chauvín - foto 1', 'datos-demo-mdp/a52-1', 'https://picsum.photos/id/1084/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH reciclado en Chauvín - foto 2', 'datos-demo-mdp/a52-2', 'https://picsum.photos/id/164/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH reciclado en Chauvín - foto 3', 'datos-demo-mdp/a52-3', 'https://picsum.photos/id/188/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a52, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Lavarropas', 'Aire acondicionado', 'Admite mascotas', 'Estacionamiento');

-- Casa de playa en Constitución
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Félix U. Camet', 4310, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa de playa en Constitución', 'Casa de playa de 5 ambientes, a minutos de Playa Chica y del Paseo Costanero Norte.\nPensado para familias o grupos grandes. Capacidad para 7 personas.', 89500, 7, 5, 3, 2, 1, @h21, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa de playa'));
SET @a53 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa de playa en Constitución', @a53);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa de playa en Constitución - foto 1', 'datos-demo-mdp/a53-1', 'https://picsum.photos/id/240/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa de playa en Constitución - foto 2', 'datos-demo-mdp/a53-2', 'https://picsum.photos/id/274/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa de playa en Constitución - foto 3', 'datos-demo-mdp/a53-3', 'https://picsum.photos/id/279/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a53, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Parrilla', 'Jardín', 'Lavarropas');

-- Casa con jardín en Stella Maris
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Patricio Peralta Ramos', 2804, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con jardín en Stella Maris', 'Casa de 5 ambientes, a pasos de Cabo Corrientes y la Torre Tanque.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 83500, 6, 5, 3, 2, 1, @h9, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a54 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con jardín en Stella Maris', @a54);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris - foto 1', 'datos-demo-mdp/a54-1', 'https://picsum.photos/id/299/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris - foto 2', 'datos-demo-mdp/a54-2', 'https://picsum.photos/id/306/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris - foto 3', 'datos-demo-mdp/a54-3', 'https://picsum.photos/id/308/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a54, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Terraza', 'Lavarropas', 'Apto para niños', 'Admite mascotas', 'Microondas', 'Televisor smart');

-- PH con patio en La Perla
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Hipólito Yrigoyen', 1156, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('PH con patio en La Perla', 'PH de 2 ambientes, a tres cuadras de las playas de La Perla.\nCómodo para una pareja con un hijo o dos amigos. Capacidad para 3 personas.', 56500, 3, 2, 1, 1, 1, @h2, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Propiedad horizontal'));
SET @a55 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de PH con patio en La Perla', @a55);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en La Perla - foto 1', 'datos-demo-mdp/a55-1', 'https://picsum.photos/id/311/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en La Perla - foto 2', 'datos-demo-mdp/a55-2', 'https://picsum.photos/id/315/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en La Perla - foto 3', 'datos-demo-mdp/a55-3', 'https://picsum.photos/id/318/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a55, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Aire acondicionado', 'Admite mascotas');

-- Departamento amplio en Plaza Mitre, San Luis
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('San Luis', 2152, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento amplio en Plaza Mitre, San Luis', 'Departamento de 3 ambientes, cerca de la Av. Colón y del Torreón del Monje.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 79000, 4, 3, 2, 1, 1, @h25, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a56 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento amplio en Plaza Mitre, San Luis', @a56);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Plaza Mitre, San Luis - foto 1', 'datos-demo-mdp/a56-1', 'https://picsum.photos/id/326/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Plaza Mitre, San Luis - foto 2', 'datos-demo-mdp/a56-2', 'https://picsum.photos/id/337/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Plaza Mitre, San Luis - foto 3', 'datos-demo-mdp/a56-3', 'https://picsum.photos/id/342/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a56, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Balcón', 'Lavarropas', 'Admite mascotas', 'Televisor smart');

-- Departamento luminoso en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Aristóbulo del Valle', 2968, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en Playa Grande', 'Departamento de 2 ambientes, a una cuadra de Playa Grande.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 60500, 2, 2, 1, 1, 1, @h16, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a57 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en Playa Grande', @a57);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Playa Grande - foto 1', 'datos-demo-mdp/a57-1', 'https://picsum.photos/id/349/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Playa Grande - foto 2', 'datos-demo-mdp/a57-2', 'https://picsum.photos/id/362/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Playa Grande - foto 3', 'datos-demo-mdp/a57-3', 'https://picsum.photos/id/364/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a57, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Acceso a la playa', 'Vista al mar', 'Aire acondicionado', 'Microondas');

-- Departamento luminoso en Playa Grande, Aristóbulo del Valle
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Aristóbulo del Valle', 3436, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento luminoso en Playa Grande, Aristóbulo del Valle', 'Departamento de 2 ambientes, a pasos de la calle Alem y sus bares.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 63000, 2, 2, 1, 1, 1, @h17, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a58 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento luminoso en Playa Grande, Aristóbulo del Valle', @a58);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Playa Grande, Aristóbulo del Valle - foto 1', 'datos-demo-mdp/a58-1', 'https://picsum.photos/id/366/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Playa Grande, Aristóbulo del Valle - foto 2', 'datos-demo-mdp/a58-2', 'https://picsum.photos/id/1011/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento luminoso en Playa Grande, Aristóbulo del Valle - foto 3', 'datos-demo-mdp/a58-3', 'https://picsum.photos/id/1013/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a58, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Vista al mar', 'Cochera cubierta', 'Estacionamiento', 'Aire acondicionado', 'Televisor smart');

-- Dúplex familiar en Constitución
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Constitución', 5788, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex familiar en Constitución', 'Dúplex de 4 ambientes, a cuatro cuadras de las playas del norte.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 81000, 6, 4, 3, 2, 1, @h11, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a59 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex familiar en Constitución', @a59);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Constitución - foto 1', 'datos-demo-mdp/a59-1', 'https://picsum.photos/id/1015/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Constitución - foto 2', 'datos-demo-mdp/a59-2', 'https://picsum.photos/id/1016/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex familiar en Constitución - foto 3', 'datos-demo-mdp/a59-3', 'https://picsum.photos/id/1018/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a59, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Pileta', 'Estacionamiento', 'Jardín', 'Admite mascotas');

-- Chalet de piedra en el Bosque Peralta Ramos, Av. Mario Bravo  (pausado)
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 4280, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Chalet de piedra en el Bosque Peralta Ramos, Av. Mario Bravo', 'Chalet de 5 ambientes, en un barrio tranquilo con mucho verde.\nPensado para familias o grupos grandes. Capacidad para 6 personas.\nPublicación pausada por el anfitrión: no aparece en el buscador.', 157000, 6, 5, 3, 2, 0, @h17, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Chalet'));
SET @a60 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Chalet de piedra en el Bosque Peralta Ramos, Av. Mario Bravo', @a60);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en el Bosque Peralta Ramos, Av. Mario Bravo - foto 1', 'datos-demo-mdp/a60-1', 'https://picsum.photos/id/1019/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en el Bosque Peralta Ramos, Av. Mario Bravo - foto 2', 'datos-demo-mdp/a60-2', 'https://picsum.photos/id/1020/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Chalet de piedra en el Bosque Peralta Ramos, Av. Mario Bravo - foto 3', 'datos-demo-mdp/a60-3', 'https://picsum.photos/id/1021/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a60, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Parrilla', 'Pileta', 'Hogar a leña', 'Jardín', 'Televisor smart', 'Admite mascotas');

-- Penthouse con terraza en Playa Grande
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Aristóbulo del Valle', 2806, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Penthouse con terraza en Playa Grande', 'Penthouse de 6 ambientes, a una cuadra de Playa Grande.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 149500, 8, 6, 4, 3, 1, @h8, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Penthouse'));
SET @a61 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Penthouse con terraza en Playa Grande', @a61);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Penthouse con terraza en Playa Grande - foto 1', 'datos-demo-mdp/a61-1', 'https://picsum.photos/id/1022/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Penthouse con terraza en Playa Grande - foto 2', 'datos-demo-mdp/a61-2', 'https://picsum.photos/id/1023/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Penthouse con terraza en Playa Grande - foto 3', 'datos-demo-mdp/a61-3', 'https://picsum.photos/id/1024/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a61, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Vista al mar', 'Terraza', 'Jacuzzi', 'Estacionamiento', 'Admite mascotas');

-- Dúplex con patio en Playa Grande, Alem
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alem', 3696, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex con patio en Playa Grande, Alem', 'Dúplex de 4 ambientes, a pasos de la calle Alem y sus bares.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 84500, 6, 4, 3, 2, 1, @h6, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a62 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex con patio en Playa Grande, Alem', @a62);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Playa Grande, Alem - foto 1', 'datos-demo-mdp/a62-1', 'https://picsum.photos/id/1025/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Playa Grande, Alem - foto 2', 'datos-demo-mdp/a62-2', 'https://picsum.photos/id/1026/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Playa Grande, Alem - foto 3', 'datos-demo-mdp/a62-3', 'https://picsum.photos/id/1027/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a62, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Cochera cubierta', 'Televisor smart', 'Aire acondicionado', 'Cafetera');

-- PH con patio en Güemes, Güemes
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Güemes', 2912, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('PH con patio en Güemes, Güemes', 'PH de 3 ambientes, a una cuadra de la calle Güemes.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 68500, 4, 3, 2, 1, 1, @h5, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Propiedad horizontal'));
SET @a63 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de PH con patio en Güemes, Güemes', @a63);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en Güemes, Güemes - foto 1', 'datos-demo-mdp/a63-1', 'https://picsum.photos/id/1028/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en Güemes, Güemes - foto 2', 'datos-demo-mdp/a63-2', 'https://picsum.photos/id/1029/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('PH con patio en Güemes, Güemes - foto 3', 'datos-demo-mdp/a63-3', 'https://picsum.photos/id/1031/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a63, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Terraza', 'Balcón', 'Escritorio para trabajar', 'Estacionamiento', 'Microondas', 'Cafetera');

-- Dúplex con patio en Güemes, Alvarado
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Alvarado', 2812, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Dúplex con patio en Güemes, Alvarado', 'Dúplex de 4 ambientes, en plena zona de Güemes, con locales, cafés y restaurantes.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 82500, 6, 4, 3, 2, 1, @h24, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Dúplex'));
SET @a64 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Dúplex con patio en Güemes, Alvarado', @a64);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Güemes, Alvarado - foto 1', 'datos-demo-mdp/a64-1', 'https://picsum.photos/id/1032/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Güemes, Alvarado - foto 2', 'datos-demo-mdp/a64-2', 'https://picsum.photos/id/1033/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Dúplex con patio en Güemes, Alvarado - foto 3', 'datos-demo-mdp/a64-3', 'https://picsum.photos/id/1035/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a64, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Terraza', 'Balcón', 'Lavarropas', 'Admite mascotas');

-- Monoambiente reciclado en La Perla, Hipólito Yrigoyen
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Hipólito Yrigoyen', 938, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Monoambiente reciclado en La Perla, Hipólito Yrigoyen', 'Monoambiente de 1 ambiente, a tres cuadras de las playas de La Perla.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 38500, 2, 1, 1, 1, 1, @h20, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Monoambiente'));
SET @a65 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Monoambiente reciclado en La Perla, Hipólito Yrigoyen', @a65);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente reciclado en La Perla, Hipólito Yrigoyen - foto 1', 'datos-demo-mdp/a65-1', 'https://picsum.photos/id/1036/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente reciclado en La Perla, Hipólito Yrigoyen - foto 2', 'datos-demo-mdp/a65-2', 'https://picsum.photos/id/1037/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente reciclado en La Perla, Hipólito Yrigoyen - foto 3', 'datos-demo-mdp/a65-3', 'https://picsum.photos/id/1038/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a65, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Acceso a la playa', 'Vista al mar', 'Escritorio para trabajar', 'Estacionamiento');

-- Departamento amplio en Punta Mogotes, Av. Mario Bravo
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 786, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento amplio en Punta Mogotes, Av. Mario Bravo', 'Departamento de 3 ambientes, a metros de los balnearios de Punta Mogotes.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 78500, 5, 3, 2, 2, 1, @h14, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a66 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento amplio en Punta Mogotes, Av. Mario Bravo', @a66);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Punta Mogotes, Av. Mario Bravo - foto 1', 'datos-demo-mdp/a66-1', 'https://picsum.photos/id/1039/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Punta Mogotes, Av. Mario Bravo - foto 2', 'datos-demo-mdp/a66-2', 'https://picsum.photos/id/1040/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento amplio en Punta Mogotes, Av. Mario Bravo - foto 3', 'datos-demo-mdp/a66-3', 'https://picsum.photos/id/1041/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a66, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Apto para niños', 'Ducha exterior', 'Admite mascotas');

-- Casa familiar en el Puerto
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('12 de Octubre', 3132, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa familiar en el Puerto', 'Casa de 5 ambientes, cerca de la banquina de pescadores.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 124500, 6, 5, 3, 2, 1, @h16, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a67 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa familiar en el Puerto', @a67);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa familiar en el Puerto - foto 1', 'datos-demo-mdp/a67-1', 'https://picsum.photos/id/1042/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa familiar en el Puerto - foto 2', 'datos-demo-mdp/a67-2', 'https://picsum.photos/id/1043/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa familiar en el Puerto - foto 3', 'datos-demo-mdp/a67-3', 'https://picsum.photos/id/1044/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a67, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Parrilla', 'Estacionamiento', 'Lavarropas', 'Apto para niños', 'Cafetera');

-- Departamento con balcón en Plaza Mitre
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('San Luis', 2340, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento con balcón en Plaza Mitre', 'Departamento de 3 ambientes, frente a la Plaza Mitre.\nPensado para familias o grupos grandes. Capacidad para 5 personas.', 50500, 5, 3, 2, 2, 1, @h7, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a68 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento con balcón en Plaza Mitre', @a68);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Plaza Mitre - foto 1', 'datos-demo-mdp/a68-1', 'https://picsum.photos/id/1045/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Plaza Mitre - foto 2', 'datos-demo-mdp/a68-2', 'https://picsum.photos/id/1047/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento con balcón en Plaza Mitre - foto 3', 'datos-demo-mdp/a68-3', 'https://picsum.photos/id/1048/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a68, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Centro de la ciudad', 'Ascensor', 'Streaming', 'Microondas', 'Estacionamiento');

-- Casa con jardín en Stella Maris, Av. Patricio Peralta Ramos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Patricio Peralta Ramos', 3214, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con jardín en Stella Maris, Av. Patricio Peralta Ramos', 'Casa de 5 ambientes, frente a Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 88000, 6, 5, 3, 2, 1, @h25, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a69 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con jardín en Stella Maris, Av. Patricio Peralta Ramos', @a69);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris, Av. Patricio Peralta Ramos - foto 1', 'datos-demo-mdp/a69-1', 'https://picsum.photos/id/1049/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris, Av. Patricio Peralta Ramos - foto 2', 'datos-demo-mdp/a69-2', 'https://picsum.photos/id/1050/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en Stella Maris, Av. Patricio Peralta Ramos - foto 3', 'datos-demo-mdp/a69-3', 'https://picsum.photos/id/1051/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a69, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Terraza', 'Ascensor', 'Lavarropas', 'Apto para niños', 'Cafetera', 'Televisor smart', 'Streaming');

-- Monoambiente luminoso en el Centro
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Córdoba', 1982, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Monoambiente luminoso en el Centro', 'Monoambiente de 1 ambiente, a dos cuadras de la peatonal San Martín.\nIdeal para una pareja o para viajes de trabajo. Capacidad para 2 personas.', 31000, 2, 1, 1, 1, 1, @h12, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Monoambiente'));
SET @a70 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Monoambiente luminoso en el Centro', @a70);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente luminoso en el Centro - foto 1', 'datos-demo-mdp/a70-1', 'https://picsum.photos/id/1052/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente luminoso en el Centro - foto 2', 'datos-demo-mdp/a70-2', 'https://picsum.photos/id/1053/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Monoambiente luminoso en el Centro - foto 3', 'datos-demo-mdp/a70-3', 'https://picsum.photos/id/1054/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a70, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Ascensor', 'Escritorio para trabajar', 'Microondas', 'Cafetera');

-- Departamento familiar en La Perla
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Brown', 1050, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento familiar en La Perla', 'Departamento de 4 ambientes, cerca del Paseo Hermitage y la Rambla.\nPensado para familias o grupos grandes. Capacidad para 6 personas.', 74000, 6, 4, 3, 2, 1, @h3, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a71 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento familiar en La Perla', @a71);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en La Perla - foto 1', 'datos-demo-mdp/a71-1', 'https://picsum.photos/id/1055/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en La Perla - foto 2', 'datos-demo-mdp/a71-2', 'https://picsum.photos/id/1056/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en La Perla - foto 3', 'datos-demo-mdp/a71-3', 'https://picsum.photos/id/1057/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a71, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Balcón', 'Vista al mar', 'Microondas', 'Lavarropas');

-- Casa con jardín en el Puerto  (pausado)
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Juan B. Justo', 436, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa con jardín en el Puerto', 'Casa de 4 ambientes, a pocas cuadras de la reserva de lobos marinos.\nPensado para familias o grupos grandes. Capacidad para 5 personas.\nPublicación pausada por el anfitrión: no aparece en el buscador.', 88000, 5, 4, 3, 2, 0, @h23, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a72 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa con jardín en el Puerto', @a72);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en el Puerto - foto 1', 'datos-demo-mdp/a72-1', 'https://picsum.photos/id/1058/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en el Puerto - foto 2', 'datos-demo-mdp/a72-2', 'https://picsum.photos/id/1059/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa con jardín en el Puerto - foto 3', 'datos-demo-mdp/a72-3', 'https://picsum.photos/id/1060/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a72, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Estacionamiento', 'Parrilla', 'Lavarropas', 'Apto para niños', 'Streaming');

-- Departamento familiar en el Centro, Belgrano
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Belgrano', 2974, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Departamento familiar en el Centro, Belgrano', 'Departamento de 3 ambientes, a dos cuadras de la peatonal San Martín.\nPensado para familias o grupos chicos. Capacidad para 4 personas.', 38500, 4, 3, 2, 1, 1, @h23, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Departamento'));
SET @a73 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Departamento familiar en el Centro, Belgrano', @a73);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en el Centro, Belgrano - foto 1', 'datos-demo-mdp/a73-1', 'https://picsum.photos/id/1061/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en el Centro, Belgrano - foto 2', 'datos-demo-mdp/a73-2', 'https://picsum.photos/id/1062/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Departamento familiar en el Centro, Belgrano - foto 3', 'datos-demo-mdp/a73-3', 'https://picsum.photos/id/1063/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a73, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Escritorio para trabajar', 'Cafetera');

-- Casa amplia en Stella Maris
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Gascón', 1664, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Casa amplia en Stella Maris', 'Casa de 6 ambientes, con vista a la bahía de Playa Varese.\nPensado para familias o grupos grandes. Capacidad para 8 personas.', 86000, 8, 6, 4, 3, 1, @h19, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa'));
SET @a74 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Casa amplia en Stella Maris', @a74);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Stella Maris - foto 1', 'datos-demo-mdp/a74-1', 'https://picsum.photos/id/1064/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Stella Maris - foto 2', 'datos-demo-mdp/a74-2', 'https://picsum.photos/id/1065/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Casa amplia en Stella Maris - foto 3', 'datos-demo-mdp/a74-3', 'https://picsum.photos/id/1066/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a74, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Terraza', 'Acceso a la playa', 'Lavarropas', 'Apto para niños', 'Estacionamiento', 'Admite mascotas');

-- Quinta con parque y quincho en el Bosque Peralta Ramos
INSERT INTO direccion (calle, altura, ciudad, provincia, pais, codigo_postal) VALUES ('Av. Mario Bravo', 4746, 'Mar del Plata', 'Buenos Aires', 'Argentina', '7600');
SET @dir = LAST_INSERT_ID();
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_banios, activo, id_anfitrion, id_direccion, id_tipo_alojamiento) VALUES ('Quinta con parque y quincho en el Bosque Peralta Ramos', 'Casa quinta de 7 ambientes, en el Bosque Peralta Ramos, rodeado de pinos.\nPensado para familias o grupos grandes. Capacidad para 10 personas.', 114500, 10, 7, 5, 3, 1, @h23, @dir, (SELECT id_tipo_alojamiento FROM tipo_alojamiento WHERE nombre_tipo = 'Casa quinta'));
SET @a75 = LAST_INSERT_ID();
INSERT INTO galeria (nombre_galeria, id_alojamiento) VALUES ('Galeria de Quinta con parque y quincho en el Bosque Peralta Ramos', @a75);
SET @gal = LAST_INSERT_ID();
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Quinta con parque y quincho en el Bosque Peralta Ramos - foto 1', 'datos-demo-mdp/a75-1', 'https://picsum.photos/id/1067/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Quinta con parque y quincho en el Bosque Peralta Ramos - foto 2', 'datos-demo-mdp/a75-2', 'https://picsum.photos/id/1068/1200/900', @gal);
INSERT INTO imagen (alt, public_id, url, id_galeria) VALUES ('Quinta con parque y quincho en el Bosque Peralta Ramos - foto 3', 'datos-demo-mdp/a75-3', 'https://picsum.photos/id/1069/1200/900', @gal);
INSERT INTO servicio_x_alojamiento (id_alojamiento, id_servicio) SELECT @a75, id_servicio FROM servicio WHERE nombre_servicio IN ('Wifi', 'Ropa de cama y toallas', 'Cocina equipada', 'Calefacción', 'Admite mascotas', 'Pileta', 'Parrilla', 'Quincho', 'Estacionamiento', 'Lavarropas');

-- ---------------------------------------------------------------- reservas, pagos y resenas
-- Montos como los calcula el backend: monto base = noches x precio; el cliente
-- paga ademas 5% de comision de la plataforma y 6% estimado de MercadoPago.
-- En cada alojamiento las reservas no se superponen.

-- R1. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -128 DAY);
SET @n = 5;
SET @p = 59500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a2, @c43);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -134 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '09:00:00'), 'demo-mdp-pago-1', 'demo-mdp-payout-1', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy buena experiencia. La calefacción tardaba en calentar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -120 DAY), '19:00:00'));

-- R2. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -109 DAY);
SET @n = 7;
SET @p = 59500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a2, @c7);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '09:00:00'), 'demo-mdp-pago-2', 'demo-mdp-payout-2', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R3. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -113 DAY);
SET @n = 5;
SET @p = 134500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a3, @c17);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '09:00:00'), 'demo-mdp-pago-3', 'demo-mdp-payout-3', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy lindo y bien equipado. El wifi se cortaba de a ratos.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '19:00:00'));

-- R4. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -94 DAY);
SET @n = 5;
SET @p = 134500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -106 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a3, @c2);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -106 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -104 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -88 DAY), '09:00:00'), 'demo-mdp-pago-4', 'demo-mdp-payout-4', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy lindo y bien equipado. El wifi se cortaba de a ratos.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -88 DAY), '19:00:00'));

-- R5. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 14 DAY);
SET @n = 7;
SET @p = 134500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a3, @c22);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-5', NULL, NULL, NULL, NULL);

-- R6. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -126 DAY);
SET @n = 2;
SET @p = 45000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a5, @c35);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -134 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '09:00:00'), 'demo-mdp-pago-6', 'demo-mdp-payout-6', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '19:00:00'));

-- R7. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 22 DAY);
SET @n = 4;
SET @p = 45000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a5, @c15);
SET @r = LAST_INSERT_ID();

-- R8. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 32 DAY);
SET @n = 7;
SET @p = 45000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a5, @c5);
SET @r = LAST_INSERT_ID();

-- R9. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -136 DAY);
SET @n = 7;
SET @p = 82000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -148 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a6, @c49);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -148 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -146 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -147 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '09:00:00'), 'demo-mdp-pago-9', 'demo-mdp-payout-9', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R10. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -102 DAY);
SET @n = 4;
SET @p = 82000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a6, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -97 DAY), '09:00:00'), 'demo-mdp-pago-10', 'demo-mdp-payout-10', NULL, NULL, @base);

-- R11. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -112 DAY);
SET @n = 2;
SET @p = 30000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a7, @c4);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '09:00:00'), 'demo-mdp-pago-11', 'demo-mdp-payout-11', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy buena experiencia. La calefacción tardaba en calentar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '19:00:00'));

-- R12. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 11 DAY);
SET @n = 3;
SET @p = 30000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a7, @c41);
SET @r = LAST_INSERT_ID();

-- R13. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 36 DAY);
SET @n = 4;
SET @p = 30000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a7, @c19);
SET @r = LAST_INSERT_ID();

-- R14. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -132 DAY);
SET @n = 3;
SET @p = 135500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a8, @c49);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -143 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '09:00:00'), 'demo-mdp-pago-14', 'demo-mdp-payout-14', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '19:00:00'));

-- R15. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -117 DAY);
SET @n = 7;
SET @p = 135500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a8, @c25);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -121 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '09:00:00'), 'demo-mdp-pago-15', 'demo-mdp-payout-15', NULL, NULL, @base);

-- R16. Confirmada con pago pendiente (vence en 43 h)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 7 DAY);
SET @n = 3;
SET @p = 135500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -5 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a8, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -5 HOUR), DATE_ADD(NOW(), INTERVAL 43 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R17. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -123 DAY);
SET @n = 3;
SET @p = 128500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -130 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a9, @c45);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -130 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '09:00:00'), 'demo-mdp-pago-17', 'demo-mdp-payout-17', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '19:00:00'));

-- R18. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -110 DAY);
SET @n = 3;
SET @p = 128500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -120 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a9, @c13);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -120 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -106 DAY), '09:00:00'), 'demo-mdp-pago-18', 'demo-mdp-payout-18', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'La ubicación es inmejorable y el lugar es hermoso. Nos sentimos como en casa.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -104 DAY), '19:00:00'));

-- R19. Confirmada y pagada: estadía en curso o recién terminada (el anfitrión puede finalizarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -1 DAY);
SET @n = 5;
SET @p = 128500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a9, @c31);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-19', NULL, NULL, NULL, NULL);

-- R20. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -128 DAY);
SET @n = 3;
SET @p = 45500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -138 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a10, @c4);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -138 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -137 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '09:00:00'), 'demo-mdp-pago-20', 'demo-mdp-payout-20', NULL, NULL, @base);

-- R21. Confirmada con pago pendiente (vence en 44 h)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 23 DAY);
SET @n = 3;
SET @p = 45500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -4 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a10, @c47);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -4 HOUR), DATE_ADD(NOW(), INTERVAL 44 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R22. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 48 DAY);
SET @n = 3;
SET @p = 45500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a10, @c20);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-22', NULL, NULL, NULL, NULL);

-- R23. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -134 DAY);
SET @n = 3;
SET @p = 90000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a11, @c21);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -130 DAY), '09:00:00'), 'demo-mdp-pago-23', 'demo-mdp-payout-23', NULL, NULL, @base);

-- R24. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 24 DAY);
SET @n = 2;
SET @p = 90000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a11, @c45);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '16:30:00'), NULL, 'demo-mdp-pago-24', NULL, NULL, NULL, NULL);

-- R25. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -122 DAY);
SET @n = 2;
SET @p = 56000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a12, @c21);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '09:00:00'), 'demo-mdp-pago-25', 'demo-mdp-payout-25', NULL, NULL, @base);

-- R26. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -110 DAY);
SET @n = 7;
SET @p = 56000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a12, @c7);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -102 DAY), '09:00:00'), 'demo-mdp-pago-26', 'demo-mdp-payout-26', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Lindo lugar, el check-in fue fácil. Algo de ruido de la calle a la noche.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '19:00:00'));

-- R27. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -101 DAY);
SET @n = 2;
SET @p = 53000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a13, @c45);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '09:00:00'), 'demo-mdp-pago-27', 'demo-mdp-payout-27', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '19:00:00'));

-- R28. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 21 DAY);
SET @n = 7;
SET @p = 53000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a13, @c17);
SET @r = LAST_INSERT_ID();

-- R29. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -118 DAY);
SET @n = 5;
SET @p = 53000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a14, @c4);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '09:00:00'), 'demo-mdp-pago-29', 'demo-mdp-payout-29', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 3, 'La ubicación es buena pero el lugar es más chico de lo que parece en las fotos.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '19:00:00'));

-- R30. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -85 DAY);
SET @n = 2;
SET @p = 53000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a14, @c8);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -94 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '16:30:00'), NULL, 'demo-mdp-pago-30', NULL, 'cliente_anticipada', @base, 0);

-- R31. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 3 DAY);
SET @n = 4;
SET @p = 53000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a14, @c23);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '16:30:00'), NULL, 'demo-mdp-pago-31', NULL, NULL, NULL, NULL);

-- R32. Cancelada: el cliente nunca pagó (pago vencido)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -113 DAY);
SET @n = 7;
SET @p = 79000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a15, @c8);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'vencido', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), NULL, NULL, NULL, NULL, 'vencimiento_pago', NULL, NULL);

-- R33. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 9 DAY);
SET @n = 7;
SET @p = 79000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a15, @c34);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-33', NULL, NULL, NULL, NULL);

-- R34. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -132 DAY);
SET @n = 7;
SET @p = 166500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a16, @c28);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '09:00:00'), 'demo-mdp-pago-34', 'demo-mdp-payout-34', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Lindo lugar, el check-in fue fácil. Algo de ruido de la calle a la noche.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '19:00:00'));

-- R35. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 12 DAY);
SET @n = 3;
SET @p = 166500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a16, @c39);
SET @r = LAST_INSERT_ID();

-- R36. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -104 DAY);
SET @n = 3;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a17, @c12);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '09:00:00'), 'demo-mdp-pago-36', 'demo-mdp-payout-36', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente lugar para pasar unos días en Mar del Plata. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '19:00:00'));

-- R37. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -73 DAY);
SET @n = 3;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -82 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a17, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -82 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -80 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -81 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -73 DAY), '09:00:00'), 'demo-mdp-pago-37', 'demo-mdp-payout-37', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R38. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 13 DAY);
SET @n = 2;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a17, @c3);
SET @r = LAST_INSERT_ID();

-- R39. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 24 DAY);
SET @n = 7;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a17, @c47);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '16:30:00'), NULL, 'demo-mdp-pago-39', NULL, NULL, NULL, NULL);

-- R40. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -123 DAY);
SET @n = 3;
SET @p = 180000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -134 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a19, @c8);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -134 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -132 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -133 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '09:00:00'), 'demo-mdp-pago-40', 'demo-mdp-payout-40', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'La ubicación es inmejorable y el lugar es hermoso. Nos sentimos como en casa.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '19:00:00'));

-- R41. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -94 DAY);
SET @n = 2;
SET @p = 180000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -102 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a19, @c16);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -102 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -101 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -94 DAY), '09:00:00'), 'demo-mdp-pago-41', 'demo-mdp-payout-41', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R42. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 19 DAY);
SET @n = 4;
SET @p = 180000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a19, @c21);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-42', NULL, NULL, NULL, NULL);

-- R43. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 36 DAY);
SET @n = 4;
SET @p = 180000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a19, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '16:30:00'), NULL, 'demo-mdp-pago-43', NULL, NULL, NULL, NULL);

-- R44. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -104 DAY);
SET @n = 4;
SET @p = 72000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a20, @c36);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -99 DAY), '09:00:00'), 'demo-mdp-pago-44', 'demo-mdp-payout-44', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '19:00:00'));

-- R45. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -135 DAY);
SET @n = 3;
SET @p = 72000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -146 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a21, @c2);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -146 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -131 DAY), '09:00:00'), 'demo-mdp-pago-45', 'demo-mdp-payout-45', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -131 DAY), '19:00:00'));

-- R46. Cancelada: el cliente nunca pagó (pago vencido)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -116 DAY);
SET @n = 5;
SET @p = 72000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a21, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'vencido', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -120 DAY), '10:00:00'), NULL, NULL, NULL, NULL, 'vencimiento_pago', NULL, NULL);

-- R47. Confirmada y pagada: estadía en curso o recién terminada (el anfitrión puede finalizarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -1 DAY);
SET @n = 5;
SET @p = 72000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a21, @c38);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '16:30:00'), NULL, 'demo-mdp-pago-47', NULL, NULL, NULL, NULL);

-- R48. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 22 DAY);
SET @n = 4;
SET @p = 143000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a22, @c15);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '16:30:00'), NULL, 'demo-mdp-pago-48', NULL, NULL, NULL, NULL);

-- R49. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 19 DAY);
SET @n = 4;
SET @p = 124500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a23, @c34);
SET @r = LAST_INSERT_ID();

-- R50. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -114 DAY);
SET @n = 2;
SET @p = 68500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -121 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a24, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -121 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -120 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '09:00:00'), 'demo-mdp-pago-50', 'demo-mdp-payout-50', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '19:00:00'));

-- R51. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -105 DAY);
SET @n = 3;
SET @p = 68500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a24, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -101 DAY), '09:00:00'), 'demo-mdp-pago-51', 'demo-mdp-payout-51', NULL, NULL, @base);

-- R52. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -121 DAY);
SET @n = 4;
SET @p = 59500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a25, @c22);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '09:00:00'), 'demo-mdp-pago-52', 'demo-mdp-payout-52', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '19:00:00'));

-- R53. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 5 DAY);
SET @n = 4;
SET @p = 59500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a25, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '16:30:00'), NULL, 'demo-mdp-pago-53', NULL, NULL, NULL, NULL);

-- R54. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -131 DAY);
SET @n = 4;
SET @p = 57000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -138 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a26, @c40);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -138 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -137 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '09:00:00'), 'demo-mdp-pago-54', 'demo-mdp-payout-54', NULL, NULL, @base);

-- R55. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -108 DAY);
SET @n = 2;
SET @p = 57000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a26, @c5);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '09:00:00'), 'demo-mdp-pago-55', 'demo-mdp-payout-55', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R56. Confirmada con pago pendiente (vence en 19 h)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 22 DAY);
SET @n = 5;
SET @p = 57000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -29 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a26, @c13);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -29 HOUR), DATE_ADD(NOW(), INTERVAL 19 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R57. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -132 DAY);
SET @n = 7;
SET @p = 109500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a27, @c9);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -143 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '09:00:00'), 'demo-mdp-pago-57', 'demo-mdp-payout-57', NULL, NULL, @base);

-- R58. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -118 DAY);
SET @n = 5;
SET @p = 109500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a27, @c39);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '16:30:00'), NULL, 'demo-mdp-pago-58', NULL, 'cliente_anticipada', @base, 0);

-- R59. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 18 DAY);
SET @n = 3;
SET @p = 129500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a28, @c35);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-59', NULL, NULL, NULL, NULL);

-- R60. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -126 DAY);
SET @n = 2;
SET @p = 98500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -133 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a29, @c30);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -133 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -131 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -132 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '09:00:00'), 'demo-mdp-pago-60', 'demo-mdp-payout-60', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente lugar para pasar unos días en Mar del Plata. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '19:00:00'));

-- R61. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 19 DAY);
SET @n = 3;
SET @p = 98500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a29, @c30);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-61', NULL, 'anfitrion', @base, 0);

-- R62. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -130 DAY);
SET @n = 3;
SET @p = 97500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a30, @c40);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '16:30:00'), NULL, 'demo-mdp-pago-62', NULL, 'cliente_anticipada', @base, 0);

-- R63. Cancelada: el cliente nunca pagó (pago vencido)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -116 DAY);
SET @n = 7;
SET @p = 97500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a30, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'vencido', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '10:00:00'), NULL, NULL, NULL, NULL, 'vencimiento_pago', NULL, NULL);

-- R64. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 7 DAY);
SET @n = 3;
SET @p = 97500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a30, @c46);
SET @r = LAST_INSERT_ID();

-- R65. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 31 DAY);
SET @n = 2;
SET @p = 97500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a30, @c20);
SET @r = LAST_INSERT_ID();

-- R66. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -139 DAY);
SET @n = 3;
SET @p = 68000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -151 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a31, @c44);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -151 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -149 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -150 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '09:00:00'), 'demo-mdp-pago-66', 'demo-mdp-payout-66', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Muy limpio, cómodo y con todo lo necesario. Súper recomendable.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '19:00:00'));

-- R67. Confirmada con pago pendiente (vence en 29 h)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 10 DAY);
SET @n = 7;
SET @p = 68000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -19 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a31, @c42);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -19 HOUR), DATE_ADD(NOW(), INTERVAL 29 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R68. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -103 DAY);
SET @n = 4;
SET @p = 139000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a32, @c48);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '09:00:00'), 'demo-mdp-pago-68', 'demo-mdp-payout-68', NULL, NULL, @base);

-- R69. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -95 DAY);
SET @n = 3;
SET @p = 139000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a32, @c25);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -103 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -104 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -91 DAY), '09:00:00'), 'demo-mdp-pago-69', 'demo-mdp-payout-69', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 3, 'Correcto por el precio, aunque le falta mantenimiento.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -89 DAY), '19:00:00'));

-- R70. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 17 DAY);
SET @n = 2;
SET @p = 139000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a32, @c31);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-70', NULL, 'anfitrion', @base, 0);

-- R71. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 25 DAY);
SET @n = 3;
SET @p = 139000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a32, @c9);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '16:30:00'), NULL, 'demo-mdp-pago-71', NULL, NULL, NULL, NULL);

-- R72. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -108 DAY);
SET @n = 2;
SET @p = 76000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a33, @c12);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '09:00:00'), 'demo-mdp-pago-72', 'demo-mdp-payout-72', NULL, NULL, @base);

-- R73. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 3 DAY);
SET @n = 3;
SET @p = 61000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a34, @c35);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-73', NULL, 'anfitrion', @base, 0);

-- R74. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -103 DAY);
SET @n = 5;
SET @p = 35500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a35, @c21);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -97 DAY), '09:00:00'), 'demo-mdp-pago-74', 'demo-mdp-payout-74', NULL, NULL, @base);

-- R75. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -84 DAY);
SET @n = 3;
SET @p = 35500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -92 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a35, @c48);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -92 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -90 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -91 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -80 DAY), '09:00:00'), 'demo-mdp-pago-75', 'demo-mdp-payout-75', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Muy limpio, cómodo y con todo lo necesario. Súper recomendable.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -80 DAY), '19:00:00'));

-- R76. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 18 DAY);
SET @n = 4;
SET @p = 35500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a35, @c47);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-76', NULL, 'anfitrion', @base, 0);

-- R77. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 6 DAY);
SET @n = 3;
SET @p = 146500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a36, @c16);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-77', NULL, NULL, NULL, NULL);

-- R78. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -119 DAY);
SET @n = 3;
SET @p = 47000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a37, @c7);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '09:00:00'), 'demo-mdp-pago-78', 'demo-mdp-payout-78', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '19:00:00'));

-- R79. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -111 DAY);
SET @n = 5;
SET @p = 47000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a37, @c41);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '09:00:00'), 'demo-mdp-pago-79', 'demo-mdp-payout-79', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -104 DAY), '19:00:00'));

-- R80. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -135 DAY);
SET @n = 5;
SET @p = 114000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -146 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a39, @c39);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -146 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '09:00:00'), 'demo-mdp-pago-80', 'demo-mdp-payout-80', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Buena ubicación y muy cómodo. Faltaban algunos utensilios de cocina.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '19:00:00'));

-- R81. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -133 DAY);
SET @n = 2;
SET @p = 65500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a40, @c5);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -143 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -130 DAY), '09:00:00'), 'demo-mdp-pago-81', 'demo-mdp-payout-81', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 3, 'Correcto por el precio, aunque le falta mantenimiento.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '19:00:00'));

-- R82. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -111 DAY);
SET @n = 3;
SET @p = 65500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a40, @c9);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '09:00:00'), 'demo-mdp-pago-82', 'demo-mdp-payout-82', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy buena experiencia. La calefacción tardaba en calentar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '19:00:00'));

-- R83. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 19 DAY);
SET @n = 7;
SET @p = 65500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a40, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-83', NULL, 'anfitrion', @base, 0);

-- R84. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -106 DAY);
SET @n = 4;
SET @p = 95000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a41, @c25);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -101 DAY), '09:00:00'), 'demo-mdp-pago-84', 'demo-mdp-payout-84', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente lugar para pasar unos días en Mar del Plata. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -101 DAY), '19:00:00'));

-- R85. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -86 DAY);
SET @n = 2;
SET @p = 95000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a41, @c49);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -93 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -94 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -83 DAY), '09:00:00'), 'demo-mdp-pago-85', 'demo-mdp-payout-85', NULL, NULL, @base);

-- R86. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 14 DAY);
SET @n = 3;
SET @p = 95000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a41, @c5);
SET @r = LAST_INSERT_ID();

-- R87. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -101 DAY);
SET @n = 3;
SET @p = 45500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a42, @c18);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '16:30:00'), NULL, 'demo-mdp-pago-87', NULL, 'cliente_anticipada', @base, 0);

-- R88. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -86 DAY);
SET @n = 7;
SET @p = 45500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a42, @c48);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -93 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -94 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -78 DAY), '09:00:00'), 'demo-mdp-pago-88', 'demo-mdp-payout-88', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy lindo y bien equipado. El wifi se cortaba de a ratos.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -76 DAY), '19:00:00'));

-- R89. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -117 DAY);
SET @n = 2;
SET @p = 79500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a43, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -121 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '09:00:00'), 'demo-mdp-pago-89', 'demo-mdp-payout-89', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '19:00:00'));

-- R90. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 14 DAY);
SET @n = 2;
SET @p = 79500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a43, @c33);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-90', NULL, 'anfitrion', @base, 0);

-- R91. Cancelada: el cliente nunca pagó (pago vencido)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -102 DAY);
SET @n = 4;
SET @p = 45500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a44, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'vencido', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), NULL, NULL, NULL, NULL, 'vencimiento_pago', NULL, NULL);

-- R92. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -102 DAY);
SET @n = 3;
SET @p = 60500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a45, @c16);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '09:00:00'), 'demo-mdp-pago-92', 'demo-mdp-payout-92', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Buena ubicación y muy cómodo. Faltaban algunos utensilios de cocina.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '19:00:00'));

-- R93. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -87 DAY);
SET @n = 3;
SET @p = 60500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a45, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -94 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '16:30:00'), NULL, 'demo-mdp-pago-93', NULL, 'cliente_anticipada', @base, 0);

-- R94. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -140 DAY);
SET @n = 3;
SET @p = 107500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -151 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a46, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -151 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -149 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -150 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '09:00:00'), 'demo-mdp-pago-94', 'demo-mdp-payout-94', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Buena ubicación y muy cómodo. Faltaban algunos utensilios de cocina.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '19:00:00'));

-- R95. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -121 DAY);
SET @n = 5;
SET @p = 107500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a46, @c43);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '09:00:00'), 'demo-mdp-pago-95', 'demo-mdp-payout-95', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Impecable y muy bien ubicado. Bajábamos caminando a la playa todos los días.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '19:00:00'));

-- R96. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -100 DAY);
SET @n = 4;
SET @p = 49000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a47, @c2);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '09:00:00'), 'demo-mdp-pago-96', 'demo-mdp-payout-96', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy buena experiencia. La calefacción tardaba en calentar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -93 DAY), '19:00:00'));

-- R97. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -79 DAY);
SET @n = 5;
SET @p = 49000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -85 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a47, @c2);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -85 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -83 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -84 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -73 DAY), '09:00:00'), 'demo-mdp-pago-97', 'demo-mdp-payout-97', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -73 DAY), '19:00:00'));

-- R98. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 25 DAY);
SET @n = 4;
SET @p = 49000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a47, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-98', NULL, 'anfitrion', @base, 0);

-- R99. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 34 DAY);
SET @n = 2;
SET @p = 49000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a47, @c28);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-99', NULL, NULL, NULL, NULL);

-- R100. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -121 DAY);
SET @n = 4;
SET @p = 81000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -133 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a48, @c18);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -133 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -131 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -132 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '09:00:00'), 'demo-mdp-pago-100', 'demo-mdp-payout-100', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Muy buena experiencia. La calefacción tardaba en calentar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '19:00:00'));

-- R101. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 25 DAY);
SET @n = 7;
SET @p = 81000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a48, @c28);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '16:30:00'), NULL, 'demo-mdp-pago-101', NULL, NULL, NULL, NULL);

-- R102. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -130 DAY);
SET @n = 2;
SET @p = 132500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -138 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a49, @c6);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -138 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -137 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '09:00:00'), 'demo-mdp-pago-102', 'demo-mdp-payout-102', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente lugar para pasar unos días en Mar del Plata. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '19:00:00'));

-- R103. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 17 DAY);
SET @n = 5;
SET @p = 132500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a49, @c29);
SET @r = LAST_INSERT_ID();

-- R104. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -115 DAY);
SET @n = 3;
SET @p = 30500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a50, @c9);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '09:00:00'), 'demo-mdp-pago-104', 'demo-mdp-payout-104', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Buena ubicación y muy cómodo. Faltaban algunos utensilios de cocina.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '19:00:00'));

-- R105. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -88 DAY);
SET @n = 3;
SET @p = 30500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a50, @c30);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -99 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -84 DAY), '09:00:00'), 'demo-mdp-pago-105', 'demo-mdp-payout-105', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Todo tal cual las fotos. El anfitrión respondió rapidísimo.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -82 DAY), '19:00:00'));

-- R106. Confirmada y pagada: estadía en curso o recién terminada (el anfitrión puede finalizarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -1 DAY);
SET @n = 5;
SET @p = 30500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a50, @c18);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '16:30:00'), NULL, 'demo-mdp-pago-106', NULL, NULL, NULL, NULL);

-- R107. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 9 DAY);
SET @n = 3;
SET @p = 30500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a50, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '16:30:00'), NULL, 'demo-mdp-pago-107', NULL, NULL, NULL, NULL);

-- R108. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -132 DAY);
SET @n = 3;
SET @p = 158500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a51, @c37);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '09:00:00'), 'demo-mdp-pago-108', 'demo-mdp-payout-108', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 3, 'La ubicación es buena pero el lugar es más chico de lo que parece en las fotos.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '19:00:00'));

-- R109. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -121 DAY);
SET @n = 5;
SET @p = 158500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a51, @c19);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '09:00:00'), 'demo-mdp-pago-109', 'demo-mdp-payout-109', NULL, NULL, @base);

-- R110. Confirmada y pagada: estadía en curso o recién terminada (el anfitrión puede finalizarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -1 DAY);
SET @n = 4;
SET @p = 158500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a51, @c3);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '16:30:00'), NULL, 'demo-mdp-pago-110', NULL, NULL, NULL, NULL);

-- R111. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 13 DAY);
SET @n = 3;
SET @p = 158500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a51, @c28);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '16:30:00'), NULL, 'demo-mdp-pago-111', NULL, NULL, NULL, NULL);

-- R112. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 22 DAY);
SET @n = 4;
SET @p = 53500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a52, @c37);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-112', NULL, 'anfitrion', @base, 0);

-- R113. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -100 DAY);
SET @n = 3;
SET @p = 89500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a53, @c46);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '09:00:00'), 'demo-mdp-pago-113', 'demo-mdp-payout-113', NULL, NULL, @base);

-- R114. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -83 DAY);
SET @n = 7;
SET @p = 89500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -89 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a53, @c35);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -89 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -87 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -88 DAY), '16:30:00'), NULL, 'demo-mdp-pago-114', NULL, 'cliente_anticipada', @base, 0);

-- R115. Solicitada: espera que el anfitrión la confirme o rechace
SET @ini = DATE_ADD(CURDATE(), INTERVAL 3 DAY);
SET @n = 7;
SET @p = 89500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('solicitada', NULL, @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a53, @c8);
SET @r = LAST_INSERT_ID();

-- R116. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -136 DAY);
SET @n = 4;
SET @p = 83500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -147 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a54, @c36);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -147 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -146 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '09:00:00'), 'demo-mdp-pago-116', 'demo-mdp-payout-116', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R117. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -111 DAY);
SET @n = 7;
SET @p = 56500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a55, @c4);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -103 DAY), '09:00:00'), 'demo-mdp-pago-117', 'demo-mdp-payout-117', NULL, NULL, @base);

-- R118. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -98 DAY);
SET @n = 5;
SET @p = 56500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a55, @c22);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '16:30:00'), NULL, 'demo-mdp-pago-118', NULL, 'cliente_anticipada', @base, 0);

-- R119. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 25 DAY);
SET @n = 4;
SET @p = 56500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a55, @c19);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '16:30:00'), NULL, 'demo-mdp-pago-119', NULL, NULL, NULL, NULL);

-- R120. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 34 DAY);
SET @n = 2;
SET @p = 56500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a55, @c24);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-120', NULL, 'anfitrion', @base, 0);

-- R121. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -118 DAY);
SET @n = 5;
SET @p = 79000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a56, @c43);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '09:00:00'), 'demo-mdp-pago-121', 'demo-mdp-payout-121', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 3, 'Correcto por el precio, aunque le falta mantenimiento.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '19:00:00'));

-- R122. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -101 DAY);
SET @n = 7;
SET @p = 60500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a57, @c20);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -106 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -107 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -93 DAY), '09:00:00'), 'demo-mdp-pago-122', 'demo-mdp-payout-122', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Ideal para ir en familia. Los chicos la pasaron genial.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -92 DAY), '19:00:00'));

-- R123. Confirmada y pagada: estadía en curso o recién terminada (el anfitrión puede finalizarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -1 DAY);
SET @n = 5;
SET @p = 60500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a57, @c42);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '16:30:00'), NULL, 'demo-mdp-pago-123', NULL, NULL, NULL, NULL);

-- R124. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 12 DAY);
SET @n = 5;
SET @p = 60500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a57, @c29);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '16:30:00'), NULL, 'demo-mdp-pago-124', NULL, NULL, NULL, NULL);

-- R125. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 30 DAY);
SET @n = 3;
SET @p = 60500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a57, @c27);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-125', NULL, NULL, NULL, NULL);

-- R126. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -131 DAY);
SET @n = 4;
SET @p = 63000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a58, @c22);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -139 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '09:00:00'), 'demo-mdp-pago-126', 'demo-mdp-payout-126', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Ideal para ir en familia. Los chicos la pasaron genial.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '19:00:00'));

-- R127. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -117 DAY);
SET @n = 5;
SET @p = 81000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a59, @c18);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -123 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -124 DAY), '16:30:00'), NULL, 'demo-mdp-pago-127', NULL, 'cliente_anticipada', @base, 0);

-- R128. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 7 DAY);
SET @n = 3;
SET @p = 81000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a59, @c28);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-128', NULL, 'anfitrion', @base, 0);

-- R129. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -127 DAY);
SET @n = 4;
SET @p = 84500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a62, @c42);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -136 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -134 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '09:00:00'), 'demo-mdp-pago-129', 'demo-mdp-payout-129', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Ideal para ir en familia. Los chicos la pasaron genial.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -121 DAY), '19:00:00'));

-- R130. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 16 DAY);
SET @n = 7;
SET @p = 84500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a62, @c43);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -9 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '16:30:00'), NULL, 'demo-mdp-pago-130', NULL, NULL, NULL, NULL);

-- R131. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 35 DAY);
SET @n = 3;
SET @p = 84500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a62, @c40);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '16:30:00'), NULL, 'demo-mdp-pago-131', NULL, 'anfitrion', @base, 0);

-- R132. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -139 DAY);
SET @n = 3;
SET @p = 68500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a63, @c37);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -145 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -143 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -144 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '09:00:00'), 'demo-mdp-pago-132', 'demo-mdp-payout-132', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 3, 'La ubicación es buena pero el lugar es más chico de lo que parece en las fotos.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -135 DAY), '19:00:00'));

-- R133. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -118 DAY);
SET @n = 4;
SET @p = 68500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a63, @c34);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -125 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -126 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '09:00:00'), 'demo-mdp-pago-133', 'demo-mdp-payout-133', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R134. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 24 DAY);
SET @n = 5;
SET @p = 68500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a63, @c15);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '16:30:00'), NULL, 'demo-mdp-pago-134', NULL, 'anfitrion', @base, 0);

-- R135. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 52 DAY);
SET @n = 4;
SET @p = 68500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a63, @c3);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '16:30:00'), NULL, 'demo-mdp-pago-135', NULL, NULL, NULL, NULL);

-- R136. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -113 DAY);
SET @n = 2;
SET @p = 82500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a64, @c49);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '09:00:00'), 'demo-mdp-pago-136', 'demo-mdp-payout-136', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Muy limpio, cómodo y con todo lo necesario. Súper recomendable.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '19:00:00'));

-- R137. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 21 DAY);
SET @n = 3;
SET @p = 82500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a64, @c35);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '16:30:00'), NULL, 'demo-mdp-pago-137', NULL, 'anfitrion', @base, 0);

-- R138. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -123 DAY);
SET @n = 4;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -131 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a65, @c36);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -131 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -130 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '09:00:00'), 'demo-mdp-pago-138', 'demo-mdp-payout-138', NULL, NULL, @base);

-- R139. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -106 DAY);
SET @n = 2;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a65, @c36);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -106 DAY), '09:00:00'), 'demo-mdp-pago-139', 'demo-mdp-payout-139', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R140. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 3 DAY);
SET @n = 5;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a65, @c44);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-140', NULL, 'anfitrion', @base, 0);

-- R141. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -101 DAY);
SET @n = 5;
SET @p = 78500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a66, @c26);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -113 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -112 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -95 DAY), '09:00:00'), 'demo-mdp-pago-141', 'demo-mdp-payout-141', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Lindo lugar, el check-in fue fácil. Algo de ruido de la calle a la noche.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -94 DAY), '19:00:00'));

-- R142. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -90 DAY);
SET @n = 4;
SET @p = 78500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a66, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -97 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -85 DAY), '09:00:00'), 'demo-mdp-pago-142', 'demo-mdp-payout-142', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente lugar para pasar unos días en Mar del Plata. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -84 DAY), '19:00:00'));

-- R143. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 18 DAY);
SET @n = 3;
SET @p = 78500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a66, @c42);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -8 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -7 DAY), '16:30:00'), NULL, 'demo-mdp-pago-143', NULL, NULL, NULL, NULL);

-- R144. Confirmada con pago pendiente (vence en 22 h)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 42 DAY);
SET @n = 5;
SET @p = 78500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -26 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a66, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -26 HOUR), DATE_ADD(NOW(), INTERVAL 22 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R145. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -109 DAY);
SET @n = 3;
SET @p = 124500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a67, @c5);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -118 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -117 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -105 DAY), '09:00:00'), 'demo-mdp-pago-145', 'demo-mdp-payout-145', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Muy limpio, cómodo y con todo lo necesario. Súper recomendable.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -103 DAY), '19:00:00'));

-- R146. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -96 DAY);
SET @n = 2;
SET @p = 124500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -102 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a67, @c18);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -102 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -101 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '09:00:00'), 'demo-mdp-pago-146', 'demo-mdp-payout-146', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R147. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 11 DAY);
SET @n = 5;
SET @p = 124500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a67, @c9);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '16:30:00'), NULL, 'demo-mdp-pago-147', NULL, NULL, NULL, NULL);

-- R148. Finalizada y liquidada, sin reseña (el cliente puede dejarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -112 DAY);
SET @n = 3;
SET @p = 50500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a68, @c21);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -122 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -120 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -121 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -108 DAY), '09:00:00'), 'demo-mdp-pago-148', 'demo-mdp-payout-148', NULL, NULL, @base);

-- R149. Confirmada y pagada: estadía en curso o recién terminada (el anfitrión puede finalizarla)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -2 DAY);
SET @n = 4;
SET @p = 50500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a68, @c30);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -12 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -10 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -11 DAY), '16:30:00'), NULL, 'demo-mdp-pago-149', NULL, NULL, NULL, NULL);

-- R150. Cancelada por el cliente con menos de 48 h (65% reembolso, 35% al anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -119 DAY);
SET @n = 3;
SET @p = 88000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a69, @c25);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_parcial', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -127 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -119 DAY), '09:00:00'), 'demo-mdp-pago-150', 'demo-mdp-payout-150', 'cliente_tardia', @base - ROUND(@base * 0.35, 2), ROUND(@base * 0.35, 2));

-- R151. Cancelada por el cliente con anticipación (reembolso total)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -129 DAY);
SET @n = 3;
SET @p = 31000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a70, @c31);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -139 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '16:30:00'), NULL, 'demo-mdp-pago-151', NULL, 'cliente_anticipada', @base, 0);

-- R152. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -99 DAY);
SET @n = 7;
SET @p = 31000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a70, @c21);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -111 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -109 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -110 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -91 DAY), '09:00:00'), 'demo-mdp-pago-152', 'demo-mdp-payout-152', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Excelente lugar para pasar unos días en Mar del Plata. Volveríamos sin dudar.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -91 DAY), '19:00:00'));

-- R153. Cancelada: el cliente nunca pagó (pago vencido)
SET @ini = DATE_ADD(CURDATE(), INTERVAL -133 DAY);
SET @n = 5;
SET @p = 74000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a71, @c26);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'vencido', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -142 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '10:00:00'), NULL, NULL, NULL, NULL, 'vencimiento_pago', NULL, NULL);

-- R154. Confirmada con pago pendiente (vence en 36 h)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 19 DAY);
SET @n = 3;
SET @p = 74000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', DATE_ADD(NOW(), INTERVAL -12 HOUR), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a71, @c45);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pendiente', @base, @cplat, @cmp, @base + @cplat + @cmp, DATE_ADD(NOW(), INTERVAL -12 HOUR), DATE_ADD(NOW(), INTERVAL 36 HOUR), NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- R155. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -132 DAY);
SET @n = 2;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a73, @c32);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -141 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -139 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -140 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -129 DAY), '09:00:00'), 'demo-mdp-pago-155', 'demo-mdp-payout-155', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 4, 'Lindo lugar, el check-in fue fácil. Algo de ruido de la calle a la noche.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -128 DAY), '19:00:00'));

-- R156. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 11 DAY);
SET @n = 3;
SET @p = 38500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a73, @c1);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 0 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -1 DAY), '16:30:00'), NULL, 'demo-mdp-pago-156', NULL, NULL, NULL, NULL);

-- R157. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -104 DAY);
SET @n = 3;
SET @p = 86000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a74, @c20);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -116 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -114 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -115 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '09:00:00'), 'demo-mdp-pago-157', 'demo-mdp-payout-157', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'La ubicación es inmejorable y el lugar es hermoso. Nos sentimos como en casa.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -100 DAY), '19:00:00'));

-- R158. Finalizada y liquidada, con reseña
SET @ini = DATE_ADD(CURDATE(), INTERVAL -92 DAY);
SET @n = 5;
SET @p = 86000;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('finalizada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a74, @c33);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -98 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -96 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -97 DAY), '16:30:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -86 DAY), '09:00:00'), 'demo-mdp-pago-158', 'demo-mdp-payout-158', NULL, NULL, @base);
INSERT INTO review (id_reserva, puntuacion, comentario, fecha) VALUES (@r, 5, 'Muy limpio, cómodo y con todo lo necesario. Súper recomendable.', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -85 DAY), '19:00:00'));

-- R159. Cancelada por el anfitrión (reembolso total al cliente)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 13 DAY);
SET @n = 4;
SET @p = 114500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('cancelada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a75, @c31);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'reembolsado_total', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '16:30:00'), NULL, 'demo-mdp-pago-159', NULL, 'anfitrion', @base, 0);

-- R160. Confirmada y pagada, estadía futura (cobro pendiente del anfitrión)
SET @ini = DATE_ADD(CURDATE(), INTERVAL 32 DAY);
SET @n = 3;
SET @p = 114500;
INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente) VALUES ('confirmada', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), @ini, DATE_ADD(@ini, INTERVAL @n DAY), @p, @n * @p, @a75, @c45);
SET @r = LAST_INSERT_ID();
SET @base = ROUND(@n * @p, 2);
SET @cplat = ROUND(@base * 0.05, 2);
SET @cmp = ROUND(@base * 0.06, 2);
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente, fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo, id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion) VALUES (@r, 'pagado', @base, @cplat, @cmp, @base + @cplat + @cmp, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -6 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -4 DAY), '10:00:00'), TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '16:30:00'), NULL, 'demo-mdp-pago-160', NULL, NULL, NULL, NULL);

COMMIT;

-- Resumen de lo cargado
SELECT 'anfitriones' AS dato, COUNT(*) AS cantidad FROM usuario WHERE email LIKE '%.mdp.alojame@gmail.com' AND tipo = 'anfitrion'
UNION ALL SELECT 'clientes', COUNT(*) FROM usuario WHERE email LIKE '%.mdp.alojame@gmail.com' AND tipo = 'cliente'
UNION ALL SELECT 'alojamientos', COUNT(*) FROM alojamiento a JOIN usuario u ON u.id_usuario = a.id_anfitrion WHERE u.email LIKE '%.mdp.alojame@gmail.com'
UNION ALL SELECT 'reservas', COUNT(*) FROM reserva r JOIN usuario u ON u.id_usuario = r.id_cliente WHERE u.email LIKE '%.mdp.alojame@gmail.com'
UNION ALL SELECT 'resenas', COUNT(*) FROM review v JOIN reserva r ON r.id_reserva = v.id_reserva JOIN usuario u ON u.id_usuario = r.id_cliente WHERE u.email LIKE '%.mdp.alojame@gmail.com';
