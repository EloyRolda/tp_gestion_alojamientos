package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    Optional<Amenity> findByNombreIgnoreCase(String nombre);
}
