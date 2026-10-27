package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// Aplanada: en vez de anidar la Reserva completa (que a su vez referencia la Review,
/// generando un ciclo), se exponen solo los datos de contexto que hacen falta.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private Long idReview;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fecha;
    private Long idReserva;
    private Long idAlojamiento;
    private String tituloAlojamiento;
    private Long idCliente;
    private String nombreCliente;

    public static ReviewResponseDTO desde(Review review) {
        return new ReviewResponseDTO(
                review.getIdReview(), review.getPuntuacion(), review.getComentario(), review.getFecha(),
                review.getReserva().getIdReserva(),
                review.getReserva().getAlojamiento().getIdAlojamiento(),
                review.getReserva().getAlojamiento().getTitulo(),
                review.getReserva().getCliente().getId(),
                review.getReserva().getCliente().getNombre());
    }
}
