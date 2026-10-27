package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Galeria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// La galeria de imagenes de un alojamiento (una por alojamiento).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GaleriaResponseDTO {

    private Long id;
    private String nombreGaleria;
    private Long idAlojamiento;

    public static GaleriaResponseDTO desde(Galeria g) {
        return new GaleriaResponseDTO(g.getId(), g.getNombreGaleria(), g.getAlojamiento().getIdAlojamiento());
    }
}
