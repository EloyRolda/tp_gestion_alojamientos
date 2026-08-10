package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.EstadisticasAnfitrionDTO;
import GestionAlojamiento.DTO.EstadisticasClienteDTO;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Model.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/// Agregador de solo-lectura para la seccion de analisis de datos: no tiene
/// repositorio propio, arma sus numeros combinando lo que ya exponen los
/// demas services (respetando que ningun service accede a tablas ajenas).
@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final ReservaService reservaService;
    private final ReviewService reviewService;
    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;

    public EstadisticasAnfitrionDTO estadisticasAnfitrion(String emailAnfitrion) {
        Long idAnfitrion = usuarioService.obtenerPorEmail(emailAnfitrion).getId();

        List<Reserva> reservas = reservaService.listarReservasPorAnfitrion(emailAnfitrion);
        long finalizadas = reservas.stream().filter(r -> r.getEstado() == EstadoReserva.FINALIZADA).count();

        BigDecimal ingresosTotales = reservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.PAGADA || r.getEstado() == EstadoReserva.FINALIZADA)
                .map(Reserva::getPrecioTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int cantidadAlojamientos = alojamientoService.obtenerIdsPorAnfitrion(idAnfitrion).size();

        double calificacionPromedio = alojamientoService.obtenerIdsPorAnfitrion(idAnfitrion).stream()
                .flatMap(idAlojamiento -> reviewService.listarPorAlojamiento(idAlojamiento).stream())
                .mapToInt(Review::getPuntuacion)
                .average()
                .orElse(0.0);

        return new EstadisticasAnfitrionDTO(
                cantidadAlojamientos,
                reservas.size(),
                finalizadas,
                ingresosTotales,
                reservaService.promedioNochesPorEstadia(emailAnfitrion),
                reservaService.tasaDeAceptacion(emailAnfitrion),
                calificacionPromedio,
                reservaService.ingresosPorMes(emailAnfitrion)
        );
    }

    public EstadisticasClienteDTO estadisticasCliente(String emailCliente) {
        List<Reserva> reservas = reservaService.listarPorUsuario(emailCliente);

        long finalizadas = reservas.stream().filter(r -> r.getEstado() == EstadoReserva.FINALIZADA).count();
        long canceladasOVencidas = reservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA || r.getEstado() == EstadoReserva.VENCIDA || r.getEstado() == EstadoReserva.RECHAZADA)
                .count();

        BigDecimal gastoTotal = reservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.PAGADA || r.getEstado() == EstadoReserva.FINALIZADA)
                .map(Reserva::getPrecioTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new EstadisticasClienteDTO(reservas.size(), finalizadas, canceladasOVencidas, gastoTotal);
    }
}
