/// Mejoras detectadas en la auditoria del frontend que necesitan cambios en el
/// backend. Quedan anotadas para mas adelante: por ahora el backend no se toca.

# Pendientes que requieren backend

Detectados al auditar el frontend (recorridos de cliente, anfitrión y administrador). Ninguno rompe la app hoy: son mejoras que el frontend no puede resolver solo porque la API no expone los datos o no tiene la operación.

| # | Pendiente | Qué haría falta en el backend | Cómo se resuelve hoy |
|---|---|---|---|
| 1 | Marcar en gris, en el calendario de reserva, las fechas que ya reservaron **otras personas** | Un endpoint público de solo lectura, por ejemplo `GET /Alojamiento/{id}/fechas-ocupadas`, que devuelva los rangos `[inicio, fin)` de las reservas no canceladas (sin datos del cliente) | El calendario marca los días pasados y las reservas propias del cliente; si las fechas chocan con una reserva ajena, el backend lo rechaza al enviar ("Ya existe una reserva en esas fechas") |
| 2 | Filtrar la búsqueda por fechas disponibles | El mismo dato del punto 1, o un filtro `desde`/`hasta` en `GET /Alojamiento/listar` | La búsqueda filtra por zona, precio, capacidad, tipo y servicios, no por disponibilidad |
| 3 | "Olvidé mi contraseña" | Endpoints para pedir un enlace de recuperación por mail (token de un solo uso, como el de verificación) y para fijar la contraseña nueva | Solo se puede cambiar la contraseña desde Mi perfil, con la sesión iniciada |
| 4 | Cantidad de huéspedes en la reserva | Un campo en `Reserva` y su validación contra la capacidad del alojamiento | Se muestra la capacidad del alojamiento, pero la reserva no registra cuántas personas van |
| 5 | Avisos por mail: al anfitrión cuando le llega una solicitud y al cliente cuando se la confirman o cancelan | Envíos con el servicio de mail que ya se usa para la verificación de cuentas | Cada uno tiene que entrar a la app para ver el estado |
| 6 | No ofrecer reservar si el anfitrión fue desactivado | Que la respuesta del alojamiento indique si el anfitrión está activo (o que el listado público lo excluya) | El backend rechaza la reserva con "El anfitrion esta inactivo" |

## Valores copiados en el frontend

El diálogo de cancelación explica cuánto se devuelve usando los **valores por defecto** del backend: corte a 48 horas (`DIAS_CORTE_CANCELACION = 2`) y retención del 35% (`pago.porcentaje-retencion-tardia`). Si se cambian en el servidor, actualizar también `frontend/src/app/core/utils/cancelacion.util.ts`, o exponerlos desde la API para que el frontend los lea.
