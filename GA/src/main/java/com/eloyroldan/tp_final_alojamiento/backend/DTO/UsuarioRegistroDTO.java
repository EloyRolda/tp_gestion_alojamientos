package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Validation.DominioEmailValido;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 50)
    private String apellido;

    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9_.]{3,50}$", message = "El usuario solo puede contener letras, numeros, punto y guion bajo")
    private String usuarioLogin;

    @NotBlank(message = "Campo obligatorio")
    @Email(message = "El formato de correo es invalido")
    @DominioEmailValido
    private String email;

    @NotBlank(message = "Campo obligatorio")
    @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 30)
    private String telefono;

    /// No se permite registrar ADMINISTRADOR desde este DTO (ver UsuarioService.crear).
    @NotNull(message = "Campo obligatorio")
    private TIPOUSUARIO tipo;
}
