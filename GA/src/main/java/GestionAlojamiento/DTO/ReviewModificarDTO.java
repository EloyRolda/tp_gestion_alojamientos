package GestionAlojamiento.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long id;

    @Max(value = 5, message = "El valor maximo es 5")
    @Min(value = 1, message = "El valor minimo es 1")
    private Integer puntuacion;

    @Size(max = 2000, message = "El comentario es muy largo")
    private String comentario;

    @Min(value = 1, message = "Id invalido")
    private Long idCliente;

    @Min(value = 1, message = "Id invalido")
    private Long idAlojamiento;
}
