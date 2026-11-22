/// Reportes de usuarios sobre publicaciones o personas (RF44). Aca viven todas
/// las reglas: el detalle obligatorio con «Otro», que el objetivo exista y no sea
/// propio, que solo se reporte a quien compartio una reserva, que el motivo
/// corresponda al objetivo y que no haya ya un reporte pendiente igual.
package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReporteRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MotivoReporteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReporteAdminDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReporteResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reporte;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.ReporteRepository;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Moderacion.AccionModeracionStrategy;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final int MAX_DETALLE = 500;
    private static final String CATEGORIA_CLIENTE = "CLIENTE";
    private static final String CATEGORIA_ANFITRION = "ANFITRION";
    private static final String CATEGORIA_ALOJAMIENTO = "ALOJAMIENTO";

    /// Solo repositorios propios: los datos de alojamientos, usuarios y reservas
    /// se piden a sus Services (nunca a sus repositorios).
    private final ReporteRepository reporteRepository;
    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;
    private final ReservaService reservaService;

    /// Estrategias de moderacion inyectadas por Spring y ya indexadas por su
    /// ACCIONMODERACION en un Map, igual que PagoService con las politicas.
    private final List<AccionModeracionStrategy> estrategias;
    private Map<ACCIONMODERACION, AccionModeracionStrategy> estrategiasPorAccion;

    @PostConstruct
    void iniciar() {
        estrategiasPorAccion = new HashMap<>();
        for (AccionModeracionStrategy estrategia : estrategias) {
            estrategiasPorAccion.put(estrategia.accion(), estrategia);
        }
    }

    //------------------------ CREAR ------------------------

    @Transactional
    public Reporte crear(ReporteRegistroDTO dto, Usuario autor) {
        validarDetalle(dto);
        validarObjetivoYmotivo(dto, autor);
        validarSinPendienteDuplicado(autor.getId(), dto);

        Reporte reporte = Reporte.builder()
                .autor(autor)
                .tipo(dto.getTipo())
                .idObjetivo(dto.getIdObjetivo())
                .motivo(dto.getMotivo())
                .detalle(normalizarDetalle(dto.getDetalle()))
                .estado(ESTADOREPORTE.PENDIENTE)
                .build();

        return reporteRepository.save(reporte);
    }

    @Transactional
    public ReporteResponseDTO crearDTO(ReporteRegistroDTO dto, Usuario autor) {
        return ReporteResponseDTO.desde(crear(dto, autor));
    }

    /// Catalogo fijo de motivos con las categorias a las que aplica cada uno.
    public List<MotivoReporteDTO> listarMotivos() {
        return Arrays.stream(MOTIVOREPORTE.values())
                .map(MotivoReporteDTO::desde)
                .toList();
    }

    //------------------------ MODERACION ------------------------

    /// Listado para la pantalla del administrador: reportes ordenados «pendientes
    /// primero» y, dentro de cada estado, del mas nuevo al mas viejo. Los nombres
    /// de objetivos y autores se resuelven con consultas agrupadas, sin N+1.
    @Transactional
    public List<ReporteAdminDTO> listarAdmin(ESTADOREPORTE estado) {
        List<Reporte> reportes = reporteRepository.findParaAdmin(estado);

        if (reportes.isEmpty()) {
            return List.of();
        }

        Set<Long> idsAlojamientos = new HashSet<>();
        Set<Long> idsUsuarios = new HashSet<>();

        for (Reporte reporte : reportes) {
            if (reporte.getTipo() == TIPOREPORTE.ALOJAMIENTO) {
                idsAlojamientos.add(reporte.getIdObjetivo());
            } else {
                idsUsuarios.add(reporte.getIdObjetivo());
            }
        }

        Map<Long, String> titulosAlojamientos = alojamientoService.titulosPorIds(idsAlojamientos);
        Map<Long, String> nombresUsuarios = usuarioService.nombresPorIds(idsUsuarios);

        return reportes.stream()
                .map(r -> toDTO(r, titulosAlojamientos, nombresUsuarios))
                .toList();
    }

    /// Resuelve un reporte pendiente con la accion elegida. Si la accion corresponde
    /// a ocultar/desactivar, tambien cierra los demas pendientes del mismo objetivo.
    @Transactional
    public ReporteAdminDTO resolver(Long idReporte, ACCIONMODERACION accion) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new IdNoEncontradoException("Reporte no encontrado."));

        if (reporte.getEstado() != ESTADOREPORTE.PENDIENTE) {
            throw new ParametroInvalidoException("Este reporte ya fue resuelto.");
        }

        AccionModeracionStrategy estrategia = estrategiasPorAccion.get(accion);
        if (estrategia == null) {
            throw new ParametroInvalidoException("Acción de moderación no soportada.");
        }

        estrategia.aplicar(reporte);

        LocalDateTime fechaResolucion = LocalDateTime.now();
        reporte.setEstado(ESTADOREPORTE.RESUELTO);
        reporte.setAccionTomada(accion);
        reporte.setFechaResolucion(fechaResolucion);
        reporte = reporteRepository.save(reporte);

        if (accion == ACCIONMODERACION.OCULTAR_ALOJAMIENTO
                || accion == ACCIONMODERACION.DESACTIVAR_USUARIO) {
            List<Reporte> otrosPendientes = reporteRepository.findByTipoAndIdObjetivoAndEstado(
                    reporte.getTipo(), reporte.getIdObjetivo(), ESTADOREPORTE.PENDIENTE);

            for (Reporte otro : otrosPendientes) {
                if (otro.getIdReporte().equals(reporte.getIdReporte())) {
                    continue;
                }
                otro.setEstado(ESTADOREPORTE.RESUELTO);
                otro.setAccionTomada(accion);
                otro.setFechaResolucion(fechaResolucion);
                reporteRepository.save(otro);
            }
        }

        Map<Long, String> titulosAlojamientos = Map.of();
        Map<Long, String> nombresUsuarios = Map.of();

        if (reporte.getTipo() == TIPOREPORTE.ALOJAMIENTO) {
            titulosAlojamientos = alojamientoService.titulosPorIds(List.of(reporte.getIdObjetivo()));
        } else {
            nombresUsuarios = usuarioService.nombresPorIds(List.of(reporte.getIdObjetivo()));
        }

        return toDTO(reporte, titulosAlojamientos, nombresUsuarios);
    }

    //------------------------ REGLAS ------------------------

    /// Regla 2: con «Otro» el detalle es obligatorio; en el resto es opcional.
    /// El maximo de 500 ya lo valida el DTO, esto lo cubre igual por si llega
    /// un reporte armado desde otro lado.
    private void validarDetalle(ReporteRegistroDTO dto) {
        String detalle = normalizarDetalle(dto.getDetalle());
        if (dto.getMotivo() == MOTIVOREPORTE.OTRO && detalle == null) {
            throw new ParametroInvalidoException("Contanos qué pasó: el detalle es obligatorio cuando elegís «Otro».");
        }
        if (detalle != null && detalle.length() > MAX_DETALLE) {
            throw new ParametroInvalidoException("El detalle no puede superar los " + MAX_DETALLE + " caracteres.");
        }
    }

    /// Reglas 3, 4 y 6: quien puede reportar a quien y con que motivo.
    private void validarObjetivoYmotivo(ReporteRegistroDTO dto, Usuario autor) {
        if (dto.getTipo() == TIPOREPORTE.ALOJAMIENTO) {
            validarReporteDeAlojamiento(dto, autor);
        } else {
            validarReporteDeUsuario(dto, autor);
        }
    }

    /// Regla 3: lo puede reportar cualquier usuario con sesion, salvo su propio anfitrion.
    private void validarReporteDeAlojamiento(ReporteRegistroDTO dto, Usuario autor) {
        Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdObjetivo());
        if (alojamiento.getAnfitrion().getId().equals(autor.getId())) {
            throw new ParametroInvalidoException("No podés reportar tu propio alojamiento.");
        }
        if (!dto.getMotivo().aplicaA(CATEGORIA_ALOJAMIENTO)) {
            throw new ParametroInvalidoException("El motivo elegido no corresponde a un alojamiento.");
        }
    }

    /// Regla 4: solo lo puede reportar quien compartio al menos una reserva con
    /// el (como cliente o como anfitrion). Nadie se reporta a si mismo y a un
    /// administrador no se lo puede reportar. Regla 6: el motivo tiene que
    /// corresponder al rol del reportado (cliente o anfitrion).
    private void validarReporteDeUsuario(ReporteRegistroDTO dto, Usuario autor) {
        if (autor.getId().equals(dto.getIdObjetivo())) {
            throw new ParametroInvalidoException("No podés reportarte a vos mismo.");
        }

        Usuario reportado = usuarioService.obtenerPorId(dto.getIdObjetivo());

        if (reportado.getTipo() == TIPOUSUARIO.ADMIN) {
            throw new ParametroInvalidoException("A un administrador no se lo puede reportar.");
        }
        if (!reservaService.compartieronReserva(autor.getId(), reportado.getId())) {
            throw new ParametroInvalidoException("Solo podés reportar a alguien con quien hayas compartido una reserva.");
        }

        String categoria = reportado.getTipo() == TIPOUSUARIO.ANFITRION ? CATEGORIA_ANFITRION : CATEGORIA_CLIENTE;
        if (!dto.getMotivo().aplicaA(categoria)) {
            throw new ParametroInvalidoException("El motivo elegido no corresponde a la persona que estás reportando.");
        }
    }

    /// Regla 5: un solo reporte pendiente por autor y objetivo.
    private void validarSinPendienteDuplicado(Long idAutor, ReporteRegistroDTO dto) {
        boolean yaExiste = reporteRepository.existsByAutor_IdAndTipoAndIdObjetivoAndEstado(
                idAutor, dto.getTipo(), dto.getIdObjetivo(), ESTADOREPORTE.PENDIENTE);
        if (yaExiste) {
            throw new ParametroInvalidoException("Ya tenés un reporte pendiente sobre esto. Lo estamos revisando.");
        }
    }

    /// Un detalle en blanco se guarda como null (el textarea siempre puede venir
    /// con espacios y no queremos " " en la base).
    private String normalizarDetalle(String detalle) {
        if (detalle == null) {
            return null;
        }
        String limpio = detalle.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    //------------------------ AUXILIAR ------------------------

    /// Convierte un Reporte en el DTO del listado de moderacion. Los nombres de
    /// los objetivos ya llegan resueltos por las consultas agrupadas de los services.
    private ReporteAdminDTO toDTO(Reporte reporte,
                                  Map<Long, String> titulosAlojamientos,
                                  Map<Long, String> nombresUsuarios) {
        String nombreObjetivo;
        if (reporte.getTipo() == TIPOREPORTE.ALOJAMIENTO) {
            nombreObjetivo = titulosAlojamientos.get(reporte.getIdObjetivo());
        } else {
            nombreObjetivo = nombresUsuarios.get(reporte.getIdObjetivo());
        }

        Usuario autor = reporte.getAutor();
        String nombreAutor = autor != null ? autor.getNombre() + " " + autor.getApellido() : "";
        Long idAutor = autor != null ? autor.getId() : null;

        return new ReporteAdminDTO(
                reporte.getIdReporte(),
                reporte.getTipo(),
                reporte.getIdObjetivo(),
                nombreObjetivo,
                idAutor,
                nombreAutor,
                reporte.getMotivo().name(),
                MotivoReporteDTO.desde(reporte.getMotivo()).getEtiqueta(),
                reporte.getDetalle(),
                reporte.getEstado(),
                reporte.getAccionTomada(),
                reporte.getFechaCreacion(),
                reporte.getFechaResolucion()
        );
    }
}
