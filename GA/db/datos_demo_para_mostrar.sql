-- =====================================================================
-- AlojaME -- reservas extra para mostrar en vivo (complemento de la demo)
--
-- La demo completa ya trae un caso de cada accion. Si en la presentacion se
-- usan y hacen falta mas, este script agrega otras:
--
--   A) 4 reservas CONFIRMADAS y PAGADAS cuya estadia ya termino (salida entre
--      ayer y hace 4 dias): el anfitrion (o el admin) puede tocar "Finalizar
--      estadia".
--   B) 3 reservas CONFIRMADAS y PAGADAS que empiezan MAÑANA: al cancelarlas
--      como cliente se aplica la regla de menos de 48 h (65% para el cliente,
--      35% para el anfitrion).
--
-- Usa alojamientos, anfitriones (con alias de cobro) y clientes de la demo,
-- eligiendo alojamientos sin otras reservas entre hace 8 dias y dentro de 5,
-- para que las fechas no se pisen. Los montos se calculan igual que en el
-- backend: base = noches x precio, + 5% plataforma + 6% MercadoPago.
--
-- Pensado para el backend con PAGOS_SIMULADOS=true: los pagos de la demo no
-- existen en MercadoPago, asi que sin esa opcion "Finalizar" y la cancelacion
-- con reembolso fallan (sin romper nada) al intentar mover la plata.
--
-- Requisito: datos_demo_completa.sql ya cargado.
-- Uso:   mysql -u alojame -p alojame < datos_demo_para_mostrar.sql
-- Las fechas son relativas al dia en que se ejecuta: correrlo el dia de la
-- demo (o la vispera). Se puede correr mas de una vez: cada vez agrega otras 7.
-- Para sacarlas: borrar_datos_demo_mar_del_plata.sql (borra toda la demo).
-- =====================================================================

SET NAMES utf8mb4;
START TRANSACTION;

-- Marca para reconocer, mas abajo, las reservas que agrega esta corrida.
SET @marca = TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 20 DAY), '10:07:00');

INSERT INTO reserva (estado_reserva, fecha_confirmacion, fecha_inicio, fecha_fin, precio_noche_pactado, precio_total, id_alojamiento, id_cliente)
SELECT
  'confirmada',
  @marca,
  CASE WHEN a.n <= 4 THEN DATE_SUB(CURDATE(), INTERVAL (a.n + 3) DAY) ELSE DATE_ADD(CURDATE(), INTERVAL 1 DAY) END,
  CASE WHEN a.n <= 4 THEN DATE_SUB(CURDATE(), INTERVAL a.n DAY) ELSE DATE_ADD(CURDATE(), INTERVAL 4 DAY) END,
  a.precio_noche,
  ROUND(3 * a.precio_noche, 2),
  a.id_alojamiento,
  c.id_usuario
FROM (
  SELECT al.id_alojamiento, al.precio_noche, ROW_NUMBER() OVER (ORDER BY al.id_alojamiento) AS n
  FROM alojamiento al
  JOIN usuario an ON an.id_usuario = al.id_anfitrion
  WHERE an.email LIKE '%.mdp.alojame@gmail.com'
    AND an.activo = 1
    AND an.alias_cobro IS NOT NULL
    AND al.activo = 1
    AND NOT EXISTS (
      SELECT 1 FROM reserva r
      WHERE r.id_alojamiento = al.id_alojamiento
        AND r.estado_reserva <> 'cancelada'
        AND r.fecha_inicio < DATE_ADD(CURDATE(), INTERVAL 5 DAY)
        AND r.fecha_fin > DATE_SUB(CURDATE(), INTERVAL 8 DAY)
    )
) a
JOIN (
  SELECT id_usuario, ROW_NUMBER() OVER (ORDER BY id_usuario) AS n
  FROM usuario
  WHERE email LIKE '%.mdp.alojame@gmail.com' AND tipo = 'cliente' AND activo = 1
) c ON c.n = a.n
WHERE a.n <= 7;

-- Un pago PAGADO por cada reserva recien agregada (las que tienen la marca y
-- todavia no tienen pago).
INSERT INTO pago (id_reserva, estado, monto_base, comision_plataforma, comision_mercadopago, monto_total_cliente,
                  fecha_creacion, fecha_limite_pago, fecha_pago, fecha_transferencia, id_pago_externo,
                  id_transferencia_externa, motivo_cancelacion, monto_reembolsado_cliente, monto_retenido_anfitrion)
SELECT
  r.id_reserva,
  'pagado',
  x.base,
  ROUND(x.base * 0.05, 2),
  ROUND(x.base * 0.06, 2),
  x.base + ROUND(x.base * 0.05, 2) + ROUND(x.base * 0.06, 2),
  @marca,
  DATE_ADD(@marca, INTERVAL 2 DAY),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 19 DAY), '16:30:00'),
  NULL,
  CONCAT('demo-mostrar-', r.id_reserva),
  NULL, NULL, NULL, NULL
FROM reserva r
JOIN (
  SELECT id_reserva, ROUND(DATEDIFF(fecha_fin, fecha_inicio) * precio_noche_pactado, 2) AS base
  FROM reserva
) x ON x.id_reserva = r.id_reserva
WHERE r.fecha_confirmacion = @marca
  AND r.estado_reserva = 'confirmada'
  AND NOT EXISTS (SELECT 1 FROM pago p WHERE p.id_reserva = r.id_reserva);

COMMIT;

-- Resumen: las reservas listas para mostrar (las de hoy que todavia no se
-- usaron) y con que cuenta entrar para cada una.
SELECT
  r.id_reserva AS reserva,
  CASE WHEN r.fecha_fin < CURDATE() THEN 'Para FINALIZAR' ELSE 'Para CANCELAR (menos de 48 h)' END AS para,
  al.titulo AS alojamiento,
  r.fecha_inicio AS llegada,
  r.fecha_fin AS salida,
  an.email AS anfitrion,
  cl.email AS cliente
FROM reserva r
JOIN pago p ON p.id_reserva = r.id_reserva
JOIN alojamiento al ON al.id_alojamiento = r.id_alojamiento
JOIN usuario an ON an.id_usuario = al.id_anfitrion
JOIN usuario cl ON cl.id_usuario = r.id_cliente
WHERE r.fecha_confirmacion = @marca AND p.id_pago_externo LIKE 'demo-mostrar-%'
  AND r.estado_reserva = 'confirmada'  -- solo las que todavia se pueden usar
ORDER BY para, r.id_reserva;
