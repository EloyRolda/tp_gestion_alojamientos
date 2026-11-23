/// Accion de moderacion «Descartar» (RF44 / SCRUM-79): cierra el reporte como
/// resuelto sin tocar al alojamiento ni a la persona reportada.
package com.eloyroldan.tp_final_alojamiento.backend.Service.Moderacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import org.springframework.stereotype.Component;

@Component
public class DescartarAccion implements AccionModeracionStrategy {

    @Override
    public ACCIONMODERACION accion() {
        return ACCIONMODERACION.DESCARTAR;
    }

    /// No hace nada sobre el objetivo: solo se guarda la resolucion del reporte.
    @Override
    public void aplicar(Reporte reporte, MOTIVOREPORTE motivo, String detalle) {
        // Sin efecto sobre el objetivo.
    }
}
