package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReservaModificarDTO;
import GestionAlojamiento.DTO.ReservaRegistroDTO;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoEstado;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Repository.ClienteRepository;
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
    private final ClienteRepository clienteRepository;
    private final AlojamientoService alojamientoService;
    private final DisponibilidadService disponibilidadService;

    //------------------------ LISTAR ------------------------

    public List<Reserva> listarTodos() {
        return reservaRepository.findAll();
    }

    public Reserva mostarPorId(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, la reserva no se encuentra en la base de datos"));
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Reserva crear(ReservaRegistroDTO dto) {

        Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());

        Cliente cliente = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> new RuntimeException("El cliente no se encuentra en la base de datos."));


        // VALIDACIONES

        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        if (dto.getFechaInicio().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior al dia actual.");
        }


        // DISPONIBILIDAD

        disponibilidadService.ocuparFechas(dto.getFechaInicio(), dto.getFechaFin());


        // RESERVA

        Reserva reserva = new Reserva();

        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());

        reserva.setTipoEstado(dto.getTipoEstado());

        reserva.setCliente(cliente);
        reserva.setAlojamiento(alojamiento);

        reserva.setPrecioTotal(calcularPrecio(alojamiento, dto.getFechaInicio(), dto.getFechaFin()));

        return reservaRepository.save(reserva);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Error, la reserva no existe");
        }
        //Liberamos las fechas
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, la reserva no existe"));
        disponibilidadService.liberarFechas(reserva.getFechaInicio(), reserva.getFechaFin());

        reservaRepository.deleteById(id);
    }

    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Reserva actualizar(ReservaModificarDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Reserva inexistente"));

        // FECHAS NUEVAS

        LocalDate fechaInicio = reserva.getFechaInicio();
        LocalDate fechaFin = reserva.getFechaFin();

        if (dto.getFechaInicio() != null) {
            fechaInicio = dto.getFechaInicio();
        }

        if (dto.getFechaFin() != null) {
            fechaFin = dto.getFechaFin();
        }

        // VALIDACIONES

        if (fechaFin.isBefore(fechaInicio)) {
            throw new RuntimeException("La fecha fin no puede ser anterior a la fecha inicio");
        }

        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha inicio no puede ser anterior al dia actual");
        }

        // LIBERAR FECHAS VIEJAS

        disponibilidadService.liberarFechas(reserva.getFechaInicio(), reserva.getFechaFin());

        // OCUPAR NUEVAS FECHAS

        disponibilidadService.ocuparFechas(fechaInicio, fechaFin);

        // SETEAR FECHAS

        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);

        // ESTADO

        if (dto.getTipoEstado() != null) {
            reserva.setTipoEstado(dto.getTipoEstado());
        }

        // CLIENTE

        if (dto.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> new RuntimeException("Cliente inexistente"));
            reserva.setCliente(cliente);
        }

        // ALOJAMIENTO

        if (dto.getIdAlojamiento() != null) {
            Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());
            reserva.setAlojamiento(alojamiento);
        }

        // PRECIO

        reserva.setPrecioTotal(calcularPrecio(reserva.getAlojamiento(), fechaInicio, fechaFin));

        return reservaRepository.save(reserva);
    }

    public BigDecimal calcularPrecio(Alojamiento alojamiento, LocalDate inicio, LocalDate fin) {

        long totalDiasReserva = ChronoUnit.DAYS.between(inicio, fin);//ChronoUnit es para calcular tiempo entre fechas

        return alojamiento.getPrecioNoche().multiply(BigDecimal.valueOf(totalDiasReserva));
    }


}
