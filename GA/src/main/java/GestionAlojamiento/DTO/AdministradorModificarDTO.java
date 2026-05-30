package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdministradorModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;

    private String matricula;

    //[Usuario]
    @Email(message = "El formato de correo es invalido")
    private String email;

    private String password;
    private String nombre;

    private String telefono;

    private Boolean activo; //Se inicia activo
}
