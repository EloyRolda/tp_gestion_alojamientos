package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Imagen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Una imagen de la galeria de un alojamiento.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenResponseDTO {

    private Long id;
    private String alt;
    private String url;
    private Long idGaleria;

    public static ImagenResponseDTO desde(Imagen i) {
        return new ImagenResponseDTO(i.getId(), i.getAlt(), i.getUrl(), i.getGaleria().getId());
    }
}
