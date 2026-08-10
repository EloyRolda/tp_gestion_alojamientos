package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByOrderByFechaDesc();
    List<Log> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
