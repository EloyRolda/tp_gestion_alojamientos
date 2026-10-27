package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// El identificador puede ser email, usuario_login o telefono indistintamente.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "No puede dejar el campo vacio")
    private String identificador;

    @NotBlank(message = "No puede dejar el campo vacio")
    private String password;
}
