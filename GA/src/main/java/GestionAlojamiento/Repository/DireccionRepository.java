package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
}
