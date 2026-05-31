use db_gestion_alojamientos;

select * from usuario;
-- ADMINISTRADOR
INSERT INTO usuario (nombre, email, password, telefono, fecha_registro, activo, tipo) VALUES
('Roberto Admin', 'admin@correo.com', '$2b$12$5BRMRwZBw.7Oqjz4ExSqrOqGqqd1og/PtBSyzEq4X2EjZt0bS77U6', '2230000000', NOW(), TRUE, 'ADMINISTRADOR');
select * from usuario;
INSERT INTO administrador (id_usuario, matricula) VALUES
(1, 'MAT-001');

-- ANFITRIONES
INSERT INTO usuario (nombre, email, password, telefono, fecha_registro, activo, tipo) VALUES
('Juan Perez',   'juan@correo.com',   '$2b$12$APJkMIeBLCWsochhj4/CdOMOwrIVDoNtcwkftAFGvPwKDoo7t74MW', '2234567890', NOW(), TRUE, 'ANFITRION'),
('Maria Gomez',  'maria@correo.com',  '$2b$12$APJkMIeBLCWsochhj4/CdOMOwrIVDoNtcwkftAFGvPwKDoo7t74MW', '2235551111', NOW(), TRUE, 'ANFITRION'),
('Carlos Lopez', 'carlos@correo.com', '$2b$12$APJkMIeBLCWsochhj4/CdOMOwrIVDoNtcwkftAFGvPwKDoo7t74MW', '2235552222', NOW(), TRUE, 'ANFITRION');

INSERT INTO anfitrion (id_usuario) VALUES (2), (3), (4);

-- CLIENTES
INSERT INTO usuario (nombre, email, password, telefono, fecha_registro, activo, tipo) VALUES
('Lucas Fernandez', 'lucas@correo.com', '$2b$12$XkwbnJOR4FeTAPoEkkRTzOxjTXbiU37Zk05hYICmk/DRuEcoA5oTO', '2231111111', NOW(), TRUE, 'CLIENTE'),
('Sofia Martinez',  'sofia@correo.com', '$2b$12$XkwbnJOR4FeTAPoEkkRTzOxjTXbiU37Zk05hYICmk/DRuEcoA5oTO', '2232222222', NOW(), TRUE, 'CLIENTE'),
('Diego Ruiz',      'diego@correo.com', '$2b$12$XkwbnJOR4FeTAPoEkkRTzOxjTXbiU37Zk05hYICmk/DRuEcoA5oTO', '2233333333', NOW(), TRUE, 'CLIENTE');

INSERT INTO cliente (id_usuario, metodo_pago) VALUES
(5, 'VISA'),
(6, 'MASTERCARD'),
(7, 'MERCADOPAGO');

-- DIRECCIONES
INSERT INTO direccion (pais, provincia, ciudad, codigo_postal, calle, altura) VALUES
('Argentina', 'Buenos Aires', 'Mar del Plata',   '7600', 'Av. Colon',            1500),
('Argentina', 'Buenos Aires', 'Mar del Plata',   '7600', 'Guemes',               2850),
('Argentina', 'Buenos Aires', 'Mar del Plata',   '7600', 'San Martin',           3200),
('Argentina', 'Cordoba',      'Villa Carlos Paz', '5152', 'Costanera',            800),
('Argentina', 'Rio Negro',    'Bariloche',        '8400', 'Mitre',                900),
('Argentina', 'Mendoza',      'Mendoza Capital',  '5500', 'Aristides Villanueva', 450);

-- SERVICIOS
INSERT INTO servicio (tiene_cocina, tiene_lavarropa, tiene_wifi, tiene_estacionamiento) VALUES
(TRUE,  TRUE,  TRUE,  TRUE),
(TRUE,  FALSE, TRUE,  FALSE),
(FALSE, FALSE, TRUE,  TRUE),
(TRUE,  TRUE,  TRUE,  FALSE),
(FALSE, FALSE, TRUE,  TRUE),
(TRUE,  FALSE, TRUE,  TRUE);

-- ALOJAMIENTOS
INSERT INTO alojamiento (titulo, descripcion, precio_noche, capacidad, cant_ambientes, cant_habitaciones, cant_camas, cant_banios, activo, tipo, id_servicio, id_anfitrion, id_direccion) VALUES
('Casa familiar cerca del mar',    'Casa amplia ideal para vacaciones en familia, ubicada cerca de la playa.', 65000.00, 6, 4, 3, 5, 2, TRUE, 'CASA',         1, 2, 1),
('Departamento moderno en Guemes', 'Departamento cómodo y moderno, cerca de bares, restaurantes y zona comercial.', 42000.00, 3, 2, 1, 2, 1, TRUE, 'DEPARTAMENTO', 2, 2, 2),
('Hotel Centro Mar del Plata',     'Hotel céntrico con habitaciones cómodas y buena ubicación.',              55000.00, 2, 1, 1, 1, 1, TRUE, 'HOTEL',        3, 3, 3),
('Casa con vista al lago',         'Casa cómoda en Villa Carlos Paz, ideal para descansar.',                  70000.00, 5, 4, 2, 4, 2, TRUE, 'CASA',         4, 3, 4),
('Hotel Bariloche Premium',        'Hotel con excelente ubicación, desayuno incluido y vista a la montaña.',  90000.00, 2, 1, 1, 1, 1, TRUE, 'HOTEL',        5, 4, 5),
('Departamento en Mendoza',        'Departamento luminoso cerca de la zona gastronómica.',                    48000.00, 4, 3, 2, 3, 1, TRUE, 'DEPARTAMENTO', 6, 4, 6);
SELECT id_alojamiento FROM alojamiento;
-- CASAS
INSERT INTO casa (id_alojamiento, tiene_patio, tiene_pileta, tiene_parrilla) VALUES
(1, TRUE, FALSE, TRUE),
(2, TRUE, TRUE,  TRUE);

-- DEPARTAMENTOS
INSERT INTO departamento (id_alojamiento, piso, tiene_ascensor, expensas_incluidas) VALUES
(3, 5, TRUE, TRUE),
(4, 3, TRUE, FALSE);

-- HOTELES
INSERT INTO hotel (id_alojamiento, estrellas, incluye_desayuno, servicio_limpieza) VALUES
(5, 3, TRUE, TRUE),
(6, 5, TRUE, TRUE);

-- RESERVAS PASADAS CONFIRMADAS (para poder dejar reviews)
INSERT INTO reserva (fecha_inicio, fecha_fin, precio_total, estado, id_cliente, id_alojamiento) VALUES
('2026-05-01', '2026-05-06', 325000.00, 'CONFIRMADA', 5, 1),
('2026-05-08', '2026-05-12', 168000.00, 'CONFIRMADA', 5, 2),
('2026-05-01', '2026-05-08', 385000.00, 'CONFIRMADA', 6, 3),
('2026-05-10', '2026-05-15', 350000.00, 'CONFIRMADA', 7, 4);