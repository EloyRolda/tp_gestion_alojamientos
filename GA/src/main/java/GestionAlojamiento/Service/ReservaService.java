package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.IngresoMensualDTO;
import GestionAlojamiento.DTO.RangoFechaDTO;
import GestionAlojamiento.DTO.ReservaSolicitudDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.CategoriaNotificacion;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    /// Ventana para pagar una vez aceptada la solicitud.
    public static final long HORAS_LIMITE_PAGO = 48;

    /// Estados que efectivamente ocupan el calendario de un alojamiento.
    /// Una SOLICITADA todavia no bloquea nada: recien bloquea si el anfitrion la acepta.
    private static final List<EstadoReserva> ESTADOS_QUE_BLOQUEAN = List.of(EstadoReserva.ACEPTADA, EstadoReserva.PAGADA);

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;
    private final NotificacionService notificacionService;
    private final LogService logService;

    //------------------------ LISTAR ------------------------

    public List<Reserva> listarTodos() {
        return reservaRepository.findAll();
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, la reserva no se encuentra en la base de datos"));
    }

    /// Historial completo del cliente (todas las reservas propias, cualquier estado).
    public List<Reserva> listarPorUsuario(String correoUsuario) {
        return reservaRepository.findByCliente_Email(correoUsuario);
    }

    /// Todas las reservas de los alojamientos de un anfitrion.
    public List<Reserva> listarReservasPorAnfitrion(String email) {
        Usuario anfitrion = usuarioService.obtenerPorEmail(email);
        List<Long> alojamientoIds = alojamientoService.obtenerIdsPorAnfitrion(anfitrion.getId());

        if (alojamientoIds.isEmpty()) {
            return new ArrayList<>();
        }
        return reservaRepository.findByAlojamientoIdIn(alojamientoIds);
    }

    /// Solo las solicitudes pendientes de respuesta del anfitrion (bandeja de entrada).
    public List<Reserva> listarSolicitudesPendientes(String emailAnfitrion) {
        return listarReservasPorAnfitrion(emailAnfitrion).stream()
                .filter(r -> r.getEstado() == EstadoReserva.SOLICITADA)
                .collect(Collectors.toList());
    }

    /// Rangos de fechas ocupados de un alojamiento, para pintar el calendario en el frontend.
    public List<RangoFechaDTO> disponibilidad(Long idAlojamiento) {
        return reservaRepository.findByAlojamientoIdAndEstadoIn(idAlojamiento, ESTADOS_QUE_BLOQUEAN).stream()
                .map(r -> new RangoFechaDTO(r.getFechaInicio(), r.getFechaFin()))
                .collect(Collectors.toList());
    }

    //------------------------ SOLICITAR (CLIENTE) ------------------------

    /// El cliente pide una reserva: queda en estado SOLICITADA hasta que el anfitrion responda.
    @Transactional
    public Reserva solicitar(ReservaSolicitudDTO dto, String emailCliente) {

        Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());
        Usuario cliente = usuarioService.obtenerPorEmail(emailCliente);

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new ParametroInvalidoException("La fecha de fin no puede ser anterior o igual a la fecha de inicio.");
        }
        if (dto.getFechaInicio().isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La fecha de inicio no puede ser anterior al dia actual.");
        }
        if (!Boolean.TRUE.equals(alojamiento.getActivo()) || !Boolean.TRUE.equals(alojamiento.getPublicado())) {
            throw new ParametroInvalidoException("El alojamiento no esta disponible para reservar.");
        }
        if (!Boolean.TRUE.equals(alojamiento.getAnfitrion().getActivo())) {
            throw new ParametroInvalidoException("El anfitrión está inactivo");
        }
        if (dto.getCantidadPersonas() > alojamiento.getCapacidad()) {
            throw new ParametroInvalidoException("La cantidad de personas supera la capacidad del alojamiento (" + alojamiento.getCapacidad() + ").");
        }
        if (alojamiento.getAnfitrion().getId().equals(cliente.getId())) {
            throw new ParametroInvalidoException("No podes reservar tu propio alojamiento.");
        }

        boolean solapada = reservaRepository.existsByAlojamientoIdAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
                alojamiento.getId(), ESTADOS_QUE_BLOQUEAN, dto.getFechaFin(), dto.getFechaInicio());
        if (solapada) {
            throw new ParametroInvalidoException("Ya hay una reserva confirmada en esas fechas.");
        }

        Reserva reserva = new Reserva();
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setMensajeSolicitud(dto.getMensajeSolicitud());
        reserva.setEstado(EstadoReserva.SOLICITADA);
        reserva.setCliente(cliente);
        reserva.setAlojamiento(alojamiento);
        reserva.setPrecioTotal(calcularPrecio(alojamiento, dto.getFechaInicio(), dto.getFechaFin()));

        Reserva guardada = reservaRepository.save(reserva);

        notificacionService.crear(alojamiento.getAnfitrion(),
                "Tenes una nueva solicitud de reserva para \"" + alojamiento.getTitulo() + "\".",
                CategoriaNotificacion.RESERVA, "reserva:" + guardada.getId());
        logService.registrar(cliente, "CREAR", "RESERVA", guardada.getId(), "Solicitud de reserva creada");

        return guardada;
    }

    //------------------------ ACEPTAR / RECHAZAR (ANFITRION) ------------------------

    @Transactional
    public Reserva aceptar(Long idReserva, String emailAnfitrion) {
        Reserva reserva = obtenerPorId(idReserva);
        Usuario anfitrion = validarAnfitrionDueno(reserva, emailAnfitrion);

        if (reserva.getEstado() != EstadoReserva.SOLICITADA) {
            throw new ParametroInvalidoException("Solo se pueden aceptar solicitudes en estado SOLICITADA.");
        }

        boolean solapada = reservaRepository.existsByAlojamientoIdAndIdNotAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
                reserva.getAlojamiento().getId(), reserva.getId(), ESTADOS_QUE_BLOQUEAN,
                reserva.getFechaFin(), reserva.getFechaInicio());
        if (solapada) {
            throw new ParametroInvalidoException("Ya aceptaste otra reserva que se solapa con estas fechas.");
        }

        reserva.setEstado(EstadoReserva.ACEPTADA);
        reserva.setFechaAceptacion(LocalDateTime.now());
        reserva.setFechaLimitePago(LocalDateTime.now().plusHours(HORAS_LIMITE_PAGO));
        Reserva guardada = reservaRepository.save(reserva);

        notificacionService.crear(reserva.getCliente(),
                "¡Tu solicitud para \"" + reserva.getAlojamiento().getTitulo() + "\" fue aceptada! Tenes " + HORAS_LIMITE_PAGO + "hs para pagar.",
                CategoriaNotificacion.RESERVA, "reserva:" + guardada.getId());
        logService.registrar(anfitrion, "MODIFICAR", "RESERVA", guardada.getId(), "Solicitud aceptada");

        return guardada;
    }

    @Transactional
    public Reserva rechazar(Long idReserva, String emailAnfitrion, String motivo) {
        Reserva reserva = obtenerPorId(idReserva);
        Usuario anfitrion = validarAnfitrionDueno(reserva, emailAnfitrion);

        if (reserva.getEstado() != EstadoReserva.SOLICITADA) {
            throw new ParametroInvalidoException("Solo se pueden rechazar solicitudes en estado SOLICITADA.");
        }

        reserva.setEstado(EstadoReserva.RECHAZADA);
        reserva.setMotivoRechazo(motivo);
        Reserva guardada = reservaRepository.save(reserva);

        notificacionService.crear(reserva.getCliente(),
                "Tu solicitud para \"" + reserva.getAlojamiento().getTitulo() + "\" fue rechazada.",
                CategoriaNotificacion.RESERVA, "reserva:" + guardada.getId());
        logService.registrar(anfitrion, "MODIFICAR", "RESERVA", guardada.getId(), "Solicitud rechazada: " + motivo);

        return guardada;
    }

    //------------------------ CANCELAR (CLIENTE, antes de pagar) ------------------------

    @Transactional
    public Reserva cancelar(Long idReserva, String emailSolicitante) {
        Reserva reserva = obtenerPorId(idReserva);
        Usuario solicitante = usuarioService.obtenerPorEmail(emailSolicitante);

        boolean esCliente = reserva.getCliente().getId().equals(solicitante.getId());
        boolean esAdmin = solicitante.getTipoUsuario() == GestionAlojamiento.Model.Enums.TipoUsuario.ADMINISTRADOR;

        if (!esCliente && !esAdmin) {
            throw new ParametroInvalidoException("No autorizado para cancelar esta reserva.");
        }
        if (reserva.getEstado() != EstadoReserva.SOLICITADA && reserva.getEstado() != EstadoReserva.ACEPTADA) {
            throw new ParametroInvalidoException("Solo se puede cancelar antes de pagar.");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        Reserva guardada = reservaRepository.save(reserva);

        notificacionService.crear(reserva.getAlojamiento().getAnfitrion(),
                "La reserva de \"" + reserva.getAlojamiento().getTitulo() + "\" fue cancelada por el cliente.",
                CategoriaNotificacion.RESERVA, "reserva:" + guardada.getId());
        logService.registrar(solicitante, "MODIFICAR", "RESERVA", guardada.getId(), "Reserva cancelada");

        return guardada;
    }

    //------------------------ PAGO (invocado por PagoService) ------------------------

    /// Marca la reserva como PAGADA. No lo llama el controller directamente: lo dispara
    /// PagoService una vez que el pago (simulado o real) quedo aprobado.
    @Transactional
    public Reserva confirmarPago(Long idReserva) {
        Reserva reserva = obtenerPorId(idReserva);

        if (reserva.getEstado() != EstadoReserva.ACEPTADA) {
            throw new ParametroInvalidoException("Solo se puede pagar una reserva ACEPTADA.");
        }
        if (reserva.getFechaLimitePago() != null && LocalDateTime.now().isAfter(reserva.getFechaLimitePago())) {
            throw new ParametroInvalidoException("Se vencio la ventana de pago de 48hs para esta reserva.");
        }

        reserva.setEstado(EstadoReserva.PAGADA);
        return reservaRepository.save(reserva);
    }

    //------------------------ FINALIZAR ------------------------

    /// Cierra una estadia ya terminada (fecha_fin en el pasado). Solo el anfitrion dueno o un admin.
    @Transactional
    public Reserva finalizar(Long id, String emailSolicitante) {
        Reserva reserva = obtenerPorId(id);

        if (reserva.getEstado() != EstadoReserva.PAGADA) {
            throw new ParametroInvalidoException("Solo se pueden finalizar reservas con estado PAGADA.");
        }
        if (!reserva.getFechaFin().isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La reserva aún no ha finalizado.");
        }

        Usuario solicitante = usuarioService.obtenerPorEmail(emailSolicitante);
        boolean esAdmin = solicitante.getTipoUsuario() == GestionAlojamiento.Model.Enums.TipoUsuario.ADMINISTRADOR;
        boolean esAnfitrionDuenio = reserva.getAlojamiento().getAnfitrion().getId().equals(solicitante.getId());

        if (!esAdmin && !esAnfitrionDuenio) {
            throw new ParametroInvalidoException("No autorizado para finalizar esta reserva.");
        }

        reserva.setEstado(EstadoReserva.FINALIZADA);
        reserva.setFechaFinalizacion(LocalDateTime.now());
        Reserva guardada = reservaRepository.save(reserva);

        notificacionService.crear(reserva.getCliente(),
                "Tu estadia en \"" + reserva.getAlojamiento().getTitulo() + "\" finalizo. ¡Contanos que tal estuvo!",
                CategoriaNotificacion.RESERVA, "reserva:" + guardada.getId());

        return guardada;
    }

    //------------------------ TAREAS PROGRAMADAS (invocadas por SchedulerService) ------------------------

    /// Vence las reservas ACEPTADAS cuya ventana de 48hs para pagar ya paso.
    @Transactional
    public void vencerSolicitudesSinPagar() {
        List<Reserva> vencidas = reservaRepository.findByEstadoAndFechaLimitePagoBefore(EstadoReserva.ACEPTADA, LocalDateTime.now());
        for (Reserva reserva : vencidas) {
            reserva.setEstado(EstadoReserva.VENCIDA);
            reservaRepository.save(reserva);
            notificacionService.crear(reserva.getCliente(),
                    "Se vencio el plazo para pagar la reserva de \"" + reserva.getAlojamiento().getTitulo() + "\".",
                    CategoriaNotificacion.RESERVA, "reserva:" + reserva.getId());
            notificacionService.crear(reserva.getAlojamiento().getAnfitrion(),
                    "La reserva aceptada de \"" + reserva.getAlojamiento().getTitulo() + "\" vencio sin pago.",
                    CategoriaNotificacion.RESERVA, "reserva:" + reserva.getId());
        }
    }

    //------------------------ ESTADISTICAS (Data Analysis) ------------------------

    /// Ingresos agrupados por mes ("YYYY-MM") en base a reservas PAGADAS o FINALIZADAS.
    public List<IngresoMensualDTO> ingresosPorMes(String emailAnfitrion) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, List<Reserva>> agrupadas = listarReservasPorAnfitrion(emailAnfitrion).stream()
                .filter(r -> r.getEstado() == EstadoReserva.PAGADA || r.getEstado() == EstadoReserva.FINALIZADA)
                .collect(Collectors.groupingBy(r -> r.getFechaInicio().format(formato), TreeMap::new, Collectors.toList()));

        return agrupadas.entrySet().stream()
                .map(e -> new IngresoMensualDTO(
                        e.getKey(),
                        e.getValue().stream().map(Reserva::getPrecioTotal).reduce(BigDecimal.ZERO, BigDecimal::add),
                        e.getValue().size()))
                .collect(Collectors.toList());
    }

    /// Promedio de noches por estadia (reservas PAGADA/FINALIZADA) de un anfitrion.
    public double promedioNochesPorEstadia(String emailAnfitrion) {
        List<Reserva> reservas = listarReservasPorAnfitrion(emailAnfitrion).stream()
                .filter(r -> r.getEstado() == EstadoReserva.PAGADA || r.getEstado() == EstadoReserva.FINALIZADA)
                .toList();

        if (reservas.isEmpty()) {
            return 0.0;
        }
        return reservas.stream()
                .mapToLong(r -> ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin()))
                .average()
                .orElse(0.0);
    }

    /// Porcentaje de solicitudes que el anfitrion termina aceptando (sobre aceptadas + rechazadas).
    public double tasaDeAceptacion(String emailAnfitrion) {
        List<Reserva> reservas = listarReservasPorAnfitrion(emailAnfitrion);
        long aceptadas = reservas.stream().filter(r -> r.getEstado() != EstadoReserva.SOLICITADA && r.getEstado() != EstadoReserva.RECHAZADA).count();
        long rechazadas = reservas.stream().filter(r -> r.getEstado() == EstadoReserva.RECHAZADA).count();

        long total = aceptadas + rechazadas;
        return total == 0 ? 0.0 : (aceptadas * 100.0) / total;
    }

    //------------------------ PRIVADOS ------------------------

    private Usuario validarAnfitrionDueno(Reserva reserva, String emailAnfitrion) {
        Usuario anfitrion = usuarioService.obtenerPorEmail(emailAnfitrion);
        boolean esAdmin = anfitrion.getTipoUsuario() == GestionAlojamiento.Model.Enums.TipoUsuario.ADMINISTRADOR;
        boolean esDuenio = reserva.getAlojamiento().getAnfitrion().getId().equals(anfitrion.getId());

        if (!esAdmin && !esDuenio) {
            throw new ParametroInvalidoException("No autorizado: no sos el anfitrion de este alojamiento.");
        }
        return anfitrion;
    }

    private BigDecimal calcularPrecio(Alojamiento alojamiento, LocalDate inicio, LocalDate fin) {
        long totalDiasReserva = ChronoUnit.DAYS.between(inicio, fin);
        return alojamiento.getPrecioNoche().multiply(BigDecimal.valueOf(totalDiasReserva));
    }
}
