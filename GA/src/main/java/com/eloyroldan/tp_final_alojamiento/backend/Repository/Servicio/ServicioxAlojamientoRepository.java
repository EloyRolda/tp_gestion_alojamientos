package com.eloyroldan.tp_final_alojamiento.backend.Repository.Servicio;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.ServicioxAlojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioxAlojamientoRepository extends JpaRepository<ServicioxAlojamiento, Long> {

    List<ServicioxAlojamiento> findByAlojamiento_IdAlojamiento(Long idAlojamiento);

    /// Util para que el admin pueda reasignar/fusionar un servicio duplicado
    /// (ej: "Labarropa" -> "Lavarropa") en todos los alojamientos que lo tengan.
    List<ServicioxAlojamiento> findByServicio_IdServicio(Long idServicio);

    boolean existsByAlojamiento_IdAlojamientoAndServicio_IdServicio(Long idAlojamiento, Long idServicio);

    void deleteByAlojamiento_IdAlojamiento(Long idAlojamiento);
}
