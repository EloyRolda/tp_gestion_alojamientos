package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Casa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasaRepository extends JpaRepository<Casa, Long> {
    List<Casa> findByTienePatioTrue();
    List<Casa> findByTieneParrillaTrue();
    List<Casa> findByTienePiletaTrue();
}
