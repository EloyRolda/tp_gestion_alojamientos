package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Model.Enums.TipoInmueble;
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
    private final ServicioService servicioService;
    private final DisponibilidadService disponibilidadService;

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
        alojamiento.setTitulo(hotelRegistroDTO.getTitulo());
        alojamiento.setAnfitrion(anfitrionService.obtenerPorId(hotelRegistroDTO.getIdAnfitrion()));
        alojamiento.setTipoInmueble(TipoInmueble.HOTEL);


        // [DIRECCION]

        Direccion direccion = new Direccion();

        direccion.setPais(hotelRegistroDTO.getPais());
        direccion.setProvincia(hotelRegistroDTO.getProvincia());
        direccion.setCodigoPostal(hotelRegistroDTO.getCodigoPostal());
        direccion.setCiudad(hotelRegistroDTO.getCiudad());
        direccion.setCalle(hotelRegistroDTO.getCalle());
        direccion.setAltura(hotelRegistroDTO.getAltura());

        alojamiento.setDireccion(direccionService.crear(direccion));


        // [SERVICIO]

        Servicio servicio = new Servicio();

        servicio.setTieneCocina(hotelRegistroDTO.isTieneCocina());
        servicio.setTieneLavarropa(hotelRegistroDTO.isTieneLavarropa());
        servicio.setTieneWifi(hotelRegistroDTO.isTieneWifi());
        servicio.setTieneEstacionamiento(hotelRegistroDTO.isTieneEstacionamiento());

        alojamiento.setServicio(servicioService.crear(servicio));


        // [DISPONIBILIDAD]

        Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setFecha(hotelRegistroDTO.getFecha());
        disponibilidad.setDisponible(hotelRegistroDTO.isDisponible());

        alojamiento.setDisponibilidad(disponibilidadService.crear(disponibilidad));


        // [HOTEL]

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


        // [HOTEL]

        if (dto.getEstrellas() != null) {
            hotel.setEstrellas(dto.getEstrellas());
        }

        hotel.setServicioLimpieza(dto.isServicioLimpieza());

        hotel.setIncluyeDesayuno(dto.isIncluyeDesayuno());


        // [ANFITRION]

        if (dto.getIdAnfitrion() != null) {

            hotel.getAlojamiento().setAnfitrion(anfitrionService.obtenerPorId(dto.getIdAnfitrion()));
        }


        // [ALOJAMIENTO]

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

        if (dto.getDescripcion() != null &&
                !dto.getDescripcion().isBlank()) {

            hotel.getAlojamiento()
                    .setDescripcion(dto.getDescripcion());
        }

        if (dto.getTitulo() != null &&
                !dto.getTitulo().isBlank()) {

            hotel.getAlojamiento()
                    .setTitulo(dto.getTitulo());
        }

        if (dto.getPrecioNoche() != null) {

            hotel.getAlojamiento()
                    .setPrecioNoche(dto.getPrecioNoche());
        }


        // [DIRECCION]

        if (dto.getPais() != null &&
                !dto.getPais().isBlank()) {

            hotel.getAlojamiento()
                    .getDireccion()
                    .setPais(dto.getPais());
        }

        if (dto.getProvincia() != null &&
                !dto.getProvincia().isBlank()) {

            hotel.getAlojamiento()
                    .getDireccion()
                    .setProvincia(dto.getProvincia());
        }

        if (dto.getCodigoPostal() != null &&
                !dto.getCodigoPostal().isBlank()) {

            hotel.getAlojamiento()
                    .getDireccion()
                    .setCodigoPostal(dto.getCodigoPostal());
        }

        if (dto.getCiudad() != null &&
                !dto.getCiudad().isBlank()) {

            hotel.getAlojamiento()
                    .getDireccion()
                    .setCiudad(dto.getCiudad());
        }

        if (dto.getCalle() != null &&
                !dto.getCalle().isBlank()) {

            hotel.getAlojamiento()
                    .getDireccion()
                    .setCalle(dto.getCalle());
        }

        if (dto.getAltura() != null) {

            hotel.getAlojamiento()
                    .getDireccion()
                    .setAltura(dto.getAltura());
        }


        // [SERVICIO]

        if (hotel.getAlojamiento().getServicio() == null) {

            Servicio servicio = new Servicio();

            servicio.setTieneCocina(dto.isTieneCocina());
            servicio.setTieneLavarropa(dto.isTieneLavarropa());
            servicio.setTieneWifi(dto.isTieneWifi());
            servicio.setTieneEstacionamiento(dto.isTieneEstacionamiento());

            hotel.getAlojamiento().setServicio(servicio);

        } else {

            hotel.getAlojamiento().getServicio()
                    .setTieneCocina(dto.isTieneCocina());

            hotel.getAlojamiento().getServicio()
                    .setTieneLavarropa(dto.isTieneLavarropa());

            hotel.getAlojamiento().getServicio()
                    .setTieneWifi(dto.isTieneWifi());

            hotel.getAlojamiento().getServicio()
                    .setTieneEstacionamiento(dto.isTieneEstacionamiento());
        }


        // [DISPONIBILIDAD]

        if (hotel.getAlojamiento().getDisponibilidad() == null) {

            Disponibilidad disponibilidad = new Disponibilidad();

            disponibilidad.setFecha(dto.getFecha());
            disponibilidad.setDisponible(dto.isDisponible());

            hotel.getAlojamiento()
                    .setDisponibilidad(disponibilidad);

        } else {

            if (dto.getFecha() != null) {

                hotel.getAlojamiento()
                        .getDisponibilidad()
                        .setFecha(dto.getFecha());
            }

            hotel.getAlojamiento()
                    .getDisponibilidad()
                    .setDisponible(dto.isDisponible());
        }


        return hotelRepository.save(hotel);
    }
}