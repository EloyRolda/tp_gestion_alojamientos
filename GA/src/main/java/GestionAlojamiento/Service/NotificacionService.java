package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Enums.CategoriaNotificacion;
import GestionAlojamiento.Model.Notificacion;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.NotificacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    /// Crea y persiste una notificacion para un usuario. La llaman los demas
    /// services (ReservaService, PagoService, ChatService, etc) en cada evento relevante.
    @Transactional
    public Notificacion crear(Usuario destinatario, String mensaje, CategoriaNotificacion categoria, String referencia) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(destinatario);
        notificacion.setMensaje(mensaje);
        notificacion.setCategoria(categoria);
        notificacion.setLeida(false);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setReferencia(referencia);
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listarPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuarioIdOrderByFechaDesc(idUsuario);
    }

    public long contarNoLeidas(Long idUsuario) {
        return notificacionRepository.countByUsuarioIdAndLeidaFalse(idUsuario);
    }

    @Transactional
    public Notificacion marcarLeida(Long idNotificacion, Long idUsuarioSolicitante) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new IdNoEncontradoException("Notificacion no encontrada: " + idNotificacion));

        if (!notificacion.getUsuario().getId().equals(idUsuarioSolicitante)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta notificacion no te pertenece");
        }

        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarTodasLeidas(Long idUsuario) {
        List<Notificacion> pendientes = notificacionRepository.findByUsuarioIdAndLeidaFalse(idUsuario);
        pendientes.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(pendientes);
    }
}
