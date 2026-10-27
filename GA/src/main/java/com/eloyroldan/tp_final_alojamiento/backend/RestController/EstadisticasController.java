package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CancelacionPorMotivoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.CobroPendienteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.FlujoDineroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.IngresoMensualDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ResumenAlojamientoAnfitrionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ResumenClienteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.AlojamientoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.PagoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReservaService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReviewService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/// Solo orquesta: llama a los Services dueños de cada tabla y combina resultados.
/// Ninguna consulta SQL vive aca — todas estan en el Repository correspondiente.
///
/// En "anfitrion/**" y "cliente/**": el propio usuario siempre ve lo suyo. Un
/// ADMIN, ademas, puede pasar idAnfitrion/idCliente por query param para ver el
/// resumen de cualquier otro usuario — mismo patron que ya usamos en
/// AlojamientoRegistroDTO.idAnfitrion y ReservaRegistroDTO.idCliente.
@RestController
@RequestMapping("/Estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final PagoService pagoService;
    private final ReviewService reviewService;
    private final ReservaService reservaService;
    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;

    //================================ ADMIN ================================

    @GetMapping("/admin/flujo-dinero")
    public FlujoDineroDTO flujoDeDinero(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return pagoService.flujoDeDinero(desde, hasta);
    }

    /// criterio: ingresos | reservas | rating
    @GetMapping("/admin/top-alojamientos")
    public List<AlojamientoRankingDTO> topAlojamientos(
            @RequestParam(defaultValue = "ingresos") String criterio,
            @RequestParam(defaultValue = "10") int limite) {
        return switch (criterio) {
            case "ingresos" -> pagoService.topAlojamientosPorIngresos(limite);
            case "reservas" -> reservaService.topPorCantidadReservas(limite);
            case "rating" -> reviewService.topPorRating(limite);
            default -> throw new ParametroInvalidoException("Criterio invalido: use ingresos, reservas o rating.");
        };
    }

    @GetMapping("/admin/cancelaciones-por-motivo")
    public List<CancelacionPorMotivoDTO> cancelacionesPorMotivo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return pagoService.cancelacionesPorMotivo(desde, hasta);
    }

    @GetMapping("/admin/ingresos-mensuales")
    public List<IngresoMensualDTO> ingresosMensuales(@RequestParam int anio) {
        return pagoService.ingresosMensuales(anio);
    }

    //================================ ANFITRION ================================

    /// Un resumen por cada alojamiento propio: ingresos, cantidad de reservas y rating.
    /// idAnfitrion es opcional y solo lo puede usar un ADMIN (para ver el resumen
    /// de cualquier anfitrion); si lo manda un ANFITRION, se ignora y se usa el suyo.
    /// Optimizado para no hacer 3 consultas POR alojamiento: se piden los 3 datos
    /// (ingresos, reservas, rating) de TODOS los alojamientos de una sola vez, y
    /// se combinan en memoria — 3 consultas fijas, sin importar cuantos alojamientos tenga.
    @GetMapping("/anfitrion/resumen")
    public List<ResumenAlojamientoAnfitrionDTO> resumenAnfitrion(
            @RequestParam(required = false) Long idAnfitrion, Authentication auth) {
        Usuario anfitrion = resolverAnfitrionConsultado(idAnfitrion, auth);
        List<Alojamiento> propios = alojamientoService.listarPorAnfitrion(anfitrion.getId());
        List<Long> idsAlojamiento = propios.stream().map(Alojamiento::getIdAlojamiento).toList();

        Map<Long, BigDecimal> ingresosPorId = pagoService.ingresosPorAlojamientos(idsAlojamiento);
        Map<Long, Long> reservasPorId = reservaService.cantidadReservasPorAlojamientos(idsAlojamiento);
        Map<Long, Double> ratingPorId = reviewService.promedioPorAlojamientos(idsAlojamiento);

        return propios.stream().map(a -> new ResumenAlojamientoAnfitrionDTO(
                a.getIdAlojamiento(),
                a.getTitulo(),
                ingresosPorId.getOrDefault(a.getIdAlojamiento(), BigDecimal.ZERO),
                reservasPorId.getOrDefault(a.getIdAlojamiento(), 0L),
                ratingPorId.get(a.getIdAlojamiento())
        )).toList();
    }

    @GetMapping("/anfitrion/cobros-pendientes")
    public List<CobroPendienteDTO> cobrosPendientes(
            @RequestParam(required = false) Long idAnfitrion, Authentication auth) {
        Usuario anfitrion = resolverAnfitrionConsultado(idAnfitrion, auth);
        return pagoService.cobrosPendientesPorAnfitrion(anfitrion.getId());
    }

    /// Si quien pregunta es ADMIN y mando idAnfitrion, se consulta ESE anfitrion
    /// (validando que realmente lo sea). En cualquier otro caso, se ignora el
    /// parametro y cada uno ve lo suyo.
    private Usuario resolverAnfitrionConsultado(Long idAnfitrionParam, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        if (solicitante.getTipo() == TIPOUSUARIO.ADMIN && idAnfitrionParam != null) {
            return usuarioService.obtenerAnfitrionPorId(idAnfitrionParam);
        }
        return solicitante;
    }

    //================================ CLIENTE ================================

    /// idCliente es opcional y solo lo puede usar un ADMIN; si lo manda un
    /// CLIENTE, se ignora y se usa el suyo.
    @GetMapping("/cliente/resumen")
    public ResumenClienteDTO resumenCliente(
            @RequestParam(required = false) Long idCliente, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        Usuario cliente = (solicitante.getTipo() == TIPOUSUARIO.ADMIN && idCliente != null)
                ? usuarioService.obtenerClientePorId(idCliente)
                : solicitante;
        return pagoService.resumenGastoCliente(cliente.getId());
    }
}