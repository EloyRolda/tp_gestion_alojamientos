package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.eloyroldan.tp_final_alojamiento.backend.Validation.DominioEmailValido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Actualizacion de perfil. Todos los campos son opcionales (solo se pisa lo
/// que venga no-nulo) — ver UsuarioService.actualizarPropio/actualizarComoAdmin
/// para la diferencia de que campos puede tocar cada uno (ej: "activo" solo admin).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModificarDTO {

    private Long id;

    @Size(max = 50)
    private String nombre;

    @Size(max = 50)
    private String apellido;

    @Pattern(regexp = "^[A-Za-z0-9_.]{3,50}$", message = "El usuario solo puede contener letras, numeros, punto y guion bajo")
    private String usuarioLogin;

    @Email(message = "El formato de correo es invalido")
    @DominioEmailValido
    private String email;

    @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
    private String password;

    @Size(max = 30)
    private String telefono;

    private Boolean activo;
}
