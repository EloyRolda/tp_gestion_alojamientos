package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.CuentaMercadoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaMercadoPagoRepository extends JpaRepository<CuentaMercadoPago, Long> {
    Optional<CuentaMercadoPago> findByAnfitrionId(Long idAnfitrion);
}
