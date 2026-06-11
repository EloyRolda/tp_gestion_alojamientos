package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Gallery findByAlojamientoId(Long id);

    void deleteByAlojamientoId(Long alojamientoId);
}