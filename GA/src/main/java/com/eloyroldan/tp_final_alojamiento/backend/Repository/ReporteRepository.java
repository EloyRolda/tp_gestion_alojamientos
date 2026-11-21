/// Acceso a los reportes de usuarios sobre alojamientos o personas (RF44).
package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    /// Regla 5 de RF44: nadie puede tener dos reportes PENDIENTES sobre el mismo
    /// objetivo. Se consulta por autor, tipo de objetivo y objetivo.
    boolean existsByAutor_IdAndTipoAndIdObjetivoAndEstado(Long idAutor, TIPOREPORTE tipo, Long idObjetivo,
                                                          ESTADOREPORTE estado);
}
