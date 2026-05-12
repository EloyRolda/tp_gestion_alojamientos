package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    boolean existsByMatricula(String matricula);
}
