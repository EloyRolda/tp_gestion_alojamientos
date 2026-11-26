package com.eloyroldan.tp_final_alojamiento.backend.Service.Notificacion;

/// Contrato para los canales que distribuyen los avisos.
public interface CanalNotificacion {
    void enviar(Aviso aviso);
}
