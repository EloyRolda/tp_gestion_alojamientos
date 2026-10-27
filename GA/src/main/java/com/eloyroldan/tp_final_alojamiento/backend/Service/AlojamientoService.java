package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.AlojamientoModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.AlojamientoRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.DireccionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoResponseDTO;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/// AlojamientoService es el "hub": lo llaman DireccionService, TipoAlojamientoService
/// y ReservaService para validaciones cruzadas. Para evitar dependencia circular en la
/// construccion de beans, esas referencias de vuelta se inyectan con @Lazy en el otro lado.
@Service
@RequiredArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final DireccionService direccionService;
    private final TipoAlojamientoService tipoAlojamientoService;
    private final UsuarioService usuarioService;
    private final GaleriaService galeriaService;
    private final ServicioxAlojamientoService servicioxAlojamientoService;

    @Lazy
    private final ReservaService reservaService;

    //---------------------------------------- LISTAR (para controllers, como DTO) ----------------------------------------

    @Transactional
    public List<AlojamientoResponseDTO> listarTodosDTO() {
        // JOIN FETCH: evita 1+3N consultas (N = cantidad de alojamientos) por las
        // relaciones LAZY que necesita AlojamientoResponseDTO.desde().
        return alojamientoRepository.findActivosConDetalle().stream().map(AlojamientoResponseDTO::desde).toList();
    }

    @Transactional
    public List<AlojamientoResponseDTO> listarPorAnfitrionDTO(Long idAnfitrion) {
        return alojamientoRepository.findByAnfitrionIdConDetalle(idAnfitrion).stream().map(AlojamientoResponseDTO::desde).toList();
    }

    @Transactional
    public AlojamientoResponseDTO obtenerDTOPorId(Long id) {
        return AlojamientoResponseDTO.desde(obtenerPorId(id));
    }

    @Transactional
    public AlojamientoResponseDTO crearDTO(AlojamientoRegistroDTO dto, Usuario usuarioAutenticado) {
        return AlojamientoResponseDTO.desde(crear(dto, usuarioAutenticado));
    }

    @Transactional
    public AlojamientoResponseDTO modificarDTO(AlojamientoModificarDTO dto) {
        return AlojamientoResponseDTO.desde(modificar(dto));
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
    public void reasignarTipoAlojamiento(Long idTipoOrigen, TipoAlojamiento destino) {
        List<Alojamiento> afectados = listarPorTipoAlojamiento(idTipoOrigen);
        for (Alojamiento alojamiento : afectados) {
            alojamiento.setTipoAlojamiento(destino);
        }
        alojamientoRepository.saveAll(afectados);
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

        return alojamientoRepository.save(alojamiento);
    }

    //---------------------------------------- BORRAR ----------------------------------------

    /// No borra la Direccion (puede ser compartida por otros alojamientos). No permite
    /// borrar si hay reservas SOLICITADA/CONFIRMADA (primero hay que cancelarlas o
    /// esperar a que se resuelvan) — evita borrarle una estadia a un cliente sin aviso.
    @Transactional
    public void borrarPorId(Long id) {
        Alojamiento alojamiento = obtenerPorId(id);
        if (reservaService.tieneReservasActivas(alojamiento.getIdAlojamiento())) {
            throw new ParametroInvalidoException(
                    "No se puede borrar: hay reservas solicitadas o confirmadas sobre este alojamiento.");
        }
        reservaService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        servicioxAlojamientoService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        galeriaService.borrarPorAlojamientoId(alojamiento.getIdAlojamiento());
        alojamientoRepository.delete(alojamiento);
    }
}
