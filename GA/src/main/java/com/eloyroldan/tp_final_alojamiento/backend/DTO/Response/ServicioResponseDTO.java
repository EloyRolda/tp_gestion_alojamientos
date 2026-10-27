package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.Servicio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Un servicio/comodidad del catalogo dinamico.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponseDTO {

    private Long idServicio;
    private String nombreServicio;

    public static ServicioResponseDTO desde(Servicio s) {
        return new ServicioResponseDTO(s.getIdServicio(), s.getNombreServicio());
    }
}
