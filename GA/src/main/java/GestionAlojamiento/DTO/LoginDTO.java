package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public class LoginDTO {
    @NotBlank(message = "No puede dejar el campo vacio")
    @Email(message = "Formato invalido")
    private String email;
    @NotBlank(message = "No puede dejar el campo vacio")
    private String password;
}
