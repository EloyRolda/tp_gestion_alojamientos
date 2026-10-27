package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// Perfil completo (para /Usuario/me y /Usuario/mostrar/{id}). Nunca incluye password.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String usuarioLogin;
    private String email;
    private String telefono;
    private Boolean activo;
    private Boolean emailVerificado;
    private String aliasCobro;
    private LocalDateTime fechaRegistro;
    private TIPOUSUARIO tipo;

    public static UsuarioResponseDTO desde(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(), u.getNombre(), u.getApellido(), u.getUsuarioLogin(),
                u.getEmail(), u.getTelefono(), u.getActivo(), u.getEmailVerificado(), u.getAliasCobro(),
                u.getFechaRegistro(), u.getTipo());
    }
}
