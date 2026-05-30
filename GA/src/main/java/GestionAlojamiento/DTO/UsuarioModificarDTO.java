package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModificarDTO {

    private Long id;
    private String nombre;
    private String telefono;

    @Email(message = "El formato de correo es invalido")
    private String email;

    private String password;
    private Boolean activo;

    // Campos opcionales  rol
    private String metodoPago;  // CLIENTE
    private String matricula;   // ADMINISTRADOR
}