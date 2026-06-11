package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Model.Enums.TipoInmueble;
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
    private final GalleryService galleryService;
    private final ReservaService reservaService;
    private final ReviewService reviewService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Casa> listar() {
        return casaRepository.findAll();
    }

    public Casa obtenerPorId(Long id) {
        return casaRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Error, el id de la casa no se encuentra en la base de datos."));
    }

    /// Recibe un id de un anfitrion y devuelve una lista de casas que posee.
    public List<Casa> listarPorAnfitrion(String correoAnfitrion) {
        return casaRepository.findByAlojamientoAnfitrionEmail(correoAnfitrion);
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Casa crear(CasaRegistroDTO dto) {

        Casa casa = new Casa();
        casa.setTienePatio(dto.isTienePatio());
        casa.setTienePileta(dto.isTienePileta());
        casa.setTieneParrilla(dto.isTieneParrilla());

        casa.setAlojamiento(mapearAlojamiento(dto));

        Gallery gallery = new Gallery(null, dto.getTitulo(), casa.getAlojamiento());
        galleryService.createGallery(gallery);

        return casaRepository.save(casa);
    }

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id_casa) {
        Casa casa = casaRepository.findById(id_casa)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el id de CASA no se encuentra en la base de datos:" + id_casa));
        Alojamiento alojamiento = casa.getAlojamiento();
        reservaService.borrarPorAlojamientoId(alojamiento.getId());
        reviewService.borrarPorAlojamiento(alojamiento);
        galleryService.borrarPorAlojamientoId(alojamiento.getId());
        casaRepository.deleteById(id_casa);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Casa modificar(CasaModificarDTO dto) {

        Alojamiento dtoAlojamiento = mapearAlojamiento(dto);

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

        casa.setAlojamiento(alojamientoService.modificarObjeto(casa.getAlojamiento(), dtoAlojamiento));

        return casaRepository.save(casa);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    private Alojamiento mapearAlojamiento(CasaModificarDTO dto) {

        Direccion direccion = new Direccion(
                null,
                dto.getPais(),
                dto.getProvincia(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle(),
                dto.getAltura()
        );

        Servicio servicio = new Servicio(
                null,
                dto.getTieneCocina(),
                dto.getTieneLavarropa(),
                dto.getTieneWifi(),
                dto.getTieneEstacionamiento()
        );

        Usuario anfitrion = null;
        if (dto.getAnfitrion_id() != null) {
            anfitrion = usuarioService.obtenerAnfitrionPorId(dto.getAnfitrion_id());
        }

        return new Alojamiento(
                null,
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getPrecioNoche(),
                dto.getCapacidad(),
                dto.getCantAmbientes(),
                dto.getCantHabitaciones(),
                dto.getCantCamas(),
                dto.getCantBanios(),
                dto.getActivo(),
                TipoInmueble.CASA,
                anfitrion,
                direccion,
                servicio
        );
    }

    private Alojamiento mapearAlojamiento(CasaRegistroDTO dto) {

        Direccion direccion = new Direccion(
                null,
                dto.getPais(),
                dto.getProvincia(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle(),
                dto.getAltura()
        );

        Servicio servicio = new Servicio(
                null,
                dto.isTieneCocina(),
                dto.isTieneLavarropa(),
                dto.isTieneWifi(),
                dto.isTieneEstacionamiento()
        );

        return new Alojamiento(
                null,
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getPrecioNoche(),
                dto.getCapacidad(),
                dto.getCantAmbientes(),
                dto.getCantHabitaciones(),
                dto.getCantCamas(),
                dto.getCantBanios(),
                true,
                TipoInmueble.CASA,
                usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()),
                direccion,
                servicio
        );
    }
}