/// Lógica de negocio de mensajes por reserva.
/// Solo participan cliente y anfitrión de la reserva. Se lee siempre y se
/// escribe únicamente con la reserva abierta (SOLICITADA o CONFIRMADA).
package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ConversacionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MensajeDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ParticipantesReservaDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import com.eloyroldan.tp_final_alojamiento.backend.Evento.MensajeRecibido;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Mensaje;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.MensajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final ApplicationEventPublisher publicador;

    public ConversacionDTO conversacion(Long idReserva, Long idUsuario) {
        ParticipantesReservaDTO p = reservaService.participantes(idReserva);
        validarParticipante(p, idUsuario);

        List<Mensaje> mensajes = mensajeRepository.findConversacion(idReserva);

        List<MensajeDTO> mensajesDTO = mensajes.stream()
                .map(m -> toMensajeDTO(m, idUsuario))
                .toList();

        String nombreOtraParte;
        if (idUsuario.equals(p.idCliente())) {
            nombreOtraParte = p.nombreAnfitrion();
        } else {
            nombreOtraParte = p.nombreCliente();
        }

        boolean puedeEscribir = p.estado() == ESTADORESERVA.SOLICITADA
                || p.estado() == ESTADORESERVA.CONFIRMADA;

        return new ConversacionDTO(
                p.idReserva(),
                p.tituloAlojamiento(),
                nombreOtraParte,
                p.estado(),
                puedeEscribir,
                mensajesDTO
        );
    }

    public MensajeDTO enviar(Long idReserva, Long idUsuario, String texto) {
        ParticipantesReservaDTO p = reservaService.participantes(idReserva);
        validarParticipante(p, idUsuario);

        validarAbiertaParaEscribir(p.estado());

        String textoLimpio = texto == null ? "" : texto.trim();
        if (textoLimpio.isEmpty()) {
            throw new ParametroInvalidoException("Escribí un mensaje.");
        }
        if (textoLimpio.length() > 1000) {
            throw new ParametroInvalidoException("El mensaje no puede superar los 1000 caracteres.");
        }

        Reserva reserva = reservaService.obtenerPorId(idReserva);
        Usuario autor = usuarioService.obtenerPorId(idUsuario);

        Mensaje mensaje = Mensaje.builder()
                .reserva(reserva)
                .autor(autor)
                .texto(textoLimpio)
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);

        Long idDestinatario;
        boolean destinatarioEsAnfitrion;
        if (idUsuario.equals(p.idCliente())) {
            idDestinatario = p.idAnfitrion();
            destinatarioEsAnfitrion = true;
        } else {
            idDestinatario = p.idCliente();
            destinatarioEsAnfitrion = false;
        }

        publicador.publishEvent(new MensajeRecibido(
                guardado.getIdMensaje(),
                idReserva,
                idDestinatario,
                destinatarioEsAnfitrion,
                nombreCompleto(autor),
                p.tituloAlojamiento()
        ));

        return toMensajeDTO(guardado, idUsuario);
    }

    public void marcarLeidos(Long idReserva, Long idUsuario) {
        ParticipantesReservaDTO p = reservaService.participantes(idReserva);
        validarParticipante(p, idUsuario);
        mensajeRepository.marcarLeidosDeOtroAutor(idReserva, idUsuario);
    }

    public void borrarPorReserva(Long idReserva) {
        mensajeRepository.deleteByReserva_IdReserva(idReserva);
    }

    public void borrarPorAlojamiento(Long idAlojamiento) {
        mensajeRepository.borrarPorAlojamiento(idAlojamiento);
    }

    private void validarParticipante(ParticipantesReservaDTO p, Long idUsuario) {
        if (!idUsuario.equals(p.idCliente()) && !idUsuario.equals(p.idAnfitrion())) {
            throw new ParametroInvalidoException("No participás de esta reserva.");
        }
    }

    private void validarAbiertaParaEscribir(ESTADORESERVA estado) {
        if (estado != ESTADORESERVA.SOLICITADA && estado != ESTADORESERVA.CONFIRMADA) {
            throw new ParametroInvalidoException("La reserva está cerrada: podés leer los mensajes, pero no escribir.");
        }
    }

    private MensajeDTO toMensajeDTO(Mensaje m, Long idUsuario) {
        Usuario autor = m.getAutor();
        return new MensajeDTO(
                m.getIdMensaje(),
                autor.getId(),
                nombreCompleto(autor),
                m.getTexto(),
                m.getFechaEnvio(),
                m.isLeido(),
                autor.getId().equals(idUsuario)
        );
    }

    private String nombreCompleto(Usuario u) {
        return u.getNombre() + " " + u.getApellido();
    }
}
