package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CancelacionPorMotivoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CobroPendienteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.FlujoDineroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ResumenClienteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByReserva_IdReserva(Long idReserva);

    /// Trae todos los Pago de una lista de reservas de una sola vez (evita el
    /// N+1 de pedirlos uno por uno al armar un listado de ReservaResponseDTO).
    List<Pago> findByReserva_IdReservaIn(List<Long> idsReserva);

    //================================ INTEGRIDAD REFERENCIAL ================================
    // pago.id_reserva tiene FK hacia reserva SIN "ON DELETE CASCADE" (RESTRICT por
    // defecto en InnoDB). Borrar una Reserva que ya tiene un Pago sin borrar antes
    // el Pago viola esa FK. Usado por ReservaService antes de cualquier borrado.

    void deleteByReserva_IdReserva(Long idReserva);

    void deleteByReserva_Alojamiento_IdAlojamiento(Long idAlojamiento);

    Optional<Pago> findByIdPagoExterno(String idPagoExterno);

    /// Usado por el job de vencimiento (cada Pago PENDIENTE cuyo plazo ya paso).
    List<Pago> findByEstadoAndFechaLimitePagoBefore(ESTADOPAGO estado, LocalDateTime momento);

    //================================ ESTADISTICAS (admin) ================================

    /// Panel financiero global. desde/hasta null = sin limite en ese extremo.
    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.FlujoDineroDTO(
                coalesce(sum(p.montoTotalCliente), 0),
                coalesce(sum(p.comisionPlataforma), 0),
                coalesce(sum(p.comisionMercadopago), 0),
                coalesce(sum(p.montoRetenidoAnfitrion), 0),
                coalesce(sum(p.montoReembolsadoCliente), 0),
                count(p)
            )
            from Pago p
            where p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.PENDIENTE
              and p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.VENCIDO
              and (:desde is null or p.fechaCreacion >= :desde)
              and (:hasta is null or p.fechaCreacion <= :hasta)
            """)
    FlujoDineroDTO calcularFlujoDeDinero(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    /// Top alojamientos por ingresos (monto base efectivamente cobrado/liquidable).
    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO(
                a.idAlojamiento, a.titulo, sum(p.montoBase), 'ingresos'
            )
            from Pago p
            join p.reserva r
            join r.alojamiento a
            where p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.PENDIENTE
              and p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.VENCIDO
            group by a.idAlojamiento, a.titulo
            order by sum(p.montoBase) desc
            """)
    List<AlojamientoRankingDTO> topAlojamientosPorIngresos(Pageable pageable);

    /// Cantidad de cancelaciones agrupadas por motivo (CLIENTE_ANTICIPADA, CLIENTE_TARDIA, etc).
    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CancelacionPorMotivoDTO(
                p.motivoCancelacion, count(p)
            )
            from Pago p
            where p.motivoCancelacion is not null
              and (:desde is null or p.fechaCreacion >= :desde)
              and (:hasta is null or p.fechaCreacion <= :hasta)
            group by p.motivoCancelacion
            """)
    List<CancelacionPorMotivoDTO> cancelacionesPorMotivo(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    /// Serie mensual de facturacion para un anio. SQL nativo a proposito (funciones
    /// YEAR/MONTH de MySQL), para tener al menos una consulta 100% SQL manual.
    @Query(value = """
            select month(p.fecha_pago) as mes, coalesce(sum(p.monto_total_cliente), 0) as total
            from pago p
            where p.fecha_pago is not null
              and year(p.fecha_pago) = :anio
            group by month(p.fecha_pago)
            order by mes
            """, nativeQuery = true)
    List<Object[]> ingresosMensualesRaw(@Param("anio") int anio);

    //================================ ESTADISTICAS (anfitrion) ================================

    /// Version "en lote" de lo que necesita EstadisticasController.resumenAnfitrion:
    /// TODOS los alojamientos de un anfitrion, en vez de 1 consulta por cada uno
    /// (usado en EstadisticasController.resumenAnfitrion).
    @Query("""
            select r.alojamiento.idAlojamiento, coalesce(sum(p.montoBase), 0)
            from Pago p
            join p.reserva r
            where r.alojamiento.idAlojamiento in :idsAlojamiento
              and p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.PENDIENTE
              and p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.VENCIDO
            group by r.alojamiento.idAlojamiento
            """)
    List<Object[]> ingresosPorAlojamientosRaw(@Param("idsAlojamiento") List<Long> idsAlojamiento);

    /// Reservas ya pagadas (CONFIRMADA) que todavia no se liquidaron al anfitrion.
    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CobroPendienteDTO(
                r.idReserva, a.titulo, p.montoBase, r.fechaFin
            )
            from Pago p
            join p.reserva r
            join r.alojamiento a
            where a.anfitrion.id = :idAnfitrion
              and p.estado = com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.PAGADO
              and p.fechaTransferencia is null
            order by r.fechaFin asc
            """)
    List<CobroPendienteDTO> cobrosPendientesPorAnfitrion(@Param("idAnfitrion") Long idAnfitrion);

    //================================ ESTADISTICAS (cliente) ================================

    @Query("""
            select new com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ResumenClienteDTO(
                count(p),
                coalesce(sum(p.montoTotalCliente), 0),
                coalesce(avg(p.montoTotalCliente), 0.0)
            )
            from Pago p
            join p.reserva r
            where r.cliente.id = :idCliente
              and p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.PENDIENTE
              and p.estado <> com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO.VENCIDO
            """)
    ResumenClienteDTO resumenGastoCliente(@Param("idCliente") Long idCliente);
}
