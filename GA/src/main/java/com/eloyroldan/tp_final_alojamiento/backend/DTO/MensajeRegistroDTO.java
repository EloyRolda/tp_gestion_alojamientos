/// DTO de entrada para crear un mensaje nuevo dentro de una reserva.
package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensajeRegistroDTO {

    @NotBlank(message = "Escribí un mensaje.")
    @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres.")
    private String texto;
}
