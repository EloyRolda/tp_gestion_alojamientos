/// Evento que se publica cuando un usuario crea un reporte.
/// Se usa para avisar a los administradores que hay un reporte nuevo.
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record ReporteCreado(
        Long idReporte,
        String etiquetaMotivo,
        String nombreObjetivo
) {
}
