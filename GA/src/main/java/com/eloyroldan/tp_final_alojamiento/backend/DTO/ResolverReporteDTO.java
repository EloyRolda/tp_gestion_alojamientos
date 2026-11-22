/// Cuerpo del pedido con el que el admin resuelve un reporte (RF44 / SCRUM-79):
/// solamente la accion elegida, porque todo lo demas (estado, fecha, autor) lo
/// define el backend.
package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolverReporteDTO {

    @NotNull(message = "Indicá qué acción tomar con el reporte.")
    private ACCIONMODERACION accion;
}
