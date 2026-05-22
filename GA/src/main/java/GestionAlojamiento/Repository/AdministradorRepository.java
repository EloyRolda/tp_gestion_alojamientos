package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Administrador;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    boolean existsByMatricula(String matricula);

    boolean existsByMatriculaAndIdNot(String matricula, @NotNull(message = "Campo Obligatorio") Long id);
}
