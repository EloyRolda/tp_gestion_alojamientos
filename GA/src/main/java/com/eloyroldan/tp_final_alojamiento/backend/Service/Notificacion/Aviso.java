package com.eloyroldan.tp_final_alojamiento.backend.Service.Notificacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPONOTIFICACION;

/// Contrato de aviso que circula entre los eventos y los canales de notificación.
public record Aviso(Long idDestinatario, TIPONOTIFICACION tipo, String texto, String enlace, boolean porCorreo) {}
