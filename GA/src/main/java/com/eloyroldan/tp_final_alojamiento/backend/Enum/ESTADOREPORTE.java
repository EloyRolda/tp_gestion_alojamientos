/// Estado de un reporte (RF44): arranca PENDIENTE y pasa a RESUELTO cuando el
/// admin lo revisa (SCRUM-79). En la base va en minuscula, via EstadoReporteConverter.
package com.eloyroldan.tp_final_alojamiento.backend.Enum;

public enum ESTADOREPORTE {

    PENDIENTE,
    RESUELTO
}
