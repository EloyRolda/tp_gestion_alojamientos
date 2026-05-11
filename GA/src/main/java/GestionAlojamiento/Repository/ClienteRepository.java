package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByMetodo_Pago(String metodo_pago);
}
