/// Lo que manda el formulario de reporte (RF44): que se reporta, a quien, el
/// motivo de la lista fija y el detalle opcional (obligatorio con «Otro»).
package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRegistroDTO {

    @NotNull(message = "Indicá qué estás reportando.")
    private TIPOREPORTE tipo;

    @NotNull(message = "Falta el objetivo del reporte.")
    private Long idObjetivo;

    @NotNull(message = "Elegí un motivo.")
    private MOTIVOREPORTE motivo;

    @Size(max = 500, message = "El detalle no puede superar los 500 caracteres.")
    private String detalle;
}
