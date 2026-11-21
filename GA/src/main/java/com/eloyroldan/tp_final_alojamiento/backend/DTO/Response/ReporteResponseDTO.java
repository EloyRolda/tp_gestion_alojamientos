/// Respuesta de un reporte ya guardado (RF44). Se arma dentro de un metodo
/// @Transactional: nunca se devuelve la entidad Reporte desde el Controller.
package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO {

    private Long idReporte;
    private TIPOREPORTE tipo;
    private Long idObjetivo;
    private MOTIVOREPORTE motivo;
    /// Etiqueta en espanol del motivo, para no repetir la traduccion en el front.
    private String etiquetaMotivo;
    private String detalle;
    private ESTADOREPORTE estado;
    private LocalDateTime fechaCreacion;

    public static ReporteResponseDTO desde(Reporte reporte) {
        return new ReporteResponseDTO(
                reporte.getIdReporte(),
                reporte.getTipo(),
                reporte.getIdObjetivo(),
                reporte.getMotivo(),
                reporte.getMotivo() == null ? null : reporte.getMotivo().getEtiqueta(),
                reporte.getDetalle(),
                reporte.getEstado(),
                reporte.getFechaCreacion());
    }
}
