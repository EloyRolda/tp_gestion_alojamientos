package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.ReviewHuesped;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewHuespedRepository extends JpaRepository<ReviewHuesped, Long> {
    boolean existsByReservaId(Long reservaId);
    List<ReviewHuesped> findByHuespedId(Long huespedId);
}
