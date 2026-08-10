package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Model.Gallery;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;
    private final AmenityService amenityService;
    private final GalleryService galleryService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Hotel> listarTodos() {
        return hotelRepository.findAll();
    }

    public Hotel obtenerPorId(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, id de hotel no encontrado en la base de datos."));
    }

    public List<Hotel> listarPorAnfitrion(String correoAnfitrion) {
        return hotelRepository.findByAnfitrionEmail(correoAnfitrion);
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Hotel crear(HotelRegistroDTO dto) {

        Hotel hotel = new Hotel();
        hotel.setEstrellas(dto.getEstrellas());
        hotel.setIncluyeDesayuno(dto.isIncluyeDesayuno());
        hotel.setIncluyeLimpieza(dto.isIncluyeLimpieza());

        mapearComun(hotel, dto);
        hotel.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()));
        hotel.setActivo(true);

        Hotel guardado = hotelRepository.save(hotel);

        Gallery gallery = new Gallery(null, dto.getTitulo(), guardado);
        galleryService.createGallery(gallery);

        return guardado;
    }

    //---------------------------------------- BORRAR (BAJA LOGICA) ----------------------------------------
    @Transactional
    public void borrarPorId(Long idHotel) {
        obtenerPorId(idHotel);
        alojamientoService.desactivar(idHotel);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Hotel actualizar(HotelModificarDTO dto) {
        Hotel hotel = obtenerPorId(dto.getId());

        if (dto.getEstrellas() != null) {
            hotel.setEstrellas(dto.getEstrellas());
        }
        if (dto.getIncluyeDesayuno() != null) {
            hotel.setIncluyeDesayuno(dto.getIncluyeDesayuno());
        }
        if (dto.getIncluyeLimpieza() != null) {
            hotel.setIncluyeLimpieza(dto.getIncluyeLimpieza());
        }

        Hotel cambiosComunes = new Hotel();
        mapearComunModificar(cambiosComunes, dto);
        if (dto.getAnfitrion_id() != null) {
            cambiosComunes.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getAnfitrion_id()));
        }
        if (dto.getActivo() != null) {
            cambiosComunes.setActivo(dto.getActivo());
        }

        alojamientoService.modificarObjeto(hotel, cambiosComunes);

        return hotelRepository.save(hotel);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    private void mapearComun(Hotel hotel, HotelRegistroDTO dto) {
        hotel.setTitulo(dto.getTitulo());
        hotel.setDescripcion(dto.getDescripcion());
        hotel.setPrecioNoche(dto.getPrecioNoche());
        hotel.setCapacidad(dto.getCapacidad());
        hotel.setCantAmbientes(dto.getCantAmbientes());
        hotel.setCantHabitaciones(dto.getCantHabitaciones());
        hotel.setCantCamas(dto.getCantCamas());
        hotel.setCantBanios(dto.getCantBanios());
        hotel.setAmenities(amenityService.resolver(dto.getAmenityIds()));

        hotel.setDireccion(new Direccion(
                null,
                dto.getPais().toLowerCase(),
                dto.getProvincia().toLowerCase(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle().toLowerCase(),
                dto.getAltura()
        ));
    }

    private void mapearComunModificar(Hotel cambios, HotelModificarDTO dto) {
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

        Direccion direccion = new Direccion();
        direccion.setPais(dto.getPais());
        direccion.setProvincia(dto.getProvincia());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCalle(dto.getCalle());
        direccion.setAltura(dto.getAltura());
        cambios.setDireccion(direccion);
    }
}
