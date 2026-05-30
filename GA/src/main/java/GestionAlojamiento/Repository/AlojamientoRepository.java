package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

}
