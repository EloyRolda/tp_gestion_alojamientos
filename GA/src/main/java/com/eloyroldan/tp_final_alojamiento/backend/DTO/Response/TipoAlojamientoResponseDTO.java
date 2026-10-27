package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.TipoAlojamiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Un tipo del catalogo dinamico de tipos de alojamiento.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoAlojamientoResponseDTO {

    private Long idTipoAlojamiento;
    private String nombreTipo;

    public static TipoAlojamientoResponseDTO desde(TipoAlojamiento t) {
        return new TipoAlojamientoResponseDTO(t.getIdTipoAlojamiento(), t.getNombreTipo());
    }
}
