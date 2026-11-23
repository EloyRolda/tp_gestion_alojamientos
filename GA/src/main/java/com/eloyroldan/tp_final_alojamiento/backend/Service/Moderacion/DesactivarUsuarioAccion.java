/// Accion de moderacion «Desactivar usuario» (RF44 / SCRUM-79): da de baja la
/// cuenta reportada. Solo vale para reportes sobre personas y nunca alcanza a
/// un administrador (lo valida UsuarioService.suspender).
package com.eloyroldan.tp_final_alojamiento.backend.Service.Moderacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DesactivarUsuarioAccion implements AccionModeracionStrategy {

    /// Service dueño de la tabla de usuarios: nunca se toca su Repository.
    private final UsuarioService usuarioService;

    @Override
    public ACCIONMODERACION accion() {
        return ACCIONMODERACION.DESACTIVAR_USUARIO;
    }

    @Override
    public void aplicar(Reporte reporte, MOTIVOREPORTE motivo, String detalle) {
        if (reporte.getTipo() != TIPOREPORTE.USUARIO) {
            throw new ParametroInvalidoException("Desactivar al usuario solo aplica a reportes sobre personas.");
        }
        usuarioService.suspender(reporte.getIdObjetivo(), motivo, detalle);
    }
}
