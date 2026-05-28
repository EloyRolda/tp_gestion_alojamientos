package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoUsuario;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;

    private String metodoPago;
    //[USUARIO]
    private String nombre;
    @Email(message = "El formato de correo es invalido")
    private String email;

    private String password;

    private String telefono;

    private Boolean activo; //Se inicia activo

}
