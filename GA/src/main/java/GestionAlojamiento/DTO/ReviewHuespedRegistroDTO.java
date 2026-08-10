package GestionAlojamiento.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewHuespedRegistroDTO {
    @NotNull(message = "Campo Obligatorio")
    private Long idReserva;

    @NotBlank(message = "Campo Obligatorio")
    @Size(max = 800)
    private String comentario;
}
