package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.CategoriaAmenity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityRegistroDTO {
    @NotBlank(message = "Campo Obligatorio")
    private String nombre;

    @NotNull(message = "Campo Obligatorio")
    private CategoriaAmenity categoria;
}
