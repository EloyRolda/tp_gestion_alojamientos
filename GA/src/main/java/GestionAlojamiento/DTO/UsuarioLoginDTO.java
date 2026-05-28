package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLoginDTO {

    @NotBlank(message = "Campo Obligatorio")
    @Email(message = "El formato de correo es invalido")
    private String email;

    @NotBlank(message = "Campo Obligatorio")
    private String password;
}
