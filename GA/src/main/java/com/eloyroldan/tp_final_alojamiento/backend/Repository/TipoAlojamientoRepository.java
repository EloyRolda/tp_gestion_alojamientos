package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Model.TipoAlojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// Catalogo dinamico de tipos de alojamiento (ver TipoAlojamientoService.obtenerOcrear).
@Repository
public interface TipoAlojamientoRepository extends JpaRepository<TipoAlojamiento, Long> {
    Optional<TipoAlojamiento> findByNombreTipoIgnoreCase(String nombreTipo);
    boolean existsByNombreTipoIgnoreCase(String nombreTipo);
}
