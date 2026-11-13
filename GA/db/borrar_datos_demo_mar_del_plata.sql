-- =====================================================================
-- AlojaME -- borrar los datos de demostracion de Mar del Plata
--
-- Elimina todo lo que cargo datos_demo_completa.sql (o la demo anterior,
-- datos_demo_mar_del_plata.sql): todos los usuarios de la
-- demo (emails terminados en .mdp.alojame@gmail.com), sus alojamientos, direcciones,
-- galerias, imagenes, servicios asignados, reservas, pagos y resenas.
-- Tambien borra las reservas que OTROS usuarios hayan hecho sobre esos
-- alojamientos. No toca los tipos de alojamiento ni los servicios del
-- catalogo (pueden seguir sirviendo), ni a ningun otro usuario.
--
-- Uso: mysql -u alojame -p alojame < borrar_datos_demo_mar_del_plata.sql
-- =====================================================================

START TRANSACTION;

CREATE TEMPORARY TABLE tmp_usuarios AS
  SELECT id_usuario FROM usuario WHERE email LIKE '%.mdp.alojame@gmail.com';
CREATE TEMPORARY TABLE tmp_alojamientos AS
  SELECT id_alojamiento, id_direccion FROM alojamiento WHERE id_anfitrion IN (SELECT id_usuario FROM tmp_usuarios);
CREATE TEMPORARY TABLE tmp_reservas AS
  SELECT id_reserva FROM reserva
  WHERE id_cliente IN (SELECT id_usuario FROM tmp_usuarios)
     OR id_alojamiento IN (SELECT id_alojamiento FROM tmp_alojamientos);

DELETE FROM review WHERE id_reserva IN (SELECT id_reserva FROM tmp_reservas);
DELETE FROM pago WHERE id_reserva IN (SELECT id_reserva FROM tmp_reservas);
DELETE FROM reserva WHERE id_reserva IN (SELECT id_reserva FROM tmp_reservas);
DELETE FROM imagen WHERE id_galeria IN (SELECT id_galeria FROM galeria WHERE id_alojamiento IN (SELECT id_alojamiento FROM tmp_alojamientos));
DELETE FROM galeria WHERE id_alojamiento IN (SELECT id_alojamiento FROM tmp_alojamientos);
DELETE FROM servicio_x_alojamiento WHERE id_alojamiento IN (SELECT id_alojamiento FROM tmp_alojamientos);
DELETE FROM alojamiento WHERE id_alojamiento IN (SELECT id_alojamiento FROM tmp_alojamientos);
DELETE FROM direccion WHERE id_direccion IN (SELECT id_direccion FROM tmp_alojamientos);
DELETE FROM token_verificacion WHERE id_usuario IN (SELECT id_usuario FROM tmp_usuarios);
DELETE FROM usuario WHERE id_usuario IN (SELECT id_usuario FROM tmp_usuarios);

COMMIT;

SELECT 'usuarios de la demo restantes' AS dato, COUNT(*) AS cantidad FROM usuario WHERE email LIKE '%.mdp.alojame@gmail.com';
