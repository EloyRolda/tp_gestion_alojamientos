package GestionAlojamiento.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliasMercadoPagoDTO {
    @NotBlank(message = "Campo Obligatorio")
    @Size(max = 100)
    private String alias;
}
