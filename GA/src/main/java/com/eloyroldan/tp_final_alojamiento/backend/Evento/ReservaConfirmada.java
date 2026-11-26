/// Evento publicado cuando el anfitrion confirma una reserva (ReservaService.confirmar).
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record ReservaConfirmada(
        Long idReserva,
        Long idCliente,
        Long idAnfitrion,
        String tituloAlojamiento
) {}
