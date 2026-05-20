package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Model.Enums.TipoInmueble;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final DireccionService direccionService;
    private final AlojamientoService alojamientoService;
    private final AnfitrionService anfitrionService;

    //------------------------ LISTAR ------------------------
    public List<Hotel> listarTodos() {
        return hotelRepository.findAll();
    }

    public Hotel obtenerPorId(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, id de hotel no encontrado en la base de datos."));
    }

    public List<Hotel> listarPorIncluyeDesayuno() {
        return hotelRepository.findByIncluyeDesayunoTrue();
    }

    public List<Hotel> listarPorServicioLimpieza() {
        return hotelRepository.findByServicioLimpiezaTrue();
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Hotel crear(HotelRegistroDTO hotelRegistroDTO) {

        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setCantAmbientes(hotelRegistroDTO.getCantAmbientes());
        alojamiento.setCantBanios(hotelRegistroDTO.getCantBanios());
        alojamiento.setCantCamas(hotelRegistroDTO.getCantCamas());
        alojamiento.setCantHabitaciones(hotelRegistroDTO.getCantHabitaciones());
        alojamiento.setCapacidad(hotelRegistroDTO.getCapacidad());
        alojamiento.setDescripcion(hotelRegistroDTO.getDescripcion());
        alojamiento.setPrecioNoche(hotelRegistroDTO.getPrecioNoche());
        alojamiento.setAnfitrion(anfitrionService.obtenerPorId(hotelRegistroDTO.getIdAnfitrion()));

        Direccion direccion = new Direccion();
        direccion.setPais(hotelRegistroDTO.getPais());
        direccion.setProvincia(hotelRegistroDTO.getProvincia());
        direccion.setCodigoPostal(hotelRegistroDTO.getCodigoPostal());
        direccion.setCiudad(hotelRegistroDTO.getCiudad());
        direccion.setCalle(hotelRegistroDTO.getCalle());
        direccion.setAltura(hotelRegistroDTO.getAltura());
        alojamiento.setDireccion(direccionService.crear(direccion));

        Hotel hotel = new Hotel();

        hotel.setEstrellas(hotelRegistroDTO.getEstrellas());
        hotel.setIncluyeDesayuno(hotelRegistroDTO.isIncluyeDesayuno());
        hotel.setServicioLimpieza(hotelRegistroDTO.isIncluyeLimpieza());
        hotel.setAlojamiento(alojamientoService.crear(alojamiento));

        return hotelRepository.save(hotel);
    }

    @Transactional
    public void borrarPorId(Long id_hotel) {
        if (!hotelRepository.existsById(id_hotel)) {
            throw new RuntimeException("Error, el id de hotel no se encuentra en la base de datos.");
        }
        hotelRepository.deleteById(id_hotel);
    }

    //------------------------ MODIFICAR ------------------------
    @Transactional
    public Hotel actualizar(HotelModificarDTO dto) {

        Hotel hotel = obtenerPorId(dto.getId());

        if (dto.getEstrellas() != null) {
            hotel.setEstrellas(dto.getEstrellas());
        }

        if (dto.getIdAnfitrion() != null) {
            hotel.getAlojamiento().setAnfitrion(
                    anfitrionService.obtenerPorId(dto.getIdAnfitrion())
            );
        }

        if (dto.getCantAmbientes() != null) {
            hotel.getAlojamiento().setCantAmbientes(dto.getCantAmbientes());
        }

        if (dto.getCantBanios() != null) {
            hotel.getAlojamiento().setCantBanios(dto.getCantBanios());
        }

        if (dto.getCantCamas() != null) {
            hotel.getAlojamiento().setCantCamas(dto.getCantCamas());
        }

        if (dto.getCantHabitaciones() != null) {
            hotel.getAlojamiento().setCantHabitaciones(dto.getCantHabitaciones());
        }

        if (dto.getCapacidad() != null) {
            hotel.getAlojamiento().setCapacidad(dto.getCapacidad());
        }

        if (dto.getDescripcion() != null) {
            hotel.getAlojamiento().setDescripcion(dto.getDescripcion());
        }

        if (dto.getTitulo() != null) {
            hotel.getAlojamiento().setTitulo(dto.getTitulo());
        }

        if (dto.getPrecioNoche() != null) {
            hotel.getAlojamiento().setPrecioNoche(dto.getPrecioNoche());
        }

        if (dto.getPais() != null) {
            hotel.getAlojamiento().getDireccion().setPais(dto.getPais());
        }

        if (dto.getProvincia() != null) {
            hotel.getAlojamiento().getDireccion().setProvincia(dto.getProvincia());
        }

        if (dto.getCodigoPostal() != null) {
            hotel.getAlojamiento().getDireccion().setCodigoPostal(dto.getCodigoPostal());
        }

        if (dto.getCiudad() != null) {
            hotel.getAlojamiento().getDireccion().setCiudad(dto.getCiudad());
        }

        if (dto.getCalle() != null) {
            hotel.getAlojamiento().getDireccion().setCalle(dto.getCalle());
        }

        if (dto.getAltura() != null) {
            hotel.getAlojamiento().getDireccion().setAltura(dto.getAltura());
        }

        hotel.setServicioLimpieza(dto.isServicioLimpieza());
        hotel.setIncluyeDesayuno(dto.isIncluyeDesayuno());

        return hotelRepository.save(hotel);
    }
}