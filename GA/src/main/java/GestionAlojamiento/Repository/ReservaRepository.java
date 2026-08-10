package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /// Solapamiento: solo cuentan estados que realmente ocupan el calendario.
    boolean existsByAlojamientoIdAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
            Long alojamientoId, List<EstadoReserva> estados, LocalDate fechaFin, LocalDate fechaInicio);

    boolean existsByAlojamientoIdAndIdNotAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
            Long alojamientoId, Long id, List<EstadoReserva> estados, LocalDate fechaFin, LocalDate fechaInicio);

    boolean existsByClienteIdAndAlojamientoIdAndEstadoAndFechaFinBefore(Long clienteId, Long alojamientoId, EstadoReserva estado, LocalDate now);

    List<Reserva> findByCliente_Email(String email);

    List<Reserva> findByAlojamientoIdIn(List<Long> alojamientoIds);

    /// Reservas de un alojamiento en estados que ocupan calendario (para pintar el calendario de disponibilidad).
    List<Reserva> findByAlojamientoIdAndEstadoIn(Long alojamientoId, List<EstadoReserva> estados);

    void deleteByAlojamientoId(Long alojamientoId);

    /// Usado por el scheduler para vencer solicitudes ACEPTADAS que no se pagaron a tiempo.
    List<Reserva> findByEstadoAndFechaLimitePagoBefore(EstadoReserva estado, LocalDateTime momento);

    /// Usado por el scheduler para cerrar chats 48hs despues de finalizada la estadia.
    List<Reserva> findByEstadoAndFechaFinalizacionBefore(EstadoReserva estado, LocalDateTime momento);
}
