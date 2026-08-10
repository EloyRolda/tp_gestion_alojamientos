package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Chat;
import GestionAlojamiento.Model.Mensaje;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.MensajeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public List<Mensaje> listarPorChat(Long idChat) {
        return mensajeRepository.findByChatIdOrderByFechaAsc(idChat);
    }

    @Transactional
    public Mensaje crear(Chat chat, Usuario remitente, String contenido) {
        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setRemitente(remitente);
        mensaje.setContenido(contenido);
        mensaje.setFecha(LocalDateTime.now());
        mensaje.setLeido(false);
        return mensajeRepository.save(mensaje);
    }
}
