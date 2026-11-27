/// DTO de respuesta con toda la conversación de una reserva para quien la consulta.
package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;

import java.util.List;

public record ConversacionDTO(
        Long idReserva,
        String tituloAlojamiento,
        String nombreOtraParte,
        ESTADORESERVA estadoReserva,
        boolean puedeEscribir,
        List<MensajeDTO> mensajes
) {}
