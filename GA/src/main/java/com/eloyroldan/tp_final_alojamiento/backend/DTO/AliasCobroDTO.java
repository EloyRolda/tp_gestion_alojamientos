package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Paso 1 y 2 del flujo de alias/CBU/CVU de cobro (ver UsuarioService.validarAliasCobro/confirmarAliasCobro).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliasCobroDTO {

    @NotBlank(message = "Campo obligatorio")
    private String valor;
}
