package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
