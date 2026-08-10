package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.IngresoMensualDTO;
import GestionAlojamiento.DTO.RangoFechaDTO;
import GestionAlojamiento.DTO.ReservaRechazoDTO;
import GestionAlojamiento.DTO.ReservaSolicitudDTO;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Reserva")
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping("/listar")
    public List<Reserva> listar() {
        return reservaService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Reserva mostrarPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id);
    }

    /// Historial completo del cliente logueado (todos los estados).
    @GetMapping("/listar/propios")
    public List<Reserva> listarPropios(Authentication auth) {
        return reservaService.listarPorUsuario(auth.getName());
    }

    /// Todas las reservas de los alojamientos del anfitrion logueado.
    @GetMapping("/listar/anfitrion")
    public List<Reserva> listarReservasDeMisAlojamientos(Authentication auth) {
        return reservaService.listarReservasPorAnfitrion(auth.getName());
    }

    /// Bandeja de solicitudes pendientes de respuesta (para el anfitrion).
    @GetMapping("/solicitudes/pendientes")
    public List<Reserva> listarSolicitudesPendientes(Authentication auth) {
        return reservaService.listarSolicitudesPendientes(auth.getName());
    }

    /// Fechas ya ocupadas de un alojamiento, para pintar el calendario (endpoint publico dentro de lo autenticado).
    @GetMapping("/disponibilidad/{idAlojamiento}")
    public List<RangoFechaDTO> disponibilidad(@PathVariable Long idAlojamiento) {
        return reservaService.disponibilidad(idAlojamiento);
    }

    /// El CLIENTE solicita una reserva (queda SOLICITADA hasta que el anfitrion responda).
    @PostMapping("/solicitar")
    public Reserva solicitar(@Valid @RequestBody ReservaSolicitudDTO dto, Authentication auth) {
        return reservaService.solicitar(dto, auth.getName());
    }

    /// El ANFITRION acepta la solicitud (arranca el plazo de 48hs para pagar).
    @PatchMapping("/{id}/aceptar")
    public Reserva aceptar(@PathVariable Long id, Authentication auth) {
        return reservaService.aceptar(id, auth.getName());
    }

    /// El ANFITRION rechaza la solicitud.
    @PatchMapping("/{id}/rechazar")
    public Reserva rechazar(@PathVariable Long id, @RequestBody(required = false) ReservaRechazoDTO dto, Authentication auth) {
        String motivo = dto != null ? dto.getMotivo() : null;
        return reservaService.rechazar(id, auth.getName(), motivo);
    }

    /// El CLIENTE (o el admin) cancela antes de pagar.
    @PatchMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable Long id, Authentication auth) {
        return reservaService.cancelar(id, auth.getName());
    }

    /// El ANFITRION (o admin) cierra una estadia ya terminada.
    @PatchMapping("/finalizar/{id}")
    public Reserva finalizar(@PathVariable Long id, Authentication auth) {
        return reservaService.finalizar(id, auth.getName());
    }

    //------------------------ ESTADISTICAS RAPIDAS (tambien disponibles en /Estadisticas) ------------------------

    @GetMapping("/anfitrion/ingresos-por-mes")
    public List<IngresoMensualDTO> ingresosPorMes(Authentication auth) {
        return reservaService.ingresosPorMes(auth.getName());
    }
}
