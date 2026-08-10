package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Model.Gallery;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.CasaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CasaService {

    private final CasaRepository casaRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;
    private final AmenityService amenityService;
    private final GalleryService galleryService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Casa> listar() {
        return casaRepository.findAll();
    }

    public Casa obtenerPorId(Long id) {
        return casaRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el id de la casa no se encuentra en la base de datos."));
    }

    /// Recibe el correo de un anfitrion y devuelve la lista de casas que posee.
    public List<Casa> listarPorAnfitrion(String correoAnfitrion) {
        return casaRepository.findByAnfitrionEmail(correoAnfitrion);
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Casa crear(CasaRegistroDTO dto) {

        Casa casa = new Casa();
        casa.setTienePatio(dto.isTienePatio());
        casa.setTienePileta(dto.isTienePileta());
        casa.setTieneParrilla(dto.isTieneParrilla());

        mapearComun(casa, dto);
        casa.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()));
        casa.setActivo(true);

        Casa guardada = casaRepository.save(casa);

        Gallery gallery = new Gallery(null, dto.getTitulo(), guardada);
        galleryService.createGallery(gallery);

        return guardada;
    }

    //---------------------------------------- BORRAR (BAJA LOGICA) ----------------------------------------
    @Transactional
    public void borrarPorId(Long idCasa) {
        obtenerPorId(idCasa); // valida existencia
        alojamientoService.desactivar(idCasa);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Casa modificar(CasaModificarDTO dto) {

        Casa casa = obtenerPorId(dto.getIdCasa());

        if (dto.getTienePatio() != null) {
            casa.setTienePatio(dto.getTienePatio());
        }
        if (dto.getTienePileta() != null) {
            casa.setTienePileta(dto.getTienePileta());
        }
        if (dto.getTieneParrilla() != null) {
            casa.setTieneParrilla(dto.getTieneParrilla());
        }

        Casa cambiosComunes = new Casa();
        mapearComunModificar(cambiosComunes, dto);
        if (dto.getAnfitrion_id() != null) {
            cambiosComunes.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getAnfitrion_id()));
        }
        if (dto.getActivo() != null) {
            cambiosComunes.setActivo(dto.getActivo());
        }

        alojamientoService.modificarObjeto(casa, cambiosComunes);

        return casaRepository.save(casa);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    private void mapearComun(Casa casa, CasaRegistroDTO dto) {
        casa.setTitulo(dto.getTitulo());
        casa.setDescripcion(dto.getDescripcion());
        casa.setPrecioNoche(dto.getPrecioNoche());
        casa.setCapacidad(dto.getCapacidad());
        casa.setCantAmbientes(dto.getCantAmbientes());
        casa.setCantHabitaciones(dto.getCantHabitaciones());
        casa.setCantCamas(dto.getCantCamas());
        casa.setCantBanios(dto.getCantBanios());
        casa.setAmenities(amenityService.resolver(dto.getAmenityIds()));

        casa.setDireccion(new Direccion(
                null,
                dto.getPais().toLowerCase(),
                dto.getProvincia().toLowerCase(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle().toLowerCase(),
                dto.getAltura()
        ));
    }

    private void mapearComunModificar(Casa cambios, CasaModificarDTO dto) {
        cambios.setTitulo(dto.getTitulo());
        cambios.setDescripcion(dto.getDescripcion());
        cambios.setPrecioNoche(dto.getPrecioNoche());
        cambios.setCapacidad(dto.getCapacidad());
        cambios.setCantAmbientes(dto.getCantAmbientes());
        cambios.setCantHabitaciones(dto.getCantHabitaciones());
        cambios.setCantCamas(dto.getCantCamas());
        cambios.setCantBanios(dto.getCantBanios());
        if (dto.getAmenityIds() != null) {
            cambios.setAmenities(amenityService.resolver(dto.getAmenityIds()));
        }

        if (dto.getPais() != null || dto.getProvincia() != null || dto.getCodigoPostal() != null
                || dto.getCiudad() != null || dto.getCalle() != null || dto.getAltura() != null) {
            Direccion direccion = new Direccion();
            direccion.setPais(dto.getPais());
            direccion.setProvincia(dto.getProvincia());
            direccion.setCodigoPostal(dto.getCodigoPostal());
            direccion.setCiudad(dto.getCiudad());
            direccion.setCalle(dto.getCalle());
            direccion.setAltura(dto.getAltura());
            cambios.setDireccion(direccion);
        } else {
            cambios.setDireccion(new Direccion());
        }
    }
}
