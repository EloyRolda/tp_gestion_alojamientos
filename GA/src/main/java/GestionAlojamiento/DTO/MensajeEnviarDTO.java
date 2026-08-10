package GestionAlojamiento.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeEnviarDTO {
    @NotBlank(message = "El mensaje no puede estar vacio")
    @Size(max = 2000)
    private String contenido;
}
