package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.NotificacionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPONOTIFICACION;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Notificacion;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// Lógica de negocio de las notificaciones internas.
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void crear(Long idDestinatario, TIPONOTIFICACION tipo, String texto, String enlace) {
        Usuario destinatario = usuarioService.obtenerPorId(idDestinatario);
        Notificacion notificacion = Notificacion.builder()
                .destinatario(destinatario)
                .tipo(tipo)
                .texto(texto)
                .enlace(enlace)
                .leida(false)
                .build();
        notificacionRepository.save(notificacion);
    }

    @Transactional
    public List<NotificacionDTO> listarMias(Long idUsuario) {
        return notificacionRepository
                .findTop30ByDestinatario_IdOrderByFechaCreacionDescIdNotificacionDesc(idUsuario)
                .stream()
                .map(NotificacionDTO::desde)
                .toList();
    }

    @Transactional
    public long contarSinLeer(Long idUsuario) {
        return notificacionRepository.countByDestinatario_IdAndLeidaFalse(idUsuario);
    }

    @Transactional
    public void marcarLeida(Long idNotificacion, Long idUsuario) {
        Notificacion notificacion = notificacionRepository
                .findByIdNotificacionAndDestinatario_Id(idNotificacion, idUsuario)
                .orElseThrow(() -> new IdNoEncontradoException("Aviso no encontrado."));
        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarTodasLeidas(Long idUsuario) {
        notificacionRepository.marcarTodasLeidas(idUsuario);
    }
}
