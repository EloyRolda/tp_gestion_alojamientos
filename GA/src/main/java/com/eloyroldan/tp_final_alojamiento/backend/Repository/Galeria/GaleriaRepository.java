package com.eloyroldan.tp_final_alojamiento.backend.Repository.Galeria;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Galeria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// Regla de aplicacion (no de BD): una galeria por alojamiento.
@Repository
public interface GaleriaRepository extends JpaRepository<Galeria, Long> {
    Optional<Galeria> findByAlojamiento_IdAlojamiento(Long idAlojamiento);
    void deleteByAlojamiento_IdAlojamiento(Long idAlojamiento);
}
