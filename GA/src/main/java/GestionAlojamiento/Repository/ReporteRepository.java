package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Enums.EstadoReporte;
import GestionAlojamiento.Model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByEstado(EstadoReporte estado);
    List<Reporte> findAllByOrderByFechaDesc();
}
