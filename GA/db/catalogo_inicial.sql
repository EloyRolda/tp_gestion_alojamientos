-- =====================================================================
-- AlojaME -- catalogo inicial
--
-- Carga los tipos de alojamiento y los servicios/comodidades mas comunes, para
-- no tener que darlos de alta a mano desde el panel de administracion.
-- A diferencia de datos_de_prueba.sql, esto SI es para dejar en produccion:
-- borrar_datos_de_prueba.sql no lo toca.
--
-- Los nombres siguen el mismo formato que usa el backend al crearlos
-- ("Primera letra en mayuscula, resto en minuscula").
-- INSERT IGNORE: si alguno ya existe (con o sin tilde, en mayuscula o
-- minuscula), se saltea, asi que se puede correr las veces que haga falta.
-- Despues se pueden renombrar, fusionar o borrar desde "Catalogo de tipos" y
-- "Catalogo de servicios".
--
-- Uso (con el backend arrancado al menos una vez):
--   mysql -u alojame -p alojame < catalogo_inicial.sql
-- =====================================================================

SET NAMES utf8mb4;
START TRANSACTION;

-- ---------------------------------------------------------------- tipos de alojamiento (58)

-- Departamentos y similares
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES
  ('Departamento'),
  ('Monoambiente'),
  ('Loft'),
  ('Estudio'),
  ('Dúplex'),
  ('Tríplex'),
  ('Penthouse'),
  ('Propiedad horizontal'),
  ('Departamento con servicios');

-- Casas
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES
  ('Casa'),
  ('Casa de campo'),
  ('Casa de playa'),
  ('Casa de montaña'),
  ('Casa en barrio cerrado'),
  ('Casa quinta'),
  ('Quinta'),
  ('Chalet'),
  ('Villa'),
  ('Mansión'),
  ('Casa de huéspedes'),
  ('Casa adosada');

-- Campo y naturaleza
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES
  ('Cabaña'),
  ('Complejo de cabañas'),
  ('Bungalow'),
  ('Chacra'),
  ('Estancia'),
  ('Granja'),
  ('Finca'),
  ('Rancho'),
  ('Refugio de montaña'),
  ('Lodge');

-- Hotelería
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES
  ('Hotel'),
  ('Hotel boutique'),
  ('Apart hotel'),
  ('Hostería'),
  ('Posada'),
  ('Bed and breakfast'),
  ('Hostel'),
  ('Albergue'),
  ('Pensión'),
  ('Motel'),
  ('Resort'),
  ('Suite');

-- Habitaciones
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES
  ('Habitación privada'),
  ('Habitación compartida');

-- Alternativos y al aire libre
INSERT IGNORE INTO tipo_alojamiento (nombre_tipo) VALUES
  ('Camping'),
  ('Glamping'),
  ('Domo'),
  ('Tiny house'),
  ('Casa container'),
  ('Casa del árbol'),
  ('Casa rodante'),
  ('Motorhome'),
  ('Casa flotante'),
  ('Barco'),
  ('Casa cueva'),
  ('Castillo'),
  ('Faro');

-- ---------------------------------------------------------------- servicios (64)

-- Básicos
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Wifi'),
  ('Agua caliente'),
  ('Calefacción'),
  ('Aire acondicionado'),
  ('Ventilador de techo'),
  ('Ropa de cama y toallas'),
  ('Plancha'),
  ('Secador de pelo');

-- Cocina
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Cocina equipada'),
  ('Heladera'),
  ('Microondas'),
  ('Horno'),
  ('Cafetera'),
  ('Lavavajillas'),
  ('Vajilla y utensilios');

-- Lavado
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Lavarropas'),
  ('Secarropas'),
  ('Tendedero');

-- Entretenimiento y trabajo
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Televisor smart'),
  ('Streaming'),
  ('Juegos de mesa'),
  ('Escritorio para trabajar'),
  ('Bicicletas');

-- Exteriores
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Pileta'),
  ('Pileta climatizada'),
  ('Jacuzzi'),
  ('Parrilla'),
  ('Quincho'),
  ('Jardín'),
  ('Patio'),
  ('Balcón'),
  ('Terraza'),
  ('Deck'),
  ('Ducha exterior'),
  ('Hogar a leña');

-- Bienestar
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Sauna'),
  ('Gimnasio'),
  ('Spa');

-- Estacionamiento y acceso
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Estacionamiento'),
  ('Cochera cubierta'),
  ('Ascensor'),
  ('Accesible en silla de ruedas'),
  ('Check-in autónomo');

-- Familia y mascotas
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Apto para niños'),
  ('Cuna'),
  ('Silla alta para bebés'),
  ('Admite mascotas');

-- Seguridad
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Detector de humo'),
  ('Detector de monóxido de carbono'),
  ('Extintor'),
  ('Botiquín de primeros auxilios'),
  ('Caja fuerte'),
  ('Alarma'),
  ('Seguridad 24 hs'),
  ('Portero');

-- Ubicación y vistas
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Vista al mar'),
  ('Vista a la montaña'),
  ('Vista al lago'),
  ('Acceso a la playa'),
  ('Frente al lago'),
  ('Centro de la ciudad');

-- Incluidos en la estadía
INSERT IGNORE INTO servicio (nombre_servicio) VALUES
  ('Desayuno incluido'),
  ('Limpieza diaria'),
  ('Traslado al aeropuerto');

COMMIT;

SELECT 'tipos de alojamiento' AS catalogo, COUNT(*) AS cantidad FROM tipo_alojamiento
UNION ALL SELECT 'servicios', COUNT(*) FROM servicio;
