package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.TipoInmueble;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

}
