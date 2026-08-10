package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.AlojamientoModificarDTO;
import GestionAlojamiento.DTO.AlojamientoRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Model.Enums.TipoAlojamiento;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.AlojamientoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/// Service unico para los 3 tipos de alojamiento (reemplaza a CasaService,
/// DepartamentoService y HotelService, que triplicaban la misma logica).
/// IMPORTANTE: no depende de GalleryService a proposito -- GalleryService
/// depende de este Service, asi que crear la Gallery al dar de alta un
/// alojamiento lo orquesta el Controller (ver AlojamientoController.registrar),
/// no este Service, para evitar una dependencia circular.
@Service
@RequiredArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final DireccionService direccionService;
    private final UsuarioService usuarioService;
    private final AmenityService amenityService;

    //---------------------------------------- LISTAR ----------------------------------------

    /// Devuelve TODOS los alojamientos (cualquier tipo, activos o no). Uso administrativo/interno.
    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
    }

    /// Alojamientos de un anfitrion (cualquier tipo), para "Mis alojamientos".
    public List<Alojamiento> listarPorAnfitrion(String correoAnfitrion) {
        return alojamientoRepository.findByAnfitrionEmail(correoAnfitrion);
    }

    /// Listado publico estilo Airbnb: mezcla los 3 tipos, solo activos y publicados,
    /// con filtros opcionales (todos pueden venir null y se ignoran).
    public List<Alojamiento> buscarPublico(String ciudad, Integer capacidadMinima, BigDecimal precioMin, BigDecimal precioMax, String tipo) {
        TipoAlojamiento tipoEnum = mapearTipo(tipo);
        return alojamientoRepository.buscar(ciudad, capacidadMinima, precioMin, precioMax, tipoEnum);
    }

    private TipoAlojamiento mapearTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return null;
        }
        try {
            return TipoAlojamiento.valueOf(tipo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParametroInvalidoException("Tipo de alojamiento invalido: " + tipo);
        }
    }

    /// Devuelve la lista de ids de alojamientos del anfitrion.
    public List<Long> obtenerIdsPorAnfitrion(Long anfitrionId) {
        return alojamientoRepository.findByAnfitrionId(anfitrionId)
                .stream()
                .map(Alojamiento::getId)
                .collect(Collectors.toList());
    }

    public Alojamiento obtenerPorId(Long id) {
        return alojamientoRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, alojamiento no encontrado en la base de datos."));
    }

    //---------------------------------------- CREAR ----------------------------------------

    @Transactional
    public Alojamiento crear(AlojamientoRegistroDTO dto) {

        validarCamposPorTipo(dto.getTipo(), dto.getPiso(), dto.getEstrellas());

        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setTipo(dto.getTipo());
        alojamiento.setTitulo(dto.getTitulo());
        alojamiento.setDescripcion(dto.getDescripcion());
        alojamiento.setPrecioNoche(dto.getPrecioNoche());
        alojamiento.setCapacidad(dto.getCapacidad());
        alojamiento.setCantAmbientes(dto.getCantAmbientes());
        alojamiento.setCantHabitaciones(dto.getCantHabitaciones());
        alojamiento.setCantCamas(dto.getCantCamas());
        alojamiento.setCantBanios(dto.getCantBanios());
        alojamiento.setPiso(dto.getTipo() == TipoAlojamiento.DEPARTAMENTO ? dto.getPiso() : null);
        alojamiento.setEstrellas(dto.getTipo() == TipoAlojamiento.HOTEL ? dto.getEstrellas() : null);
        alojamiento.setAmenities(amenityService.resolver(dto.getAmenityIds()));
        alojamiento.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()));
        alojamiento.setActivo(true);
        alojamiento.setPublicado(false);

        alojamiento.setDireccion(new Direccion(
                null,
                dto.getPais().toLowerCase(),
                dto.getProvincia().toLowerCase(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle().toLowerCase(),
                dto.getAltura()
        ));

        return alojamientoRepository.save(alojamiento);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------

    @Transactional
    public Alojamiento modificar(AlojamientoModificarDTO dto, String emailSolicitante) {

        Alojamiento alojamiento = obtenerPorId(dto.getId());
        validarPropietarioOAdmin(alojamiento, emailSolicitante);

        if (dto.getTitulo() != null) alojamiento.setTitulo(dto.getTitulo());
        if (dto.getDescripcion() != null) alojamiento.setDescripcion(dto.getDescripcion());
        if (dto.getPrecioNoche() != null) alojamiento.setPrecioNoche(dto.getPrecioNoche());
        if (dto.getCapacidad() != null) alojamiento.setCapacidad(dto.getCapacidad());
        if (dto.getCantAmbientes() != null) alojamiento.setCantAmbientes(dto.getCantAmbientes());
        if (dto.getCantHabitaciones() != null) alojamiento.setCantHabitaciones(dto.getCantHabitaciones());
        if (dto.getCantCamas() != null) alojamiento.setCantCamas(dto.getCantCamas());
        if (dto.getCantBanios() != null) alojamiento.setCantBanios(dto.getCantBanios());

        if (alojamiento.getTipo() == TipoAlojamiento.DEPARTAMENTO && dto.getPiso() != null) {
            alojamiento.setPiso(dto.getPiso());
        }
        if (alojamiento.getTipo() == TipoAlojamiento.HOTEL && dto.getEstrellas() != null) {
            alojamiento.setEstrellas(dto.getEstrellas());
        }

        if (dto.getAmenityIds() != null) {
            alojamiento.setAmenities(amenityService.resolver(dto.getAmenityIds()));
        }

        if (dto.getAnfitrionId() != null) {
            alojamiento.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getAnfitrionId()));
        }
        if (dto.getActivo() != null) {
            alojamiento.setActivo(dto.getActivo());
        }

        Direccion cambiosDireccion = new Direccion();
        cambiosDireccion.setPais(dto.getPais());
        cambiosDireccion.setProvincia(dto.getProvincia());
        cambiosDireccion.setCodigoPostal(dto.getCodigoPostal());
        cambiosDireccion.setCiudad(dto.getCiudad());
        cambiosDireccion.setCalle(dto.getCalle());
        cambiosDireccion.setAltura(dto.getAltura());
        alojamiento.setDireccion(direccionService.modificarObjeto(alojamiento.getDireccion(), cambiosDireccion));

        return alojamientoRepository.save(alojamiento);
    }

    //---------------------------------------- BAJA / REACTIVACION ----------------------------------------

    /// Baja logica: la usa el dueno o el admin. No borra reservas ni reviews (se preservan para historial/estadisticas).
    @Transactional
    public void desactivar(Long id, String emailSolicitante) {
        Alojamiento alojamiento = obtenerPorId(id);
        validarPropietarioOAdmin(alojamiento, emailSolicitante);
        alojamiento.setActivo(false);
        alojamiento.setPublicado(false);
        alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public void reactivar(Long id, String emailSolicitante) {
        Alojamiento alojamiento = obtenerPorId(id);
        validarPropietarioOAdmin(alojamiento, emailSolicitante);
        alojamiento.setActivo(true);
        alojamientoRepository.save(alojamiento);
    }

    /// Cambia el flag de publicado. Lo invoca GalleryService una vez que verifico
    /// que el alojamiento cumple el minimo de fotos requerido (y la propia ownership).
    @Transactional
    public void actualizarPublicado(Long id, boolean publicado) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setPublicado(publicado);
        alojamientoRepository.save(alojamiento);
    }

    //---------------------------------------- PRIVADOS ----------------------------------------

    /// piso es obligatorio (y solo tiene sentido) para DEPARTAMENTO; estrellas para HOTEL.
    private void validarCamposPorTipo(TipoAlojamiento tipo, Integer piso, Integer estrellas) {
        if (tipo == TipoAlojamiento.DEPARTAMENTO && piso == null) {
            throw new ParametroInvalidoException("El piso es obligatorio para un departamento.");
        }
        if (tipo == TipoAlojamiento.HOTEL && estrellas == null) {
            throw new ParametroInvalidoException("La cantidad de estrellas es obligatoria para un hotel.");
        }
    }

    public void validarPropietarioOAdmin(Alojamiento alojamiento, String emailSolicitante) {
        Usuario solicitante = usuarioService.obtenerPorEmail(emailSolicitante);
        boolean esAdmin = solicitante.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
        boolean esDueno = alojamiento.getAnfitrion().getId().equals(solicitante.getId());

        if (!esAdmin && !esDueno) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este alojamiento no te pertenece");
        }
    }
}
