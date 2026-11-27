/// Evento que se publica cuando un reporte pasa a estado RESUELTO.
/// Se usa para avisarle al autor que su reporte fue revisado.
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record ReporteResuelto(
        Long idReporte,
        Long idAutor,
        String nombreObjetivo
) {
}
