package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByAlojamiento_IdAlojamientoIn(List<Long> idsAlojamiento);

    void deleteByAlojamiento_IdAlojamiento(Long idAlojamiento);

    /// Bloqueo pesimista: usado en las transiciones de estado (confirmar/cancelar/
    /// finalizar) para que dos requests simultaneos sobre la MISMA reserva no
    /// pasen juntos el chequeo de estado actual (condicion de carrera: los dos
    /// leen CONFIRMADA antes de que ninguno guarde el cambio a FINALIZADA, y
    /// terminan disparando el Payout/reembolso dos veces). Requiere correr
    /// dentro de una transaccion activa (@Transactional).
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reserva r where r.idReserva = :id")
    Optional<Reserva> findByIdConLock(@Param("id") Long id);

    //================================ OPTIMIZACION N+1 ================================
    // ReservaResponseDTO.desde() necesita alojamiento + cliente. Sin JOIN FETCH,
    // listar N reservas dispara consultas extra por cada relacion LAZY de cada fila.

    @Query("select r from Reserva r join fetch r.alojamiento join fetch r.cliente")
    List<Reserva> findAllConDetalle();

    @Query("select r from Reserva r join fetch r.alojamiento join fetch r.cliente where r.cliente.id = :idCliente")
    List<Reserva> findByClienteIdConDetalle(@Param("idCliente") Long idCliente);

    @Query("select r from Reserva r join fetch r.alojamiento join fetch r.cliente where r.alojamiento.idAlojamiento in :idsAlojamiento")
    List<Reserva> findByAlojamientoIdInConDetalle(@Param("idsAlojamiento") List<Long> idsAlojamiento);

    /// Verifica solapamiento de fechas contra reservas existentes de un alojamiento (alta nueva).
    /// Excluye estadoExcluido (se usa con CANCELADA: una reserva cancelada libera sus fechas).
    boolean existsByAlojamiento_IdAlojamientoAndEstadoReservaNotAndFechaInicioLessThanAndFechaFinGreaterThan(
            Long idAlojamiento, ESTADORESERVA estadoExcluido, LocalDate fechaFin, LocalDate fechaInicio);

    /// Igual al anterior pero excluyendo la propia reserva (para modificaciones/reprogramaciones).
    boolean existsByAlojamiento_IdAlojamientoAndIdReservaNotAndEstadoReservaNotAndFechaInicioLessThanAndFechaFinGreaterThan(
            Long idAlojamiento, Long idReserva, ESTADORESERVA estadoExcluido, LocalDate fechaFin, LocalDate fechaInicio);

    /// Usado antes de borrar un alojamiento: no se permite si tiene reservas todavia
    /// no resueltas (SOLICITADA o CONFIRMADA) para no borrarle una estadia a un cliente.
    boolean existsByAlojamiento_IdAlojamientoAndEstadoReservaIn(Long idAlojamiento, List<ESTADORESERVA> estados);

    //================================ ESTADISTICAS ================================

    /// Version "en lote" de lo que necesita EstadisticasController.resumenAnfitrion:
    /// 1 sola consulta agrupada para todos los alojamientos de un anfitrion.
    @Query("""
            select r.alojamiento.idAlojamiento, count(r)
            from Reserva r
            where r.alojamiento.idAlojamiento in :idsAlojamiento
              and r.estadoReserva <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA.CANCELADA
            group by r.alojamiento.idAlojamiento
            """)
    List<Object[]> cantidadPorAlojamientosRaw(@Param("idsAlojamiento") List<Long> idsAlojamiento);

    /// Top alojamientos por cantidad de reservas concretadas (admin, global).
    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO(
                a.idAlojamiento, a.titulo, count(r), 'reservas'
            )
            from Reserva r
            join r.alojamiento a
            where r.estadoReserva <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA.CANCELADA
            group by a.idAlojamiento, a.titulo
            order by count(r) desc
            """)
    List<AlojamientoRankingDTO> topAlojamientosPorCantidadReservas(Pageable pageable);
}
