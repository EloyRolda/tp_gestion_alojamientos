package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Cuenta de cancelaciones agrupadas por motivo (ver PagoRepository.cancelacionesPorMotivo).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelacionPorMotivoDTO {
    private MOTIVOCANCELACION motivo;
    private Long cantidad;
}
