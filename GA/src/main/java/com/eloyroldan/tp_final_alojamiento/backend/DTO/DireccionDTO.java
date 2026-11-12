package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

    /// Ubicacion en el mapa (opcional). Si se manda una, hay que mandar la otra.
    @DecimalMin(value = "-90.0", message = "Latitud fuera de rango")
    @DecimalMax(value = "90.0", message = "Latitud fuera de rango")
    private Double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud fuera de rango")
    @DecimalMax(value = "180.0", message = "Longitud fuera de rango")
    private Double longitud;
}
