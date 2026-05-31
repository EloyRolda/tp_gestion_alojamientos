package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CasaRepository extends JpaRepository<Casa, Long> {
    List<Casa> findByAlojamientoAnfitrionEmail(String correoAnfitrion);
}
