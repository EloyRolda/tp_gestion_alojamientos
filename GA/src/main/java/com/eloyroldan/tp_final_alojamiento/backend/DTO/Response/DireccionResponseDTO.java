package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Direccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Una direccion (puede estar compartida por varios alojamientos).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionResponseDTO {

    private Long idDireccion;
    private String pais;
    private String provincia;
    private String codigoPostal;
    private String ciudad;
    private String calle;
    private Integer altura;

    public static DireccionResponseDTO desde(Direccion d) {
        return new DireccionResponseDTO(
                d.getIdDireccion(), d.getPais(), d.getProvincia(), d.getCodigoPostal(),
                d.getCiudad(), d.getCalle(), d.getAltura());
    }
}
