/// Accion de moderacion «Ocultar alojamiento» (RF44 / SCRUM-79): despublica el
/// alojamiento reportado. Solo vale para reportes sobre alojamientos.
package com.eloyroldan.tp_final_alojamiento.backend.Service.Moderacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import com.eloyroldan.tp_final_alojamiento.backend.Service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OcultarAlojamientoAccion implements AccionModeracionStrategy {

    /// Service dueño de la tabla de alojamientos: nunca se toca su Repository.
    private final AlojamientoService alojamientoService;

    @Override
    public ACCIONMODERACION accion() {
        return ACCIONMODERACION.OCULTAR_ALOJAMIENTO;
    }

    @Override
    public void aplicar(Reporte reporte, MOTIVOREPORTE motivo, String detalle) {
        if (reporte.getTipo() != TIPOREPORTE.ALOJAMIENTO) {
            throw new ParametroInvalidoException("Ocultar el alojamiento solo aplica a reportes sobre alojamientos.");
        }
        alojamientoService.ocultarPorModeracion(reporte.getIdObjetivo(), motivo, detalle);
    }
}
