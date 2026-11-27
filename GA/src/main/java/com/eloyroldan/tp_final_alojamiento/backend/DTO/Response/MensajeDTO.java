/// DTO de respuesta para un mensaje individual dentro de una conversación.
package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import java.time.LocalDateTime;

public record MensajeDTO(
        Long id,
        Long idAutor,
        String nombreAutor,
        String texto,
        LocalDateTime fecha,
        boolean leido,
        boolean propio
) {}
