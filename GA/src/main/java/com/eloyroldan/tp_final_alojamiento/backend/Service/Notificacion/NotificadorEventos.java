package com.eloyroldan.tp_final_alojamiento.backend.Service.Notificacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPONOTIFICACION;
import com.eloyroldan.tp_final_alojamiento.backend.Evento.*;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Objects;

/// Escucha los eventos de dominio y arma los avisos correspondientes.
/// Los envíos se realizan a través de los canales disponibles y cada canal
/// se protege con try/catch para que un fallo no afecte a los demás.
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificadorEventos {

    private final List<CanalNotificacion> canales;
    private final UsuarioService usuarioService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharReservaSolicitada(ReservaSolicitada evento) {
        Aviso aviso = new Aviso(
                evento.idAnfitrion(),
                TIPONOTIFICACION.RESERVA_SOLICITADA,
                "Nueva solicitud de reserva para «" + evento.tituloAlojamiento() + "».",
                "/anfitrion/reservas/" + evento.idReserva(),
                false
        );
        enviar(aviso);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharReservaConfirmada(ReservaConfirmada evento) {
        Aviso aviso = new Aviso(
                evento.idCliente(),
                TIPONOTIFICACION.RESERVA_CONFIRMADA,
                "Tu reserva en «" + evento.tituloAlojamiento() + "» fue confirmada. Tenés 48 horas para pagarla.",
                "/mis-reservas/" + evento.idReserva(),
                true
        );
        enviar(aviso);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharReservaCancelada(ReservaCancelada evento) {
        Long idQuienCancelo = evento.idQuienCancelo();

        // idQuienCancelo es null si cancelo el sistema o un administrador: se avisa a los dos.
        if (!Objects.equals(idQuienCancelo, evento.idCliente())) {
            Aviso avisoCliente = new Aviso(
                    evento.idCliente(),
                    TIPONOTIFICACION.RESERVA_CANCELADA,
                    "Tu reserva en «" + evento.tituloAlojamiento() + "» fue cancelada.",
                    "/mis-reservas/" + evento.idReserva(),
                    true
            );
            enviar(avisoCliente);
        }

        if (!Objects.equals(idQuienCancelo, evento.idAnfitrion())) {
            Aviso avisoAnfitrion = new Aviso(
                    evento.idAnfitrion(),
                    TIPONOTIFICACION.RESERVA_CANCELADA,
                    "Se canceló la reserva de «" + evento.tituloAlojamiento() + "».",
                    "/anfitrion/reservas/" + evento.idReserva(),
                    true
            );
            enviar(avisoAnfitrion);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharPagoAcreditado(PagoAcreditado evento) {
        Aviso avisoCliente = new Aviso(
                evento.idCliente(),
                TIPONOTIFICACION.PAGO_ACREDITADO,
                "Recibimos tu pago de la reserva en «" + evento.tituloAlojamiento() + "».",
                "/mis-reservas/" + evento.idReserva(),
                true
        );
        enviar(avisoCliente);

        Aviso avisoAnfitrion = new Aviso(
                evento.idAnfitrion(),
                TIPONOTIFICACION.PAGO_ACREDITADO,
                "Se acreditó el pago de la reserva de «" + evento.tituloAlojamiento() + "».",
                "/anfitrion/reservas/" + evento.idReserva(),
                true
        );
        enviar(avisoAnfitrion);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharReservaFinalizada(ReservaFinalizada evento) {
        Aviso aviso = new Aviso(
                evento.idCliente(),
                TIPONOTIFICACION.RESERVA_FINALIZADA,
                "Tu estadía en «" + evento.tituloAlojamiento() + "» finalizó. ¡Contanos cómo te fue con una reseña!",
                "/mis-reservas/" + evento.idReserva(),
                true
        );
        enviar(aviso);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharPublicacionOcultada(PublicacionOcultada evento) {
        Aviso aviso = new Aviso(
                evento.idAnfitrion(),
                TIPONOTIFICACION.PUBLICACION_OCULTADA,
                "Tu publicación «" + evento.tituloAlojamiento() + "» fue ocultada: " + evento.etiquetaMotivo() + ".",
                "/anfitrion/alojamientos",
                false
        );
        enviar(aviso);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharPublicacionRepublicada(PublicacionRepublicada evento) {
        Aviso aviso = new Aviso(
                evento.idAnfitrion(),
                TIPONOTIFICACION.PUBLICACION_REPUBLICADA,
                "Tu publicación «" + evento.tituloAlojamiento() + "» volvió a estar visible.",
                "/anfitrion/alojamientos",
                false
        );
        enviar(aviso);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharReporteCreado(ReporteCreado evento) {
        List<Usuario> administradores = usuarioService.listarAdministradores();

        for (Usuario administrador : administradores) {
            if (!administrador.getActivo()) {
                continue;
            }

            Aviso aviso = new Aviso(
                    administrador.getId(),
                    TIPONOTIFICACION.REPORTE_NUEVO,
                    "Nuevo reporte: " + evento.etiquetaMotivo() + " sobre «" + evento.nombreObjetivo() + "».",
                    "/admin/reportes",
                    false
            );
            enviar(aviso);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void escucharReporteResuelto(ReporteResuelto evento) {
        Aviso aviso = new Aviso(
                evento.idAutor(),
                TIPONOTIFICACION.REPORTE_RESUELTO,
                "Revisamos tu reporte sobre «" + evento.nombreObjetivo() + "». Gracias por ayudarnos a cuidar la comunidad.",
                // Sin enlace: texto vacio (no null), porque la columna enlace es NOT NULL
                // en las bases ya creadas y ddl-auto=update no cambia eso. La campana
                // trata el texto vacio como "aviso sin enlace".
                "",
                false
        );
        enviar(aviso);
    }

    private void enviar(Aviso aviso) {
        for (CanalNotificacion canal : canales) {
            try {
                canal.enviar(aviso);
            } catch (RuntimeException e) {
                log.warn("No se pudo enviar aviso por el canal {}: {}", canal.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
