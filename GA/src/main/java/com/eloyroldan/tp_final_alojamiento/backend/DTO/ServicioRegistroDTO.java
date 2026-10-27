package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Alta de un servicio/comodidad nuevo en el catalogo dinamico (ver ServicioService).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioRegistroDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 255)
    private String nombreServicio;
}
