package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
