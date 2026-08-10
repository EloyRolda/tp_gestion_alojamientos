package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
    List<Notificacion> findByUsuarioIdAndLeidaFalse(Long usuarioId);
}
