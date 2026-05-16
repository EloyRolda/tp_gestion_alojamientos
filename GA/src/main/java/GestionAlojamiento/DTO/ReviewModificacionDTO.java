package GestionAlojamiento.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ReviewModificacionDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;

    @Max(value = 5, message = "El valor maximo es 5")
    @Min(value = 0, message = "El valor minimo es 0")
    private Integer puntuacion;

    @Size(max = 2000, message = "El comentario es muy largo")
    private String comentario;
}
