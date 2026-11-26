package com.eloyroldan.tp_final_alojamiento.backend.Service.Notificacion;

import com.eloyroldan.tp_final_alojamiento.backend.Service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// Canal que guarda el aviso en la base de datos para mostrarlo en la campana.
@Component
@RequiredArgsConstructor
public class CanalInterno implements CanalNotificacion {

    private final NotificacionService notificacionService;

    @Override
    public void enviar(Aviso aviso) {
        notificacionService.crear(aviso.idDestinatario(), aviso.tipo(), aviso.texto(), aviso.enlace());
    }
}
