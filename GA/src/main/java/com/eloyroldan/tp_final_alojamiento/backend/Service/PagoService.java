package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CancelacionPorMotivoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CobroPendienteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.FlujoDineroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.IngresoMensualDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.PagoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.PreferenciaMercadoPagoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ResumenClienteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.PagoRepository;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Cancelacion.PoliticaCancelacion;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/// Orquesta todo el ciclo de vida financiero de una Reserva: crear el pago al
/// confirmar, procesar el webhook de MercadoPago, resolver reembolsos/retenciones
/// al cancelar, y disparar el Payout al anfitrion (al finalizar o en cancelacion
/// tardia). Es el unico Service que toca PagoRepository.
@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private static final int HORAS_PLAZO_PAGO = 48;

    private final PagoRepository pagoRepository;
    private final PasarelaDePagoService pasarelaDePagoService;
    private final MeterRegistry meterRegistry;

    /// Una PoliticaCancelacion por MOTIVOCANCELACION (Strategy): Spring inyecta
    /// todos los beans que implementan la interfaz, y se indexan por motivo en
    /// indexarPoliticas() para no recorrer la lista en cada cancelacion. Agregar
    /// un motivo nuevo es agregar una clase en Service/Cancelacion/, no editar
    /// un switch acá (ver PoliticaCancelacion).
    private final List<PoliticaCancelacion> politicasCancelacion;
    private Map<MOTIVOCANCELACION, PoliticaCancelacion> politicasPorMotivo;

    /// ReservaService depende de PagoService (crearPago/ejecutarCancelacion/
    /// ejecutarPayoutFinalizacion en cada transicion de estado), asi que esta
    /// referencia de vuelta (solo usada por el job de vencimiento) se inyecta @Lazy.
    @Lazy
    private final ReservaService reservaService;

    @Value("${pago.comision-plataforma-porcentaje}")
    private BigDecimal comisionPlataformaPorcentaje;

    @Value("${pago.comision-mercadopago-estimada-porcentaje}")
    private BigDecimal comisionMercadopagoEstimadaPorcentaje;

    @PostConstruct
    void indexarPoliticas() {
        politicasPorMotivo = new HashMap<>();
        for (PoliticaCancelacion politica : politicasCancelacion) {
            for (MOTIVOCANCELACION motivo : politica.motivosQueAplica()) {
                politicasPorMotivo.put(motivo, politica);
            }
        }
    }

    //---------------------------------------- OBTENER ----------------------------------------

    public Pago obtenerPorReserva(Long idReserva) {
        return pagoRepository.findByReserva_IdReserva(idReserva)
                .orElseThrow(() -> new IdNoEncontradoException("Esta reserva todavia no tiene un pago asociado."));
    }

    /// Usado por ReservaService para embeber el Pago en ReservaResponseDTO sin
    /// tener que tocar PagoRepository directamente desde otro Service.
    public Optional<Pago> obtenerPorReservaOpcional(Long idReserva) {
        return pagoRepository.findByReserva_IdReserva(idReserva);
    }

    /// Version en lote de obtenerPorReservaOpcional: 1 sola consulta para toda
    /// la lista, en vez de 1 por reserva. Usado al armar listados (evita N+1).
    public Map<Long, Pago> obtenerMapaPorReservas(List<Long> idsReserva) {
        if (idsReserva.isEmpty()) {
            return Map.of();
        }
        return pagoRepository.findByReserva_IdReservaIn(idsReserva).stream()
                .collect(Collectors.toMap(p -> p.getReserva().getIdReserva(), p -> p));
    }

    //---------------------------------------- INTEGRIDAD REFERENCIAL ----------------------------------------

    /// Debe llamarse ANTES de borrar una Reserva puntual: pago.id_reserva tiene FK
    /// hacia reserva sin cascada, asi que borrar la Reserva primero rompe esa FK
    /// si ya tenia un Pago asociado (lo normal en cualquier reserva que llego a
    /// CONFIRMADA alguna vez, incluso si despues se cancelo o finalizo).
    @Transactional
    public void borrarPorReserva(Long idReserva) {
        pagoRepository.deleteByReserva_IdReserva(idReserva);
    }

    /// Igual que borrarPorReserva pero para TODAS las reservas de un alojamiento
    /// de una sola vez. Debe llamarse ANTES de AlojamientoService.borrarPorId
    /// (via ReservaService.borrarPorAlojamientoId) borre las reservas en si.
    @Transactional
    public void borrarPorAlojamiento(Long idAlojamiento) {
        pagoRepository.deleteByReserva_Alojamiento_IdAlojamiento(idAlojamiento);
    }

    //---------------------------------------- CREAR (al confirmar) ----------------------------------------

    /// Se llama apenas la reserva pasa a CONFIRMADA. Calcula el gross-up (el
    /// anfitrion recibe montoBase intacto, el cliente paga montoBase + ambas
    /// comisiones) y crea la preferencia de pago en MercadoPago.
    @Transactional
    public PreferenciaMercadoPagoDTO crearPagoParaReserva(Reserva reserva) {
        BigDecimal montoBase = calcularMontoBase(reserva);
        BigDecimal comisionPlataforma = MontoUtil.redondear(montoBase.multiply(comisionPlataformaPorcentaje)
                .divide(new BigDecimal("100")));
        BigDecimal comisionMercadopago = MontoUtil.redondear(montoBase.multiply(comisionMercadopagoEstimadaPorcentaje)
                .divide(new BigDecimal("100")));
        BigDecimal montoTotalCliente = montoBase.add(comisionPlataforma).add(comisionMercadopago);

        Pago pago = Pago.builder()
                .montoBase(montoBase)
                .comisionPlataforma(comisionPlataforma)
                .comisionMercadopago(comisionMercadopago)
                .montoTotalCliente(montoTotalCliente)
                .estado(ESTADOPAGO.PENDIENTE)
                .fechaLimitePago(LocalDateTime.now().plusHours(HORAS_PLAZO_PAGO))
                .reserva(reserva)
                .build();

        PreferenciaMercadoPagoDTO preferencia = meterRegistry.timer("mercadopago.crear_preferencia")
                .record(() -> pasarelaDePagoService.crearPreferencia(
                        referenciaExterna(reserva.getIdReserva()),
                        montoTotalCliente,
                        "Reserva #" + reserva.getIdReserva() + " - " + reserva.getAlojamiento().getTitulo(),
                        reserva.getCliente().getEmail()));

        pago.setUrlCheckout(preferencia.getUrlCheckout());
        pagoRepository.save(pago);

        return preferencia;
    }

    private String referenciaExterna(Long idReserva) {
        return "RESERVA-" + idReserva;
    }

    /// No confia en Reserva.precioTotal (columna GENERATED de la base): en MariaDB
    /// 10.4, un ALTER TABLE de Hibernate sobre OTRA columna puede hacer que la
    /// tabla se reconstruya y la definicion GENERATED se pierda, dejando la
    /// columna en null. Se recalcula acá mismo, en Java, con los mismos datos
    /// (precio_noche_pactado * noches) para no depender de eso.
    private BigDecimal calcularMontoBase(Reserva reserva) {
        long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        return reserva.getPrecioNochePactado().multiply(BigDecimal.valueOf(noches));
    }

    //---------------------------------------- WEBHOOK ----------------------------------------

    /// Se llama desde el endpoint publico de webhook. Nunca confia en el body de la
    /// notificacion: vuelve a preguntarle a MercadoPago el estado real del pago
    /// antes de marcar nada. Idempotente: si ya estaba PAGADO no hace nada de nuevo.
    @Transactional
    public void procesarNotificacionPago(String idPagoExterno) {
        Map<String, Object> datosPago = pasarelaDePagoService.consultarPago(idPagoExterno);
        String estadoMp = String.valueOf(datosPago.get("status"));
        String referenciaExterna = String.valueOf(datosPago.get("external_reference"));

        if (referenciaExterna == null || !referenciaExterna.startsWith("RESERVA-")) {
            log.warn("Webhook de MercadoPago con external_reference inesperada: {}", referenciaExterna);
            return;
        }

        Long idReserva;
        try {
            idReserva = Long.valueOf(referenciaExterna.replace("RESERVA-", ""));
        } catch (NumberFormatException e) {
            // Puede pasar si alguien le pega al webhook a mano con un id de pago
            // real de MercadoPago que no fue creado por esta app (o con datos
            // adulterados). No es un error nuestro: se ignora sin romper nada.
            log.warn("Webhook con external_reference no numerica, se ignora: {}", referenciaExterna);
            return;
        }

        Pago pago = obtenerPorReserva(idReserva);

        if (pago.getEstado() != ESTADOPAGO.PENDIENTE) {
            log.info("Webhook ignorado: el pago de la reserva {} ya estaba en estado {}", idReserva, pago.getEstado());
            return;
        }

        if ("approved".equals(estadoMp)) {
            pago.setEstado(ESTADOPAGO.PAGADO);
            pago.setIdPagoExterno(idPagoExterno);
            pago.setFechaPago(LocalDateTime.now());
            pagoRepository.save(pago);
            meterRegistry.counter("pagos.procesados", "resultado", "aprobado").increment();
            log.info("Pago confirmado para la reserva {}", idReserva);
        } else {
            meterRegistry.counter("pagos.procesados", "resultado", estadoMp).increment();
            log.info("Webhook recibido para la reserva {} con estado MercadoPago '{}' (sin cambios)", idReserva, estadoMp);
        }
    }

    //---------------------------------------- CANCELACION ----------------------------------------

    /// Resuelve el reembolso/retencion segun el motivo de cancelacion. Si el pago
    /// todavia estaba PENDIENTE (nunca se pago), no hay nada que reembolsar ni
    /// transferir: solo se deja constancia del motivo.
    /// montoReembolsoManual solo se usa (y es obligatorio) cuando motivo es ADMIN.
    @Transactional
    public void ejecutarCancelacion(Reserva reserva, MOTIVOCANCELACION motivo, BigDecimal montoReembolsoManual) {
        Pago pago = obtenerPorReserva(reserva.getIdReserva());
        pago.setMotivoCancelacion(motivo);

        if (pago.getEstado() == ESTADOPAGO.PENDIENTE) {
            if (motivo == MOTIVOCANCELACION.VENCIMIENTO_PAGO) {
                pago.setEstado(ESTADOPAGO.VENCIDO);
            }
            pagoRepository.save(pago);
            return;
        }

        if (pago.getEstado() != ESTADOPAGO.PAGADO) {
            throw new ParametroInvalidoException("Este pago ya fue reembolsado/procesado, no se puede cancelar de nuevo.");
        }

        // VENCIMIENTO_PAGO no tiene PoliticaCancelacion propia: un pago que
        // llega hasta aca ya fue PAGADO, asi que "se vencio sin pagarse" es una
        // inconsistencia de datos, no un caso normal a resolver.
        if (motivo == MOTIVOCANCELACION.VENCIMIENTO_PAGO) {
            throw new ParametroInvalidoException(
                    "VENCIMIENTO_PAGO no deberia llegar aca con un pago ya PAGADO (inconsistencia de datos).");
        }

        PoliticaCancelacion politica = politicasPorMotivo.get(motivo);
        if (politica == null) {
            throw new ParametroInvalidoException("No hay una politica de cancelacion configurada para el motivo " + motivo + ".");
        }
        politica.aplicar(pago, reserva, montoReembolsoManual);

        pagoRepository.save(pago);
    }

    //---------------------------------------- PAYOUT AL FINALIZAR ----------------------------------------

    /// Se llama cuando la reserva pasa a FINALIZADA: transfiere el 100% del monto
    /// base al anfitrion.
    @Transactional
    public void ejecutarPayoutFinalizacion(Reserva reserva) {
        Pago pago = obtenerPorReserva(reserva.getIdReserva());
        if (pago.getEstado() != ESTADOPAGO.PAGADO) {
            throw new ParametroInvalidoException("No se puede liquidar: el pago de esta reserva no esta en estado PAGADO.");
        }
        Usuario anfitrion = reserva.getAlojamiento().getAnfitrion();
        if (anfitrion.getAliasCobro() == null || anfitrion.getAliasCobro().isBlank()) {
            throw new ParametroInvalidoException("El anfitrion no tiene cargado un alias/CBU/CVU para cobrar.");
        }

        String idTransferencia = pasarelaDePagoService.crearPayout(
                anfitrion.getAliasCobro(), pago.getMontoBase(), "FINALIZACION-RESERVA-" + reserva.getIdReserva());

        pago.setIdTransferenciaExterna(idTransferencia);
        pago.setFechaTransferencia(LocalDateTime.now());
        pago.setMontoRetenidoAnfitrion(pago.getMontoBase());
        pagoRepository.save(pago);
    }

    //---------------------------------------- JOB: VENCIMIENTO DE PAGO ----------------------------------------

    /// Corre cada 10 minutos: cancela toda reserva CONFIRMADA cuyo Pago sigue
    /// PENDIENTE despues de las 48hs de plazo.
    @Scheduled(fixedRateString = "${pago.job.vencimiento-fixed-rate-ms:600000}")
    @Transactional
    public void cancelarPagosVencidos() {
        List<Pago> vencidos = pagoRepository.findByEstadoAndFechaLimitePagoBefore(ESTADOPAGO.PENDIENTE, LocalDateTime.now());
        for (Pago pago : vencidos) {
            Long idReserva = pago.getReserva().getIdReserva();
            try {
                log.info("Venciendo pago pendiente de la reserva {} (plazo de 48hs superado)", idReserva);
                ejecutarCancelacion(pago.getReserva(), MOTIVOCANCELACION.VENCIMIENTO_PAGO, null);
                reservaService.marcarComoCanceladaPorSistema(idReserva);
            } catch (ParametroInvalidoException e) {
                // Carrera con el webhook: el pago dejo de estar PENDIENTE (ej: se
                // confirmo) entre la consulta de arriba y esta iteracion.
                // ejecutarCancelacion cae en su rama "no deberia llegar aca" y tira
                // esto — sin capturarlo aca, la excepcion aborta TODA la transaccion
                // del batch, incluidos los pagos anteriores ya procesados en esta
                // misma pasada. Se lo trata como no-op: si de verdad se confirmo, ya
                // no corresponde vencerlo; si es otra inconsistencia real, queda
                // logueada para revisar a mano.
                log.warn("No se pudo vencer el pago de la reserva {} (probable carrera con el webhook): {}", idReserva, e.getMessage());
            }
        }
    }

    //---------------------------------------- ESTADISTICAS (admin) ----------------------------------------

    /// desde/hasta null = sin limite en ese extremo (todo el historico).
    public FlujoDineroDTO flujoDeDinero(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeDateTime = desde == null ? null : desde.atStartOfDay();
        LocalDateTime hastaDateTime = hasta == null ? null : hasta.atTime(23, 59, 59);
        return pagoRepository.calcularFlujoDeDinero(desdeDateTime, hastaDateTime);
    }

    public List<AlojamientoRankingDTO> topAlojamientosPorIngresos(int limite) {
        return pagoRepository.topAlojamientosPorIngresos(PageRequest.of(0, limite));
    }

    public List<CancelacionPorMotivoDTO> cancelacionesPorMotivo(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeDateTime = desde == null ? null : desde.atStartOfDay();
        LocalDateTime hastaDateTime = hasta == null ? null : hasta.atTime(23, 59, 59);
        return pagoRepository.cancelacionesPorMotivo(desdeDateTime, hastaDateTime);
    }

    /// Devuelve los 12 meses del anio, con 0 en los que no hubo facturacion (para
    /// no dejarle al front la molestia de "rellenar los huecos").
    public List<IngresoMensualDTO> ingresosMensuales(int anio) {
        Map<Integer, BigDecimal> porMes = new HashMap<>();
        for (Object[] fila : pagoRepository.ingresosMensualesRaw(anio)) {
            Integer mes = ((Number) fila[0]).intValue();
            BigDecimal total = (BigDecimal) fila[1];
            porMes.put(mes, total);
        }
        List<IngresoMensualDTO> resultado = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            resultado.add(new IngresoMensualDTO(anio, mes, porMes.getOrDefault(mes, BigDecimal.ZERO)));
        }
        return resultado;
    }

    //---------------------------------------- ESTADISTICAS (anfitrion) ----------------------------------------

    /// Null si el alojamiento todavia no tuvo ningun pago.
    /// Version en lote (ver EstadisticasController.resumenAnfitrion).
    public Map<Long, BigDecimal> ingresosPorAlojamientos(List<Long> idsAlojamiento) {
        if (idsAlojamiento.isEmpty()) {
            return Map.of();
        }
        Map<Long, BigDecimal> resultado = new HashMap<>();
        for (Object[] fila : pagoRepository.ingresosPorAlojamientosRaw(idsAlojamiento)) {
            resultado.put((Long) fila[0], (BigDecimal) fila[1]);
        }
        return resultado;
    }

    public List<CobroPendienteDTO> cobrosPendientesPorAnfitrion(Long idAnfitrion) {
        return pagoRepository.cobrosPendientesPorAnfitrion(idAnfitrion);
    }

    //---------------------------------------- ESTADISTICAS (cliente) ----------------------------------------

    public ResumenClienteDTO resumenGastoCliente(Long idCliente) {
        return pagoRepository.resumenGastoCliente(idCliente);
    }
}
