/// Cuerpo del pedido con el que el admin resuelve un reporte (RF44 / SCRUM-79):
/// la acción elegida y, para ocultar/desactivar, el motivo y detalle opcionales.
package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolverReporteDTO {

    @NotNull(message = "Indicá qué acción tomar con el reporte.")
    private ACCIONMODERACION accion;

    /// Motivo opcional: si no llega, el backend usa el motivo del reporte.
    private MOTIVOREPORTE motivo;

    @Size(max = 500)
    private String detalle;
}
