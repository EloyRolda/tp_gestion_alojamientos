package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Alta de un tipo de alojamiento nuevo en el catalogo dinamico (ver TipoAlojamientoService).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoAlojamientoRegistroDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100)
    private String nombreTipo;
}
