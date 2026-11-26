package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPONOTIFICACION;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Notificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// Respuesta para listar los avisos del usuario autenticado.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {

    private Long id;
    private TIPONOTIFICACION tipo;
    private String texto;
    private String enlace;
    private boolean leida;
    private LocalDateTime fecha;

    public static NotificacionDTO desde(Notificacion notificacion) {
        return NotificacionDTO.builder()
                .id(notificacion.getIdNotificacion())
                .tipo(notificacion.getTipo())
                .texto(notificacion.getTexto())
                .enlace(notificacion.getEnlace())
                .leida(notificacion.isLeida())
                .fecha(notificacion.getFechaCreacion())
                .build();
    }
}
