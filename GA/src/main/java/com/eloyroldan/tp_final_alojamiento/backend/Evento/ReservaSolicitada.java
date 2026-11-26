/// Evento publicado cuando un cliente crea una reserva (ReservaService.crear).
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record ReservaSolicitada(
        Long idReserva,
        Long idCliente,
        Long idAnfitrion,
        String tituloAlojamiento
) {}
