/// Evento que se publica cuando un participante envía un mensaje en una reserva.
/// Lo va a usar SCRUM-87 para avisar al destinatario.
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record MensajeRecibido(
        Long idMensaje,
        Long idReserva,
        Long idDestinatario,
        boolean destinatarioEsAnfitrion,
        String nombreAutor,
        String tituloAlojamiento
) {}
