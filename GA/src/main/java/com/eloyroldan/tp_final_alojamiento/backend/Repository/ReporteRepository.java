/// Acceso a los reportes de usuarios sobre alojamientos o personas (RF44).
package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    /// Regla 5 de RF44: nadie puede tener dos reportes PENDIENTES sobre el mismo
    /// objetivo. Se consulta por autor, tipo de objetivo y objetivo.
    boolean existsByAutor_IdAndTipoAndIdObjetivoAndEstado(Long idAutor, TIPOREPORTE tipo, Long idObjetivo,
                                                          ESTADOREPORTE estado);

    /// Consulta del listado de moderacion (SCRUM-79): trae la relacion autor con
    /// JOIN FETCH para evitar N+1, filtra opcionalmente por estado y ordena
    /// pendientes primero. El nombre del objetivo se resuelve aparte, en una sola
    /// consulta agrupada por tipo de objetivo.
    @Query("""
            select r from Reporte r
            join fetch r.autor
            where (:estado is null or r.estado = :estado)
            order by r.estado asc, r.fechaCreacion desc
            """)
    List<Reporte> findParaAdmin(@Param("estado") ESTADOREPORTE estado);

    /// Reportes pendientes de un mismo objetivo, para cerrarlos en cadena cuando
    /// la moderacion oculta un alojamiento o desactiva una cuenta.
    List<Reporte> findByTipoAndIdObjetivoAndEstado(TIPOREPORTE tipo, Long idObjetivo,
                                                   ESTADOREPORTE estado);
}
