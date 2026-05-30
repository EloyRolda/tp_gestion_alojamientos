package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByAlojamientoIdAndFechaInicioLessThanAndFechaFinGreaterThan(Long alojamientoId, LocalDate fechaFin, LocalDate fechaInicio);

    boolean existsByAlojamientoIdAndIdNotAndFechaInicioLessThanAndFechaFinGreaterThan(Long alojamientoId, Long id, LocalDate fechaFin, LocalDate fechaInicio);
}
