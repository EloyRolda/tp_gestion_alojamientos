package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAlojamiento(Alojamiento alojamiento);

    List<Review> findByCliente(Cliente cliente);
}
