package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAlojamiento(Alojamiento alojamiento);

    List<Review> findByCliente(Usuario cliente);

    Optional<Review> findByReservaId(Long reservaId);

    boolean existsByReservaId(Long reservaId);

    void deleteByAlojamiento(Alojamiento alojamiento);
}
