package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {


    List<Departamento> findByAlojamientoAnfitrionEmail(String correoAnfitrion);
}
