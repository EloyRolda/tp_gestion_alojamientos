package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReservaModificarDTO;
import GestionAlojamiento.DTO.ReservaRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteService clienteService;
    private final AlojamientoService alojamientoService;


    //------------------------ LISTAR ------------------------

    public List<Reserva> listarTodos() {
        return reservaRepository.findAll();
    }

    public Reserva mostrarPorId(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Error, la reserva no se encuentra en la base de datos"));
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

        if (!alojamiento.getAnfitrion().getUsuario().getActivo()) {
            throw new ParametroInvalidoException("El anfitrión está inactivo");
        }

        if (!alojamiento.getActivo()) {
            throw new ParametroInvalidoException("El alojamiento está inactivo");
        }

        Cliente cliente = clienteService.obtenerPorId(dto.getIdCliente());

        if (!cliente.getUsuario().getActivo()) {
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
            Cliente cliente = clienteService.obtenerPorId(dto.getIdCliente());

            if (!cliente.getUsuario().getActivo()) {
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
            if (!alojamiento.getAnfitrion().getUsuario().getActivo()) {
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


}
