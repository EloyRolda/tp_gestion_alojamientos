package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Casa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasaRepository extends JpaRepository<Casa, Long> {
    List<Casa> findByTiene_patio();
    List<Casa> findByTiene_parrilla();
    List<Casa> findByTiene_Pileta();
}
