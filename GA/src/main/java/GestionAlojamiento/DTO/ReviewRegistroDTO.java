package GestionAlojamiento.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// La resena siempre se liga a una RESERVA puntual (FINALIZADA y del cliente logueado),
/// de ahi se derivan el alojamiento y el cliente: no se piden por separado para
/// evitar que alguien resene una estadia que no es suya.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRegistroDTO {

    @Max(value = 5, message = "El valor maximo es 5")
    @Min(value = 0, message = "El valor minimo es 0")
    @NotNull(message = "Campo Obligatorio")
    private Integer puntuacion;

    @Size(max = 2000, message = "El comentario es muy largo")
    @NotBlank(message = "Campo Obligatorio")
    private String comentario;

    @NotNull(message = "Campo Obligatorio")
    private Long idReserva;
}
