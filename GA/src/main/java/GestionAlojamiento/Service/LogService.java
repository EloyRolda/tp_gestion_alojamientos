package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Log;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/// Auditoria simple para que el admin pueda ver que paso en el sistema.
/// Usa REQUIRES_NEW: si algo falla despues de registrar el log dentro de la
/// misma transaccion que la accion auditada, el log igual queda guardado.
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Usuario usuario, String accion, String entidad, Long idEntidad, String detalle) {
        Log log = new Log();
        log.setUsuario(usuario);
        log.setAccion(accion);
        log.setEntidad(entidad);
        log.setIdEntidad(idEntidad);
        log.setDetalle(detalle);
        log.setFecha(LocalDateTime.now());
        logRepository.save(log);
    }

    public List<Log> listarTodos() {
        return logRepository.findAllByOrderByFechaDesc();
    }

    public List<Log> listarPorUsuario(Long idUsuario) {
        return logRepository.findByUsuarioIdOrderByFechaDesc(idUsuario);
    }
}
