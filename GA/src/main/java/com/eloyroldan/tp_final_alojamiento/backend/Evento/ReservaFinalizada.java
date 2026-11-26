/// Evento publicado cuando una reserva finaliza (ReservaService.finalizar).
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record ReservaFinalizada(
        Long idReserva,
        Long idCliente,
        Long idAnfitrion,
        String tituloAlojamiento
) {}
