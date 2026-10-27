package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Pide un nuevo token de verificacion de correo (el anterior pudo vencer o perderse).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReenviarVerificacionDTO {

    @NotBlank(message = "Campo obligatorio")
    @Email(message = "El formato de correo es invalido")
    private String email;
}
