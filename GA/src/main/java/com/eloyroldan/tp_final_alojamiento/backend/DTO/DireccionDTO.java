package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Se usa embebido dentro de AlojamientoRegistroDTO/ModificarDTO para crear una
/// direccion nueva. Si en cambio se quiere reutilizar una direccion ya
/// existente (ej: varias cabanias del mismo predio), se envia idDireccion
/// en el DTO contenedor y este objeto se ignora.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100)
    private String pais;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100)
    private String provincia;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 10)
    private String codigoPostal;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100)
    private String ciudad;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 30)
    private String calle;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor invalido")
    private Integer altura;
}
