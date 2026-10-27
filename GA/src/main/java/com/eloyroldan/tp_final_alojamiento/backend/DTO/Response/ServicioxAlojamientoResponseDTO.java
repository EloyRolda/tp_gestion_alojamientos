package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.ServicioxAlojamiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Un servicio/comodidad ya vinculado a un alojamiento puntual.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioxAlojamientoResponseDTO {

    private Long idServicioXAlojamiento;
    private ServicioResponseDTO servicio;

    public static ServicioxAlojamientoResponseDTO desde(ServicioxAlojamiento sxa) {
        return new ServicioxAlojamientoResponseDTO(
                sxa.getIdServicioXAlojamiento(), ServicioResponseDTO.desde(sxa.getServicio()));
    }
}
