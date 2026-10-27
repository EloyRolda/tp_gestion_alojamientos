package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReservaModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReservaRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReservaResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// Estados de reserva: SOLICITADA (creada por el cliente) -> CONFIRMADA (el anfitrion
/// la acepta, arranca el plazo de pago de 48hs) -> FINALIZADA (una vez pasada la fecha
/// fin y liquidado el pago) o CANCELADA (por vencimiento de pago, por el cliente o
/// por el anfitrion, cada caso con su propia regla de reembolso — ver PagoService).
@Service
@RequiredArgsConstructor
public class ReservaService {

    private static final int DIAS_CORTE_CANCELACION = 2; // ~48hs, ver PagoService

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;

    /// PagoService depende de ReservaService solo para el job de vencimiento
    /// (marcarComoCanceladaPorSistema), asi que esa referencia va @Lazy del lado
    /// de PagoService. Ac es al reves: ReservaService usa PagoService en casi
    /// todas las transiciones de estado, asi que esta va eager.
    private final PagoService pagoService;

    /// Metrica operativa (Micrometer/Actuator): cuantas reservas se crean, para
    /// poder graficar el ritmo de altas en el tiempo. Ver /actuator/prometheus.
    private final MeterRegistry meterRegistry;

    //------------------------ LISTAR ------------------------

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, la reserva no se encuentra en la base de datos"));
    }

    /// obtenerPorIdConLock (no obtenerPorId): bloquea la fila de la reserva hasta
    /// que esta transaccion termine, para que dos transiciones de estado
    /// simultaneas sobre la MISMA reserva (ej: dos PATCH /finalizar casi a la vez)
    /// no pasen juntas el chequeo de estado actual y disparen el Payout/reembolso
    /// dos veces. Usar en confirmar/cancelar/cancelarComoAdmin/finalizar.
    public Reserva obtenerPorIdConLock(Long id) {
        return reservaRepository.findByIdConLock(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, la reserva no se encuentra en la base de datos"));
    }

    /// Reservas de todos los alojamientos que administra un anfitrion.
    public List<Reserva> listarPorAnfitrion(Long idAnfitrion) {
        List<Long> idsAlojamiento = alojamientoService.listarPorAnfitrion(idAnfitrion).stream()
                .map(Alojamiento::getIdAlojamiento)
                .toList();
        if (idsAlojamiento.isEmpty()) {
            return List.of();
        }
        return reservaRepository.findByAlojamiento_IdAlojamientoIn(idsAlojamiento);
    }

    //------------------------ CREAR ------------------------

    /// usuarioAutenticado es quien hace el request. Si es CLIENTE, reserva a su propio
    /// nombre (se ignora dto.idCliente); si es ADMIN, debe indicar dto.idCliente.
    @Transactional
    public Reserva crear(ReservaRegistroDTO dto, Usuario usuarioAutenticado) {

        // obtenerPorIdConLock (no obtenerPorId): bloquea la fila del alojamiento
        // hasta que esta transaccion termine, para que dos solicitudes simultaneas
        // sobre el mismo alojamiento no puedan pasar juntas el chequeo de
        // solapamiento de mas abajo (la segunda espera a que la primera termine
        // de guardar, y recien ahi hace su propio chequeo con el dato actualizado).
        Alojamiento alojamiento = alojamientoService.obtenerPorIdConLock(dto.getIdAlojamiento());
        Usuario cliente = resolverCliente(dto.getIdCliente(), usuarioAutenticado);

        validarFechas(dto.getFechaInicio(), dto.getFechaFin());

        if (!alojamiento.getActivo()) {
            throw new ParametroInvalidoException("El alojamiento esta inactivo");
        }
        if (!alojamiento.getAnfitrion().getActivo()) {
            throw new ParametroInvalidoException("El anfitrion esta inactivo");
        }
        if (!cliente.getActivo()) {
            throw new ParametroInvalidoException("El cliente esta inactivo");
        }

        boolean solapada = reservaRepository.existsByAlojamiento_IdAlojamientoAndEstadoReservaNotAndFechaInicioLessThanAndFechaFinGreaterThan(
                alojamiento.getIdAlojamiento(), ESTADORESERVA.CANCELADA, dto.getFechaFin(), dto.getFechaInicio());
        if (solapada) {
            throw new ParametroInvalidoException("Ya existe una reserva en esas fechas");
        }

        Reserva reserva = Reserva.builder()
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .precioNochePactado(alojamiento.getPrecioNoche())
                .estadoReserva(ESTADORESERVA.SOLICITADA)
                .alojamiento(alojamiento)
                .cliente(cliente)
                .build();

        Reserva guardada = reservaRepository.save(reserva);
        meterRegistry.counter("reservas.creadas").increment();
        return guardada;
    }

    private Usuario resolverCliente(Long idClienteDto, Usuario usuarioAutenticado) {
        if (usuarioAutenticado.getTipo() == TIPOUSUARIO.CLIENTE) {
            return usuarioAutenticado;
        }
        if (idClienteDto == null) {
            throw new ParametroInvalidoException("Debe indicar idCliente.");
        }
        return usuarioService.obtenerClientePorId(idClienteDto);
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new ParametroInvalidoException("La fecha de fin no puede ser anterior o igual a la fecha de inicio.");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La fecha de inicio no puede ser anterior al dia actual.");
        }
    }

    //------------------------ MODIFICAR (reprogramar fechas) ------------------------

    /// Solo el cliente dueño (o un admin) puede reprogramar, y solo mientras la reserva este SOLICITADA.
    @Transactional
    public Reserva actualizar(ReservaModificarDTO dto, String emailSolicitante) {
        Reserva reserva = obtenerPorId(dto.getIdReserva());
        Usuario solicitante = usuarioService.obtenerPorIdentificador(emailSolicitante);

        if (!esAdmin(solicitante) && !esClienteDuenio(reserva, solicitante)) {
            throw new ParametroInvalidoException("No autorizado para modificar esta reserva.");
        }
        if (reserva.getEstadoReserva() != ESTADORESERVA.SOLICITADA) {
            throw new ParametroInvalidoException("Solo se pueden reprogramar reservas en estado SOLICITADA.");
        }

        validarFechas(dto.getFechaInicio(), dto.getFechaFin());

        // Mismo criterio que en crear(): bloquea el alojamiento hasta terminar,
        // para cerrar la misma ventana de condicion de carrera al reprogramar.
        alojamientoService.obtenerPorIdConLock(reserva.getAlojamiento().getIdAlojamiento());

        boolean solapada = reservaRepository.existsByAlojamiento_IdAlojamientoAndIdReservaNotAndEstadoReservaNotAndFechaInicioLessThanAndFechaFinGreaterThan(
                reserva.getAlojamiento().getIdAlojamiento(), reserva.getIdReserva(), ESTADORESERVA.CANCELADA, dto.getFechaFin(), dto.getFechaInicio());
        if (solapada) {
            throw new ParametroInvalidoException("Ya existe una reserva en esas fechas");
        }

        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setPrecioNochePactado(reserva.getAlojamiento().getPrecioNoche());

        return reservaRepository.save(reserva);
    }

    //------------------------ TRANSICIONES DE ESTADO ------------------------

    /// El anfitrion dueño del alojamiento (o un admin) acepta la reserva solicitada.
    /// Ahi mismo arranca el plazo de pago (48hs) y se crea el Pago + preferencia de
    /// MercadoPago para que el cliente pueda pagar.
    @Transactional
    public Reserva confirmar(Long id, String emailSolicitante) {
        Reserva reserva = obtenerPorIdConLock(id);
        verificarEsAnfitrionDuenioOAdmin(reserva, emailSolicitante);

        if (reserva.getEstadoReserva() != ESTADORESERVA.SOLICITADA) {
            throw new ParametroInvalidoException("Solo se pueden confirmar reservas en estado SOLICITADA.");
        }
        reserva.setEstadoReserva(ESTADORESERVA.CONFIRMADA);
        reserva.setFechaConfirmacion(LocalDateTime.now());
        reserva = reservaRepository.save(reserva);

        pagoService.crearPagoParaReserva(reserva);
        return reserva;
    }

    /// Cliente dueño, anfitrion dueño o admin pueden cancelar. Determina el motivo
    /// (y por lo tanto la regla de reembolso/retencion) segun quien cancela y,
    /// si es el cliente, cuanto falta para el check-in.
    @Transactional
    public Reserva cancelar(Long id, String emailSolicitante) {
        Reserva reserva = obtenerPorIdConLock(id);
        Usuario solicitante = usuarioService.obtenerPorIdentificador(emailSolicitante);
        boolean esAnfitrionDuenio = esAnfitrionDuenio(reserva, solicitante);

        if (!esAdmin(solicitante) && !esClienteDuenio(reserva, solicitante) && !esAnfitrionDuenio) {
            throw new ParametroInvalidoException("No autorizado para cancelar esta reserva.");
        }
        if (reserva.getEstadoReserva() == ESTADORESERVA.FINALIZADA || reserva.getEstadoReserva() == ESTADORESERVA.CANCELADA) {
            throw new ParametroInvalidoException("La reserva ya esta " + reserva.getEstadoReserva() + ".");
        }

        // Si todavia estaba SOLICITADA (el anfitrion nunca la confirmo), jamas
        // se creo un Pago para ella (PagoService.crearPagoParaReserva solo se
        // llama desde confirmar()) — no hay nada que reembolsar ni retener, asi
        // que no corresponde llamar a PagoService.ejecutarCancelacion: ese
        // metodo asume que el Pago existe y tira IdNoEncontradoException si no.
        boolean yaFueConfirmada = reserva.getEstadoReserva() == ESTADORESERVA.CONFIRMADA;

        reserva.setEstadoReserva(ESTADORESERVA.CANCELADA);
        reserva = reservaRepository.save(reserva);

        if (yaFueConfirmada) {
            MOTIVOCANCELACION motivo = determinarMotivo(reserva, solicitante, esAnfitrionDuenio);
            pagoService.ejecutarCancelacion(reserva, motivo, null);
        }
        return reserva;
    }

    /// El anfitrion cancelando siempre es sin penalidad para el cliente. Si cancela
    /// el cliente, se mira cuanto falta para fecha_inicio contando en dias completos
    /// (no tenemos hora de check-in en el modelo, ver charla de diseño).
    private MOTIVOCANCELACION determinarMotivo(Reserva reserva, Usuario solicitante, boolean esAnfitrionDuenio) {
        if (esAnfitrionDuenio && solicitante.getTipo() == TIPOUSUARIO.ANFITRION) {
            return MOTIVOCANCELACION.ANFITRION;
        }
        LocalDate corte = reserva.getFechaInicio().minusDays(DIAS_CORTE_CANCELACION);
        boolean esTardia = !LocalDate.now().isBefore(corte); // hoy >= corte
        return esTardia ? MOTIVOCANCELACION.CLIENTE_TARDIA : MOTIVOCANCELACION.CLIENTE_ANTICIPADA;
    }

    /// El admin cancela con un monto de reembolso a medida, para casos que no
    /// entran en ninguna de las reglas automaticas.
    @Transactional
    public Reserva cancelarComoAdmin(Long id, BigDecimal montoReembolso) {
        Reserva reserva = obtenerPorIdConLock(id);
        if (reserva.getEstadoReserva() == ESTADORESERVA.FINALIZADA || reserva.getEstadoReserva() == ESTADORESERVA.CANCELADA) {
            throw new ParametroInvalidoException("La reserva ya esta " + reserva.getEstadoReserva() + ".");
        }

        // Mismo criterio que en cancelar(): sin confirmar no hay Pago, nada para
        // reembolsar/retener.
        boolean yaFueConfirmada = reserva.getEstadoReserva() == ESTADORESERVA.CONFIRMADA;

        reserva.setEstadoReserva(ESTADORESERVA.CANCELADA);
        reserva = reservaRepository.save(reserva);

        if (yaFueConfirmada) {
            pagoService.ejecutarCancelacion(reserva, MOTIVOCANCELACION.ADMIN, montoReembolso);
        }
        return reserva;
    }

    /// Finaliza una reserva CONFIRMADA cuya fecha de fin ya paso, y dispara el
    /// Payout del 100% del monto base al anfitrion. Solo el anfitrion dueño o un admin.
    @Transactional
    public Reserva finalizar(Long id, String emailSolicitante) {
        Reserva reserva = obtenerPorIdConLock(id);
        verificarEsAnfitrionDuenioOAdmin(reserva, emailSolicitante);

        if (reserva.getEstadoReserva() != ESTADORESERVA.CONFIRMADA) {
            throw new ParametroInvalidoException("Solo se pueden finalizar reservas con estado CONFIRMADA.");
        }
        if (!reserva.getFechaFin().isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La reserva aun no ha finalizado.");
        }

        reserva.setEstadoReserva(ESTADORESERVA.FINALIZADA);
        reserva = reservaRepository.save(reserva);

        pagoService.ejecutarPayoutFinalizacion(reserva);
        return reserva;
    }

    private void verificarEsAnfitrionDuenioOAdmin(Reserva reserva, String emailSolicitante) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(emailSolicitante);
        if (!esAdmin(solicitante) && !esAnfitrionDuenio(reserva, solicitante)) {
            throw new ParametroInvalidoException("No autorizado para esta operacion sobre la reserva.");
        }
    }

    //------------------------ PERTENENCIA (quien puede tocar esta reserva) ------------------------
    // Antes estas 3 condiciones se repetian armadas a mano (esAdmin/esClienteDuenio/
    // esAnfitrionDuenio) en cada metodo que necesitaba alguna combinacion de ellas
    // (actualizar, cancelar, verificarEsAnfitrionDuenioOAdmin, obtenerDTOVerificandoAcceso).
    // Centralizarlas aca evita que una quede desincronizada de las demas si cambia
    // la regla de "quien es el dueño".

    private boolean esAdmin(Usuario solicitante) {
        return solicitante.getTipo() == TIPOUSUARIO.ADMIN;
    }

    private boolean esClienteDuenio(Reserva reserva, Usuario solicitante) {
        return reserva.getCliente().getId().equals(solicitante.getId());
    }

    private boolean esAnfitrionDuenio(Reserva reserva, Usuario solicitante) {
        return reserva.getAlojamiento().getAnfitrion().getId().equals(solicitante.getId());
    }

    //------------------------ USO INTERNO (otros services) ------------------------

    /// Usado por AlojamientoService al borrar un alojamiento: elimina cada Reserva
    /// (no un bulk delete) para que el orphanRemoval de Reserva.review se dispare igual.
    /// Primero borra los Pago asociados (FK sin cascada, ver PagoService.borrarPorAlojamiento).
    @Transactional
    public void borrarPorAlojamientoId(Long idAlojamiento) {
        pagoService.borrarPorAlojamiento(idAlojamiento);
        reservaRepository.deleteByAlojamiento_IdAlojamiento(idAlojamiento);
    }

    /// True si el alojamiento tiene reservas SOLICITADA o CONFIRMADA (todavia no
    /// resueltas). Se usa para impedir que un anfitrion borre un alojamiento con
    /// estadias pendientes de un cliente.
    public boolean tieneReservasActivas(Long idAlojamiento) {
        return reservaRepository.existsByAlojamiento_IdAlojamientoAndEstadoReservaIn(
                idAlojamiento, List.of(ESTADORESERVA.SOLICITADA, ESTADORESERVA.CONFIRMADA));
    }

    /// Usado por PagoService.cancelarPagosVencidos (el job de las 48hs): solo cambia
    /// el estado, la logica de plata ya la resolvio el job antes de llamar aca.
    @Transactional
    public void marcarComoCanceladaPorSistema(Long idReserva) {
        Reserva reserva = obtenerPorId(idReserva);
        reserva.setEstadoReserva(ESTADORESERVA.CANCELADA);
        reservaRepository.save(reserva);
    }

    //------------------------ PARA CONTROLLERS (DTO) ------------------------

    @Transactional
    public List<ReservaResponseDTO> listarTodosDTO() {
        return mapearListaConPagos(reservaRepository.findAllConDetalle());
    }

    @Transactional
    public List<ReservaResponseDTO> listarPorClienteDTO(Long idCliente) {
        return mapearListaConPagos(reservaRepository.findByClienteIdConDetalle(idCliente));
    }

    @Transactional
    public List<ReservaResponseDTO> listarPorAnfitrionDTO(Long idAnfitrion) {
        List<Long> idsAlojamiento = alojamientoService.listarPorAnfitrion(idAnfitrion).stream()
                .map(Alojamiento::getIdAlojamiento)
                .toList();
        if (idsAlojamiento.isEmpty()) {
            return List.of();
        }
        return mapearListaConPagos(reservaRepository.findByAlojamientoIdInConDetalle(idsAlojamiento));
    }

    /// Arma la lista de DTOs con 1 sola consulta extra para TODOS los pagos
    /// (en vez de una consulta de Pago por cada Reserva de la lista).
    private List<ReservaResponseDTO> mapearListaConPagos(List<Reserva> reservas) {
        List<Long> idsReserva = reservas.stream().map(Reserva::getIdReserva).toList();
        Map<Long, Pago> pagosPorReserva =
                pagoService.obtenerMapaPorReservas(idsReserva);
        return reservas.stream()
                .map(r -> ReservaResponseDTO.desde(r, pagosPorReserva.get(r.getIdReserva())))
                .toList();
    }

    private ReservaResponseDTO mapearConPago(Reserva r) {
        return ReservaResponseDTO.desde(r, pagoService.obtenerPorReservaOpcional(r.getIdReserva()).orElse(null));
    }

    /// Solo el cliente dueño, el anfitrion dueño del alojamiento o un admin pueden verla.
    @Transactional
    public ReservaResponseDTO obtenerDTOVerificandoAcceso(Long id, String emailSolicitante) {
        Reserva reserva = obtenerPorId(id);
        Usuario solicitante = usuarioService.obtenerPorIdentificador(emailSolicitante);

        if (!esAdmin(solicitante) && !esClienteDuenio(reserva, solicitante) && !esAnfitrionDuenio(reserva, solicitante)) {
            throw new ParametroInvalidoException("No autorizado para ver esta reserva.");
        }
        return mapearConPago(reserva);
    }

    @Transactional
    public ReservaResponseDTO crearDTO(ReservaRegistroDTO dto, Usuario usuarioAutenticado) {
        return mapearConPago(crear(dto, usuarioAutenticado));
    }

    @Transactional
    public ReservaResponseDTO actualizarDTO(ReservaModificarDTO dto, String emailSolicitante) {
        return mapearConPago(actualizar(dto, emailSolicitante));
    }

    @Transactional
    public ReservaResponseDTO confirmarDTO(Long id, String emailSolicitante) {
        return mapearConPago(confirmar(id, emailSolicitante));
    }

    @Transactional
    public ReservaResponseDTO cancelarDTO(Long id, String emailSolicitante) {
        return mapearConPago(cancelar(id, emailSolicitante));
    }

    @Transactional
    public ReservaResponseDTO cancelarComoAdminDTO(Long id, BigDecimal montoReembolso) {
        return mapearConPago(cancelarComoAdmin(id, montoReembolso));
    }

    @Transactional
    public ReservaResponseDTO finalizarDTO(Long id, String emailSolicitante) {
        return mapearConPago(finalizar(id, emailSolicitante));
    }

    //------------------------ BORRAR (solo admin, limpieza) ------------------------

    /// Primero borra el Pago asociado si existe (FK sin cascada, ver
    /// PagoService.borrarPorReserva), despues la Reserva.
    @Transactional
    public void borrarPorId(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new IdNoEncontradoException("Error, la reserva no existe");
        }
        pagoService.borrarPorReserva(id);
        reservaRepository.deleteById(id);
    }

    //------------------------ ESTADISTICAS ------------------------

    /// Version en lote (ver EstadisticasController.resumenAnfitrion).
    public Map<Long, Long> cantidadReservasPorAlojamientos(List<Long> idsAlojamiento) {
        if (idsAlojamiento.isEmpty()) {
            return Map.of();
        }
        Map<Long, Long> resultado = new HashMap<>();
        for (Object[] fila : reservaRepository.cantidadPorAlojamientosRaw(idsAlojamiento)) {
            resultado.put((Long) fila[0], (Long) fila[1]);
        }
        return resultado;
    }

    public List<AlojamientoRankingDTO> topPorCantidadReservas(int limite) {
        return reservaRepository.topAlojamientosPorCantidadReservas(
                PageRequest.of(0, limite));
    }
}
