package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReservaModificarDTO;
import GestionAlojamiento.DTO.ReservaRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Model.Enums.TipoEstado;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;


    //------------------------ LISTAR ------------------------

    public List<Reserva> listarTodos() {
        return reservaRepository.findAll();
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Error, la reserva no se encuentra en la base de datos"));
    }

    /// Recibe un id de un anfitrion y devuelve una lista de las reservas es que posee.
    public List<Reserva> listarPorUsuario(String correoUsuario) {
        return reservaRepository.findByCliente_Email(correoUsuario);
    }

    /// devuelve las reservas segun el anfitrion
    public List<Reserva> listarReservasPorAnfitrion(String email) {
        Usuario anfitrion = usuarioService.obtenerPorEmail(email);

        if (anfitrion == null) {
            throw new RuntimeException("Anfitrión no encontrado");
        }

        List<Long> alojamientoIds = alojamientoService.obtenerIdsPorAnfitrion(anfitrion.getId());

        if (alojamientoIds.isEmpty()) {
            return new ArrayList<>();
        }

        return reservaRepository.findByAlojamientoIdIn(alojamientoIds);
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    ///Crea una reserva en la base de datos con el DTO establecido, verifica que esten todos los integrantes pertinentes.
    public Reserva crear(ReservaRegistroDTO dto) {

        Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new ParametroInvalidoException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        if (dto.getFechaInicio().isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La fecha de inicio no puede ser anterior al dia actual.");
        }

        if (!alojamiento.getAnfitrion().getActivo()) {
            throw new ParametroInvalidoException("El anfitrión está inactivo");
        }

        if (!alojamiento.getActivo()) {
            throw new ParametroInvalidoException("El alojamiento está inactivo");
        }

        Usuario cliente = usuarioService.obtenerClientePorId(dto.getIdCliente());

        if (!cliente.getActivo()) {
            throw new ParametroInvalidoException("El cliente está inactivo");
        }

        // RESERVA
        Reserva reserva = new Reserva();
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setTipoEstado(dto.getTipoEstado());
        reserva.setCliente(cliente);
        reserva.setAlojamiento(alojamiento);
        reserva.setPrecioTotal(calcularPrecio(alojamiento, dto.getFechaInicio(), dto.getFechaFin()));

        boolean solapada = reservaRepository.existsByAlojamientoIdAndFechaInicioLessThanAndFechaFinGreaterThan(alojamiento.getId(), dto.getFechaFin(), dto.getFechaInicio());

        if (solapada) {
            throw new ParametroInvalidoException("Ya existe una reserva en esas fechas");
        }
        return reservaRepository.save(reserva);
    }

    @Transactional
    ///Borra de la base de datos la reserva
    public void borrarPorId(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new IdNoEncontradoException("Error, la reserva no existe");
        }
        //Liberamos las fechas
        reservaRepository.deleteById(id);
    }

    @Transactional
    /// Borra todas las reservas asociadas a un alojamiento (usado al eliminar un alojamiento)
    public void borrarPorAlojamientoId(Long alojamientoId) {
        reservaRepository.deleteByAlojamientoId(alojamientoId);
    }

    @Transactional
    /// Finaliza una reserva confirmada cuya fecha de fin ya pasó. Solo puede hacerlo el anfitrión del alojamiento o un administrador.
    public Reserva finalizar(Long id, String emailSolicitante) {
        Reserva reserva = obtenerPorId(id);

        if (reserva.getTipoEstado() != TipoEstado.CONFIRMADA) {
            throw new ParametroInvalidoException("Solo se pueden finalizar reservas con estado CONFIRMADA.");
        }

        if (!reserva.getFechaFin().isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La reserva aún no ha finalizado.");
        }

        Usuario solicitante = usuarioService.obtenerPorEmail(emailSolicitante);
        boolean esAdmin = solicitante.getTipoUsuario() == GestionAlojamiento.Model.Enums.TipoUsuario.ADMINISTRADOR;
        boolean esAnfitrionDueño = reserva.getAlojamiento().getAnfitrion().getId().equals(solicitante.getId());

        if (!esAdmin && !esAnfitrionDueño) {
            throw new ParametroInvalidoException("No autorizado para finalizar esta reserva.");
        }

        reserva.setTipoEstado(TipoEstado.FINALIZADA);
        return reservaRepository.save(reserva);
    }

    //------------------------ MODIFICAR ------------------------

    /// Valida el dto recibido y llena los campos pertinentes
    @Transactional
    public Reserva actualizar(ReservaModificarDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId()).orElseThrow(() -> new IdNoEncontradoException("Reserva inexistente"));

        // FECHAS NUEVAS
        LocalDate fechaInicio = reserva.getFechaInicio();
        LocalDate fechaFin = reserva.getFechaFin();

        if (dto.getFechaInicio() != null) {
            fechaInicio = dto.getFechaInicio();
        }

        if (dto.getFechaFin() != null) {
            fechaFin = dto.getFechaFin();
        }

        // VALIDACIONES FECHAS
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new ParametroInvalidoException("La fecha fin no puede ser anterior a la fecha inicio");
        }

        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new ParametroInvalidoException("La fecha inicio no puede ser anterior al dia actual");
        }

        // SETEAR FECHAS
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);

        // CLIENTE
        if (dto.getIdCliente() != null) {
            Usuario cliente = usuarioService.obtenerClientePorId(dto.getIdCliente());

            if (!cliente.getActivo()) {
                throw new ParametroInvalidoException("El cliente está inactivo");
            }

            reserva.setCliente(cliente);
        }
        if (dto.getTipoEstado() != null) {
            reserva.setTipoEstado(dto.getTipoEstado());
        }
        // ALOJAMIENTO
        if (dto.getIdAlojamiento() != null) {
            Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());

            if (!alojamiento.getActivo()) {
                throw new ParametroInvalidoException("El alojamiento está inactivo");
            }
            if (!alojamiento.getAnfitrion().getActivo()) {
                throw new ParametroInvalidoException("El anfitrión está inactivo");
            }
            reserva.setAlojamiento(alojamiento);
        }

        // SOLAPAMIENTO
        boolean solapada = reservaRepository
                .existsByAlojamientoIdAndIdNotAndFechaInicioLessThanAndFechaFinGreaterThan(
                        reserva.getAlojamiento().getId(),
                        reserva.getId(),
                        fechaFin,
                        fechaInicio
                );

        if (solapada) {
            throw new ParametroInvalidoException("Ya existe una reserva en esas fechas");
        }

        //precio
        reserva.setPrecioTotal(
                calcularPrecio(reserva.getAlojamiento(), fechaInicio, fechaFin)
        );

        return reservaRepository.save(reserva);
    }

    /// Toma el precio de un alojamiento por dia, y devuelve el precio final basado en la fecha de inicio y la fecha fin
    private BigDecimal calcularPrecio(Alojamiento alojamiento, LocalDate inicio, LocalDate fin) {

        long totalDiasReserva = ChronoUnit.DAYS.between(inicio, fin);//ChronoUnit es para calcular tiempo entre fechas

        return alojamiento.getPrecioNoche().multiply(BigDecimal.valueOf(totalDiasReserva));
    }


    /// VERIFICA SI EL CLIENTE TUVO UNA RESERVA CONFIRMADA O FINALIZADA QUE YA PASO Y DEVUELVE V O F
    public boolean tuvisteReservaConfirmada(Long clienteId, Long alojamientoId) {
        return reservaRepository.existsByClienteIdAndAlojamientoIdAndTipoEstadoAndFechaFinBefore(clienteId, alojamientoId, TipoEstado.CONFIRMADA, LocalDate.now())
                || reservaRepository.existsByClienteIdAndAlojamientoIdAndTipoEstadoAndFechaFinBefore(clienteId, alojamientoId, TipoEstado.FINALIZADA, LocalDate.now());
    }


}