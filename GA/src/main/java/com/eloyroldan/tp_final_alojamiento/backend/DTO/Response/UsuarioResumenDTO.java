package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Version publica/liviana de Usuario, para embeber dentro de otras respuestas
/// (ej: el anfitrion de un Alojamiento, el cliente de una Reserva) sin exponer
/// datos de contacto ni de cuenta.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResumenDTO {

    private Long id;
    private String nombre;
    private String apellido;

    public static UsuarioResumenDTO desde(Usuario usuario) {
        return new UsuarioResumenDTO(usuario.getId(), usuario.getNombre(), usuario.getApellido());
    }
}
