package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoUsuario;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AnfitrionModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;

    //[USUARIO]
    @Email(message = "El formato de correo es invalido")
    private String email;

    private String password;

    private String telefono;

    private LocalDateTime fechaRegistro;

    private boolean activo; //Se inicia activo

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario; //AUTOMATICAMENTE Anfitrion
}
