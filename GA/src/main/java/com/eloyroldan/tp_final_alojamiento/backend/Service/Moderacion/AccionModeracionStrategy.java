/// Contrato de las acciones de moderacion (RF44 / SCRUM-79): cada accion sabe que
/// valor de ACCIONMODERACION implementa y como castigar al objetivo del reporte.
/// ReporteService las recibe como lista y las indexa por accion() en un Map,
/// igual que PagoService hace con las politicas de cancelacion.
package com.eloyroldan.tp_final_alojamiento.backend.Service.Moderacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;

public interface AccionModeracionStrategy {

    /// Accion de moderacion que resuelve esta estrategia.
    ACCIONMODERACION accion();

    /// Aplica el efecto de la accion sobre el objetivo del reporte. Si la accion
    /// no corresponde al tipo de objetivo, tiene que fallar con
    /// ParametroInvalidoException (el Controller la traduce a 400).
    ///
    /// @param motivo  motivo que eligió el administrador. Si viene `null`, se usa
    ///                el motivo original del reporte.
    /// @param detalle detalle opcional de la moderación (puede ser `null`).
    void aplicar(Reporte reporte, MOTIVOREPORTE motivo, String detalle);
}
