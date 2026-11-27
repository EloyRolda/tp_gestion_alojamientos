/// Datos mínimos de una reserva para armar la conversación de mensajes.
package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;

public record ParticipantesReservaDTO(
        Long idReserva,
        Long idCliente,
        String nombreCliente,
        Long idAnfitrion,
        String nombreAnfitrion,
        String tituloAlojamiento,
        ESTADORESERVA estado
) {}
