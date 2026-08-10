package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Chat;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Mensaje;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/// Un chat se abre UNA vez por reserva, recien cuando la reserva queda PAGADA
/// (lo dispara PagoService), y se cierra solo 48hs despues de finalizada la estadia.
@Service
@RequiredArgsConstructor
public class ChatService {

    public static final long HORAS_PARA_CERRAR_CHAT = 48;

    private final ChatRepository chatRepository;
    private final MensajeService mensajeService;
    private final UsuarioService usuarioService;
    private final SimpMessagingTemplate mensajeriaWebSocket;

    @Transactional
    public Chat crearParaReserva(Reserva reserva) {
        Chat chat = new Chat();
        chat.setReserva(reserva);
        chat.setAnfitrion(reserva.getAlojamiento().getAnfitrion());
        chat.setCliente(reserva.getCliente());
        chat.setActivo(true);
        return chatRepository.save(chat);
    }

    public Chat obtenerPorId(Long idChat) {
        return chatRepository.findById(idChat)
                .orElseThrow(() -> new IdNoEncontradoException("Chat no encontrado: " + idChat));
    }

    public Chat obtenerPorReserva(Long idReserva, String emailSolicitante) {
        Chat chat = chatRepository.findByReservaId(idReserva)
                .orElseThrow(() -> new IdNoEncontradoException("Todavia no hay chat para esta reserva (se habilita al confirmarse el pago)."));
        validarParticipanteOAdmin(chat, emailSolicitante);
        return chat;
    }

    /// Todos los chats donde el usuario logueado es cliente o anfitrion.
    public List<Chat> listarPropios(String email) {
        List<Chat> comoCliente = chatRepository.findByClienteEmail(email);
        List<Chat> comoAnfitrion = chatRepository.findByAnfitrionEmail(email);
        comoCliente.addAll(comoAnfitrion);
        return comoCliente;
    }

    /// El admin puede auditar/participar en cualquier chat puntual.
    public List<Chat> listarTodos() {
        return chatRepository.findAll();
    }

    public List<Mensaje> listarMensajes(Long idChat, String emailSolicitante) {
        Chat chat = obtenerPorId(idChat);
        validarParticipanteOAdmin(chat, emailSolicitante);
        return mensajeService.listarPorChat(idChat);
    }

    @Transactional
    public Mensaje enviarMensaje(Long idChat, String contenido, String emailRemitente) {
        Chat chat = obtenerPorId(idChat);
        Usuario remitente = usuarioService.obtenerPorEmail(emailRemitente);
        validarParticipanteOAdmin(chat, emailRemitente);

        if (!Boolean.TRUE.equals(chat.getActivo())) {
            throw new ParametroInvalidoException("Este chat ya esta cerrado.");
        }

        Mensaje mensaje = mensajeService.crear(chat, remitente, contenido);

        // Notificamos en tiempo real a quienes esten escuchando el topic del chat.
        mensajeriaWebSocket.convertAndSend("/topic/chat/" + idChat, mensaje);

        return mensaje;
    }

    /// Cierra chats activos cuya estadia finalizo hace mas de 48hs (tarea programada).
    @Transactional
    public void cerrarChatsVencidos() {
        LocalDateTime limite = LocalDateTime.now().minusHours(HORAS_PARA_CERRAR_CHAT);
        List<Chat> chats = chatRepository.findActivosConEstadiaFinalizadaAntesDe(EstadoReserva.FINALIZADA, limite);
        for (Chat chat : chats) {
            chat.setActivo(false);
            chatRepository.save(chat);
        }
    }

    private void validarParticipanteOAdmin(Chat chat, String email) {
        Usuario usuario = usuarioService.obtenerPorEmail(email);
        boolean esAdmin = usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
        boolean esParticipante = chat.getAnfitrion().getId().equals(usuario.getId()) || chat.getCliente().getId().equals(usuario.getId());

        if (!esAdmin && !esParticipante) {
            throw new ParametroInvalidoException("No autorizado sobre este chat.");
        }
    }
}
