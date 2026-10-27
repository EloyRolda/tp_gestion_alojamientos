package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Version liviana de Alojamiento, para embeber dentro de Reserva/Review sin
/// duplicar todo el detalle (anfitrion, direccion, tipo) en cada respuesta anidada.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlojamientoResumenDTO {

    private Long idAlojamiento;
    private String titulo;
    private BigDecimal precioNoche;

    public static AlojamientoResumenDTO desde(Alojamiento a) {
        return new AlojamientoResumenDTO(a.getIdAlojamiento(), a.getTitulo(), a.getPrecioNoche());
    }
}
