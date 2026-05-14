package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Repository.ClienteRepository;
import GestionAlojamiento.Repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
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
    public Reserva crear(Long id_Alojamiento, Long id_Cliente, Reserva reserva) {

        Alojamiento alojamiento = alojamientoService.obtenerPorId(id_Alojamiento);
        Cliente cliente = clienteRepository.findById(id_Cliente).orElseThrow(() -> new RuntimeException("El Alojamiento no se encuentra en la base de datos."));

        disponibilidadService.ocuparFechas(reserva.getFechaInicio(), reserva.getFechaFin());

        reserva.setCliente(cliente);
        reserva.setAlojamiento(alojamiento);
        reserva.setPrecioTotal(calcularPrecio(alojamiento, reserva.getFechaInicio(), reserva.getFechaFin()));
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
    //Deberia liberar la fecha pertinente y asignar la nueva.
    public Reserva actualizarFecha_inicio(Long id_reserva, LocalDate nuevaFecha) {
        Reserva reserva = reservaRepository.findById(id_reserva).orElseThrow(() -> new RuntimeException("Error, la reserva no existe en la base de datos."));
        //checkear fecha validas
        if (reserva.getFechaFin().isBefore(nuevaFecha)) {
            throw new RuntimeException("La fecha debe ser anterior a la fecha de fin");
        }
        LocalDate antiguaFecha = reserva.getFechaInicio();
        disponibilidadService.cambiarDisponibleInicioAFin(antiguaFecha, nuevaFecha);
        reserva.setFechaInicio(nuevaFecha);

        actualizarPrecioReserva(id_reserva);
        return reservaRepository.save(reserva);
    }

    @Transactional
    //Deberia revisar entre fechas y verificar que este disponible
    public Reserva actualizarFecha_fin(Long id_reserva, LocalDate nuevaFecha) {
        Reserva reserva = reservaRepository.findById(id_reserva).orElseThrow(() -> new RuntimeException("Error, la reserva no existe en la base de datos."));
        if (reserva.getFechaInicio().isAfter(nuevaFecha)) {
            throw new RuntimeException("La fecha debe ser posterior a la fecha de fin");
        }
        disponibilidadService.cambiarDisponibleInicioAFin(reserva.getFechaFin(), nuevaFecha);
        reserva.setFechaFin(nuevaFecha);

        actualizarPrecioReserva(id_reserva);
        return reservaRepository.save(reserva);
    }


    @Transactional
    public Reserva actualizarPrecioReserva(Long id_reserva) {
        Reserva reserva = reservaRepository.findById(id_reserva).orElseThrow(() -> new RuntimeException("Reserva inexistente"));
        Alojamiento alojamiento = alojamientoService.obtenerPorId(reserva.getAlojamiento().getId());

        Long totalDiasReserva = reservaRepository.countByFechaInicioBetween(reserva.getFechaInicio(), reserva.getFechaFin());

        reserva.setPrecioTotal(alojamiento.getPrecioNoche().multiply(BigDecimal.valueOf(totalDiasReserva)));

        return reservaRepository.save(reserva);
    }

    public BigDecimal calcularPrecio(Alojamiento alojamiento, LocalDate inicio, LocalDate fin) {

        Long totalDiasReserva = reservaRepository.countByFechaInicioBetween(inicio, fin);

        return alojamiento.getPrecioNoche().multiply(BigDecimal.valueOf(totalDiasReserva));
    }


}
