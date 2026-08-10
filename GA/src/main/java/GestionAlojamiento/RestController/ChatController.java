package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.MensajeEnviarDTO;
import GestionAlojamiento.Model.Chat;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Mensaje;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.ChatService;
import GestionAlojamiento.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UsuarioService usuarioService;

    /// Chats donde el usuario logueado participa (como cliente o como anfitrion).
    @GetMapping("/listar/propios")
    public List<Chat> listarPropios(Authentication auth) {
        return chatService.listarPropios(auth.getName());
    }

    /// El admin puede auditar cualquier chat.
    @GetMapping("/listar")
    public List<Chat> listarTodos() {
        return chatService.listarTodos();
    }

    @GetMapping("/reserva/{idReserva}")
    public Chat obtenerPorReserva(@PathVariable Long idReserva, Authentication auth) {
        return chatService.obtenerPorReserva(idReserva, auth.getName());
    }

    @GetMapping("/{idChat}/mensajes")
    public List<Mensaje> listarMensajes(@PathVariable Long idChat, Authentication auth) {
        return chatService.listarMensajes(idChat, auth.getName());
    }

    /// Envio via REST tradicional (fallback si el frontend todavia no usa WebSocket).
    @PostMapping("/{idChat}/mensajes")
    public Mensaje enviarMensajeRest(@PathVariable Long idChat, @RequestBody @Valid MensajeEnviarDTO dto, Authentication auth) {
        return chatService.enviarMensaje(idChat, dto.getContenido(), auth.getName());
    }

    /// Envio en tiempo real via STOMP: el cliente publica a /app/chat/{idChat}/enviar
    /// y todos los que esten suscriptos a /topic/chat/{idChat} reciben el mensaje.
    @MessageMapping("/chat/{idChat}/enviar")
    public void enviarMensajeWebSocket(@DestinationVariable Long idChat, MensajeEnviarDTO dto, Authentication auth) {
        chatService.enviarMensaje(idChat, dto.getContenido(), auth.getName());
    }
}
