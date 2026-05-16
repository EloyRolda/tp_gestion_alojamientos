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

import java.time.LocalDateTime;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteRegistroDTO {

    @NotBlank(message = "Campo Obligatorio")
    private String metodoPago;

    //[USUARIO]
    @NotBlank(message = "Campo Obligatorio")
    @Email(message = "El formato de correo es invalido")
    private String email;

    @NotBlank(message = "Campo Obligatorio")
    private String password;

    @NotBlank(message = "Campo Obligatorio")
    private String telefono;

    @NotBlank(message = "Campo Obligatorio")
    private LocalDateTime fechaRegistro;

    @NotNull(message = "Campo Obligatorio")
    private boolean activo;//Se inicia activo

    @NotBlank(message = "Campo Obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario; //AUTOMATICAMENTE Cliente

}
