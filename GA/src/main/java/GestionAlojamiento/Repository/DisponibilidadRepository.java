package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {


    List<Disponibilidad> findByDisponibleTrue();

    boolean existsByFecha(LocalDate nuevaFecha);

    Disponibilidad findByFecha(LocalDate localDate);

    List<Disponibilidad> findByFechaBetween(LocalDate inicio, LocalDate fin);
}