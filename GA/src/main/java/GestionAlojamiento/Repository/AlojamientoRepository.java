package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    List<Alojamiento> findByActivoTrueOrderByTituloAsc();

}
