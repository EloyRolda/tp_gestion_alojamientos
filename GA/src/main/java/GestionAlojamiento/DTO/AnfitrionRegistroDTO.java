package GestionAlojamiento.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AnfitrionRegistroDTO {

    //[USUARIO]
    @NotBlank(message = "Campo Obligatorio")
    @Email(message = "El formato de correo es invalido")
    private String email;

    @NotBlank(message = "Campo Obligatorio")
    private String password;

    @NotBlank(message = "Campo Obligatorio")
    private String telefono;

    @NotBlank(message = "Campo Obligatorio")
    private String nombre;
    @NotNull(message = "Campo Obligatorio")
    private boolean activo; //Se inicia activo

}
