package GestionAlojamiento.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
//Lombok
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

    @NotBlank(message = "Campo Obligatorio")
    private LocalDateTime fecha; //Automaticamente registrado.

    @NotNull(message = "Campo Obligatorio")
    private Long idCliente;

    @NotNull(message = "Campo Obligatorio")
    private Long idAlojamiento;
}
