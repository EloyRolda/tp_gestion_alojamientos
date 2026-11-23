package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.AlojamientoModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.AlojamientoRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.DireccionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MotivoReporteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Direccion;
import com.eloyroldan.tp_final_alojamiento.backend.Model.TipoAlojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.AlojamientoRepository;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria.GaleriaService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Servicio.ServicioxAlojamientoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// AlojamientoService es el "hub": lo llaman DireccionService, TipoAlojamientoService
/// y ReservaService para validaciones cruzadas. Para evitar dependencia circular en la
/// construccion de beans, esas referencias de vuelta se inyectan con @Lazy en el otro lado.
@Service
@RequiredArgsConstructor
@Slf4j
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final DireccionService direccionService;
    private final TipoAlojamientoService tipoAlojamientoService;
    private final UsuarioService usuarioService;
    private final GaleriaService galeriaService;
    private final ServicioxAlojamientoService servicioxAlojamientoService;
    private final EmailService emailService;

    @Lazy
    private final ReservaService reservaService;

    @Lazy
    private final FavoritoService favoritoService;

    //---------------------------------------- LISTAR (para controllers, como DTO) ----------------------------------------

    @Transactional
    public List<AlojamientoResponseDTO> listarTodosDTO() {
        // JOIN FETCH: evita 1+3N consultas (N = cantidad de alojamientos) por las
        // relaciones LAZY que necesita AlojamientoResponseDTO.desde().
        return conImagenPrincipal(alojamientoRepository.findActivosConDetalle());
    }

    @Transactional
    public List<AlojamientoResponseDTO> listarPorAnfitrionDTO(Long idAnfitrion) {
        return conImagenPrincipal(alojamientoRepository.findByAnfitrionIdConDetalle(idAnfitrion));
    }

    /// DTOs de un conjunto de alojamientos, en una sola consulta y respetando el
    /// orden de la lista de ids recibida (lo usa FavoritoService: del favorito mas
    /// reciente al mas viejo). Los ids que ya no existen se saltean.
    @Transactional
    public List<AlojamientoResponseDTO> listarPorIdsDTO(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        Map<Long, Alojamiento> porId = new HashMap<>();
        for (Alojamiento alojamiento : alojamientoRepository.findByIdsConDetalle(ids)) {
            porId.put(alojamiento.getIdAlojamiento(), alojamiento);
        }
        List<Alojamiento> ordenados = ids.stream()
                .map(porId::get)
                .filter(Objects::nonNull)
                .toList();
        return conImagenPrincipal(ordenados);
    }

    @Transactional
    public AlojamientoResponseDTO obtenerDTOPorId(Long id) {
        Alojamiento alojamiento = obtenerPorId(id);
        String imagen = galeriaService.imagenPrincipalPorAlojamiento(List.of(id)).get(id);
        return AlojamientoResponseDTO.desde(alojamiento, imagen);
    }

    /// Arma los DTOs del listado con la foto principal de cada alojamiento, resuelta en
    /// una sola consulta agrupada (no una por fila).
    private List<AlojamientoResponseDTO> conImagenPrincipal(List<Alojamiento> alojamientos) {
        Map<Long, String> imagenes = galeriaService.imagenPrincipalPorAlojamiento(
                alojamientos.stream().map(Alojamiento::getIdAlojamiento).toList());
        return alojamientos.stream()
                .map(a -> AlojamientoResponseDTO.desde(a, imagenes.get(a.getIdAlojamiento())))
                .toList();
    }

    @Transactional
    public AlojamientoResponseDTO crearDTO(AlojamientoRegistroDTO dto, Usuario usuarioAutenticado) {
        return AlojamientoResponseDTO.desde(crear(dto, usuarioAutenticado));
    }

    @Transactional
    public AlojamientoResponseDTO modificarDTO(AlojamientoModificarDTO dto) {
        return AlojamientoResponseDTO.desde(modificar(dto));
    }

    /// id -> titulo de cada alojamiento, en una sola consulta. Lo usa la
    /// moderacion de reportes para no resolver el nombre del objetivo fila por fila.
    public Map<Long, String> titulosPorIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        List<Long> lista = new ArrayList<>(ids);
        Map<Long, String> resultado = new HashMap<>();
        for (Object[] fila : alojamientoRepository.titulosPorIds(lista)) {
            resultado.put((Long) fila[0], (String) fila[1]);
        }
        return resultado;
    }

    //---------------------------------------- LISTAR ----------------------------------------

    public List<Alojamiento> listarPorAnfitrion(Long idAnfitrion) {
        return alojamientoRepository.findByAnfitrion_Id(idAnfitrion);
    }

    public List<Alojamiento> listarPorTipoAlojamiento(Long idTipoAlojamiento) {
        return alojamientoRepository.findByTipoAlojamiento_IdTipoAlojamiento(idTipoAlojamiento);
    }

    public Alojamiento obtenerPorId(Long id) {
        return alojamientoRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, alojamiento no encontrado en la base de datos."));
    }

    /// Version con bloqueo pesimista de obtenerPorId — usarla especificamente al
    /// crear/reprogramar una Reserva, para cerrar la ventana de condicion de
    /// carrera del chequeo de solapamiento de fechas. Debe llamarse dentro de un
    /// metodo @Transactional (el lock se libera cuando esa transaccion termina).
    public Alojamiento obtenerPorIdConLock(Long id) {
        return alojamientoRepository.findByIdConLock(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, alojamiento no encontrado en la base de datos."));
    }

    /// Resuelve el id del anfitrion dueño dentro de una transaccion (evita
    /// LazyInitializationException si se llamara fuera, con open-in-view en false).
    @Transactional
    public Long obtenerIdAnfitrion(Long idAlojamiento) {
        return obtenerPorId(idAlojamiento).getAnfitrion().getId();
    }

    //---------------------------------------- USO INTERNO (otros services) ----------------------------------------

    /// Usado por DireccionService antes de borrar una direccion (puede ser compartida).
    public boolean existeConDireccion(Long idDireccion) {
        return alojamientoRepository.existsByDireccion_IdDireccion(idDireccion);
    }

    /// Usado por TipoAlojamientoService al fusionar un tipo duplicado dentro de otro.
    @Transactional
    public int reasignarTipoAlojamiento(Long idTipoOrigen, TipoAlojamiento destino) {
        List<Alojamiento> afectados = listarPorTipoAlojamiento(idTipoOrigen);
        for (Alojamiento alojamiento : afectados) {
            alojamiento.setTipoAlojamiento(destino);
        }
        alojamientoRepository.saveAll(afectados);
        return afectados.size();
    }

    /// idTipoAlojamiento -> cantidad de alojamientos que lo usan (los que no
    /// aparecen no tienen ninguno).
    public Map<Long, Long> cantidadPorTipo() {
        Map<Long, Long> resultado = new HashMap<>();
        for (Object[] fila : alojamientoRepository.cantidadPorTipoRaw()) {
            resultado.put((Long) fila[0], (Long) fila[1]);
        }
        return resultado;
    }

    //---------------------------------------- CREAR ----------------------------------------

    /// usuarioAutenticado es quien hace el request. Si es ANFITRION, publica a su propio
    /// nombre (se ignora dto.idAnfitrion); si es ADMIN, debe indicar dto.idAnfitrion.
    @Transactional
    public Alojamiento crear(AlojamientoRegistroDTO dto, Usuario usuarioAutenticado) {

        Usuario anfitrion = resolverAnfitrion(dto.getIdAnfitrion(), usuarioAutenticado);
        Direccion direccion = resolverDireccion(dto.getIdDireccion(), dto.getDireccion());

        Alojamiento alojamiento = Alojamiento.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion() != null ? dto.getDescripcion() : "Excelente alojamiento")
                .precioNoche(dto.getPrecioNoche())
                .capacidad(dto.getCapacidad())
                .cantAmbientes(dto.getCantAmbientes())
                .cantHabitaciones(dto.getCantHabitaciones())
                .cantBanios(dto.getCantBanios())
                .activo(true)
                .anfitrion(anfitrion)
                .direccion(direccion)
                .tipoAlojamiento(tipoAlojamientoService.obtenerOcrear(dto.getNombreTipoAlojamiento()))
                .build();

        alojamiento = alojamientoRepository.save(alojamiento);

        servicioxAlojamientoService.asignarServicios(alojamiento, dto.getNombresServicios());
        galeriaService.crearParaAlojamiento(alojamiento);

        return alojamiento;
    }

    private Usuario resolverAnfitrion(Long idAnfitrionDto, Usuario usuarioAutenticado) {
        if (usuarioAutenticado.getTipo() == TIPOUSUARIO.ANFITRION) {
            return usuarioAutenticado;
        }
        // ADMIN publicando a nombre de otro anfitrion
        if (idAnfitrionDto == null) {
            throw new ParametroInvalidoException("Debe indicar idAnfitrion.");
        }
        return usuarioService.obtenerAnfitrionPorId(idAnfitrionDto);
    }

    private Direccion resolverDireccion(Long idDireccionDto, DireccionDTO direccionDto) {
        if (idDireccionDto != null) {
            return direccionService.obtenerPorId(idDireccionDto);
        }
        if (direccionDto == null) {
            throw new ParametroInvalidoException("Debe indicar idDireccion o los datos de una direccion nueva.");
        }
        return direccionService.crear(direccionDto);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------

    @Transactional
    public Alojamiento modificar(AlojamientoModificarDTO dto) {
        Alojamiento alojamiento = obtenerPorId(dto.getIdAlojamiento());

        // Regla 5: si el alojamiento fue ocultado por moderación, el anfitrión no
        // puede reactivarlo tocando el propio formulario de edición.
        if (dto.getActivo() != null && dto.getActivo() && alojamiento.getMotivoOcultamiento() != null) {
            String etiqueta = MotivoReporteDTO.desde(alojamiento.getMotivoOcultamiento()).getEtiqueta();
            throw new ParametroInvalidoException("Esta publicación fue ocultada por moderación (" + etiqueta + "). Escribinos para revisarla.");
        }

        if (dto.getTitulo() != null) {
            alojamiento.setTitulo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null) {
            alojamiento.setDescripcion(dto.getDescripcion());
        }
        if (dto.getPrecioNoche() != null) {
            alojamiento.setPrecioNoche(dto.getPrecioNoche());
        }
        if (dto.getCapacidad() != null) {
            alojamiento.setCapacidad(dto.getCapacidad());
        }
        if (dto.getCantAmbientes() != null) {
            alojamiento.setCantAmbientes(dto.getCantAmbientes());
        }
        if (dto.getCantHabitaciones() != null) {
            alojamiento.setCantHabitaciones(dto.getCantHabitaciones());
        }
        if (dto.getCantBanios() != null) {
            alojamiento.setCantBanios(dto.getCantBanios());
        }
        if (dto.getActivo() != null) {
            alojamiento.setActivo(dto.getActivo());
        }
        if (dto.getNombreTipoAlojamiento() != null) {
            alojamiento.setTipoAlojamiento(tipoAlojamientoService.obtenerOcrear(dto.getNombreTipoAlojamiento()));
        }
        if (dto.getIdDireccion() != null) {
            alojamiento.setDireccion(direccionService.obtenerPorId(dto.getIdDireccion()));
        }
        if (dto.getLatitud() != null || dto.getLongitud() != null) {
            alojamiento.setDireccion(direccionService.actualizarCoordenadas(
                    alojamiento.getDireccion().getIdDireccion(), dto.getLatitud(), dto.getLongitud()));
        }

        return alojamientoRepository.save(alojamiento);
    }

    /// Moderacion (SCRUM-79): oculta un alojamiento guardando motivo, detalle opcional y
    /// fecha, y manda el correo al anfitrión. Si el correo falla, la ocultación queda hecha.
    @Transactional
    public void ocultarPorModeracion(Long id, MOTIVOREPORTE motivo, String detalle) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setActivo(false);
        alojamiento.setMotivoOcultamiento(motivo);
        // Un detalle en blanco se guarda como null (el textarea puede venir con espacios).
        detalle = (detalle == null || detalle.isBlank()) ? null : detalle.trim();
        alojamiento.setDetalleOcultamiento(detalle);
        alojamiento.setFechaOcultamiento(LocalDateTime.now());
        alojamientoRepository.save(alojamiento);

        try {
            Usuario anfitrion = alojamiento.getAnfitrion();
            String nombreCompleto = anfitrion.getNombre() + " " + anfitrion.getApellido();
            String titulo = alojamiento.getTitulo();
            String etiqueta = MotivoReporteDTO.desde(motivo).getEtiqueta();

            String html = "Hola " + HtmlUtils.htmlEscape(nombreCompleto)
                    + ", tu publicación «" + HtmlUtils.htmlEscape(titulo) + "» fue ocultada. Motivo: "
                    + HtmlUtils.htmlEscape(etiqueta) + ".";
            if (detalle != null && !detalle.isBlank()) {
                html += " Detalle: " + HtmlUtils.htmlEscape(detalle) + ".";
            }

            emailService.enviar(anfitrion.getEmail(), nombreCompleto, "Tu publicación «" + titulo + "» fue ocultada", html);
        } catch (RuntimeException e) {
            log.warn("No se pudo enviar correo de ocultamiento para alojamiento {}: {}", id, e.getMessage());
        }
    }

    /// Vuelve a publicar un alojamiento que había sido ocultado por moderación.
    /// Limpia los campos de ocultamiento y envía el correo de nueva publicación.
    @Transactional
    public void republicar(Long id) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setActivo(true);
        alojamiento.setMotivoOcultamiento(null);
        alojamiento.setDetalleOcultamiento(null);
        alojamiento.setFechaOcultamiento(null);
        alojamientoRepository.save(alojamiento);

        try {
            Usuario anfitrion = alojamiento.getAnfitrion();
            String nombreCompleto = anfitrion.getNombre() + " " + anfitrion.getApellido();
            String titulo = alojamiento.getTitulo();

            String html = "Hola " + HtmlUtils.htmlEscape(nombreCompleto)
                    + ", tu publicación «" + HtmlUtils.htmlEscape(titulo) + "» volvió a estar visible.";

            emailService.enviar(anfitrion.getEmail(), nombreCompleto, "Tu publicación «" + titulo + "» volvió a estar visible", html);
        } catch (RuntimeException e) {
            log.warn("No se pudo enviar correo de republicación para alojamiento {}: {}", id, e.getMessage());
        }
    }

    /// Cambio directo de activo, usado anteriormente por la moderación SCRUM-79.
    /// Se mantiene por si otras partes del sistema todavía lo usan, aunque ahora
    /// la lógica de moderación pasa por ocultarPorModeracion/republicar.
    @Transactional
    public void cambiarActivo(Long id, boolean activo) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setActivo(activo);
        alojamientoRepository.save(alojamiento);
    }

    //---------------------------------------- BORRAR ----------------------------------------

    /// No borra la Direccion (puede ser compartida por otros alojamientos). No permite
    /// borrar si hay reservas SOLICITADA/CONFIRMADA (primero hay que cancelarlas o
    /// esperar a que se resuelvan) — evita borrarle una estadia a un cliente sin aviso.
    /// Limpia tambien los favoritos: la FK de favorito no tiene cascada y el alojamiento
    /// puede estar guardado por varios clientes.
    @Transactional
    public void borrarPorId(Long id) {
        Alojamiento alojamiento = obtenerPorId(id);
        if (reservaService.tieneReservasActivas(alojamiento.getIdAlojamiento())) {
            throw new ParametroInvalidoException(
                    "No se puede borrar: hay reservas solicitadas o confirmadas sobre este alojamiento.");
        }
        reservaService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        favoritoService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        servicioxAlojamientoService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        galeriaService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        alojamientoRepository.delete(alojamiento);
    }
}
