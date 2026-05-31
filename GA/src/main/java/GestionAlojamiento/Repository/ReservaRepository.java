package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Model.Enums.TipoEstado;
import GestionAlojamiento.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByAlojamientoIdAndFechaInicioLessThanAndFechaFinGreaterThan(Long alojamientoId, LocalDate fechaFin, LocalDate fechaInicio);

    boolean existsByAlojamientoIdAndIdNotAndFechaInicioLessThanAndFechaFinGreaterThan(Long alojamientoId, Long id, LocalDate fechaFin, LocalDate fechaInicio);

    boolean existsByClienteIdAndAlojamientoIdAndTipoEstadoAndFechaFinBefore(Long clienteId, Long alojamientoId, TipoEstado tipoEstado, LocalDate now);


    List<Reserva> findByCliente_Email(String email);
}
