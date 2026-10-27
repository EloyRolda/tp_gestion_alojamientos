package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /// Todas las reviews que recibio un alojamiento (a traves de sus reservas).
    List<Review> findByReserva_Alojamiento_IdAlojamiento(Long idAlojamiento);

    /// Todas las reviews que escribio un cliente.
    List<Review> findByReserva_Cliente_Id(Long idCliente);

    Optional<Review> findByReserva_IdReserva(Long idReserva);

    boolean existsByReserva_IdReserva(Long idReserva);

    //================================ ESTADISTICAS ================================

    /// Version "en lote" de lo que necesita EstadisticasController.resumenAnfitrion:
    /// para todos los alojamientos de un anfitrion.
    @Query("""
            select rv.reserva.alojamiento.idAlojamiento, avg(rv.puntuacion)
            from Review rv
            where rv.reserva.alojamiento.idAlojamiento in :idsAlojamiento
            group by rv.reserva.alojamiento.idAlojamiento
            """)
    List<Object[]> promedioPorAlojamientosRaw(@Param("idsAlojamiento") List<Long> idsAlojamiento);

    /// Top alojamientos por rating promedio (admin, global).
    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO(
                a.idAlojamiento, a.titulo, avg(rv.puntuacion), 'rating'
            )
            from Review rv
            join rv.reserva r
            join r.alojamiento a
            group by a.idAlojamiento, a.titulo
            order by avg(rv.puntuacion) desc
            """)
    List<AlojamientoRankingDTO> topAlojamientosPorRating(Pageable pageable);
}
