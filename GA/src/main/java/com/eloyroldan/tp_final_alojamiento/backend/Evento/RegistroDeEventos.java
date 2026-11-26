/// Componente de prueba que registra en el log cada evento de dominio publicado.
/// En SCRUM-82 este mismo lugar sera reemplazado por los avisos/campanas reales.
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegistroDeEventos {

    @EventListener
    public void escucharReservaSolicitada(ReservaSolicitada evento) {
        log.info("Evento ReservaSolicitada: reserva {}", evento.idReserva());
    }

    @EventListener
    public void escucharReservaConfirmada(ReservaConfirmada evento) {
        log.info("Evento ReservaConfirmada: reserva {}", evento.idReserva());
    }

    @EventListener
    public void escucharReservaCancelada(ReservaCancelada evento) {
        log.info("Evento ReservaCancelada: reserva {}, idQuienCancelo={}", evento.idReserva(), evento.idQuienCancelo());
    }

    @EventListener
    public void escucharReservaFinalizada(ReservaFinalizada evento) {
        log.info("Evento ReservaFinalizada: reserva {}", evento.idReserva());
    }

    @EventListener
    public void escucharPagoAcreditado(PagoAcreditado evento) {
        log.info("Evento PagoAcreditado: reserva {}", evento.idReserva());
    }
}
