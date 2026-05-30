package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoUsuario;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {

    //[USUARIO]
    @NotBlank(message = "Campo Obligatorio")
    private String nombre;

    @NotBlank(message = "Campo Obligatorio")
    @Email(message = "El formato de correo es invalido")
    private String email;

    @NotBlank(message = "Campo Obligatorio")
    private String password;

    @NotBlank(message = "Campo Obligatorio")
    private String telefono;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo Obligatorio")
    private TipoUsuario tipoUsuario;

    // Opcionales por rol
    private String metodoPago;  // CLIENTE
    private String matricula;   // ADMINISTRADOR

}
