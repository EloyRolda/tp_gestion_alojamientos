package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Casa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasaRepository extends JpaRepository<Casa,Long> {
}
