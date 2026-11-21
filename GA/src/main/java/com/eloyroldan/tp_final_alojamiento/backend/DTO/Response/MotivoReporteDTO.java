/// Un motivo del catalogo fijo de reportes (RF44), con su etiqueta y las
/// categorias a las que aplica: el formulario filtra el select con esto.
package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotivoReporteDTO {

    private String codigo;
    private String etiqueta;
    private List<String> aplicaA;

    public static MotivoReporteDTO desde(MOTIVOREPORTE motivo) {
        return new MotivoReporteDTO(motivo.name(), motivo.getEtiqueta(), motivo.getAplicaA());
    }
}
