package com.eloyroldan.tp_final_alojamiento.backend.Repository.Servicio;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// Catalogo dinamico de servicios/comodidades (ver ServicioService.obtenerOcrear).
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    Optional<Servicio> findByNombreServicioIgnoreCase(String nombreServicio);
    boolean existsByNombreServicioIgnoreCase(String nombreServicio);
}
