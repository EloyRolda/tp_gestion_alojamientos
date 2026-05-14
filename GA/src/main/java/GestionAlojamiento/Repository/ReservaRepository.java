package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    public Long countByFechaInicioBetween(LocalDate fecha_inicio, LocalDate fecha_fin);


}
