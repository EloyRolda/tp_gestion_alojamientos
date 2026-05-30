package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Model.Enums.TipoInmueble;
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

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Hotel> listarTodos() {
        return hotelRepository.findAll();
    }

    public Hotel obtenerPorId(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Error, id de hotel no encontrado en la base de datos."));
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Hotel crear(HotelRegistroDTO dto) {

        Hotel hotel = new Hotel();
        hotel.setEstrellas(dto.getEstrellas());
        hotel.setIncluyeDesayuno(dto.isIncluyeDesayuno());
        hotel.setIncluyeLimpieza(dto.isIncluyeLimpieza());

        hotel.setAlojamiento(mapearAlojamiento(dto));

        return hotelRepository.save(hotel);
    }

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id_hotel) {
        if (!hotelRepository.existsById(id_hotel)) {
            throw new IdNoEncontradoException("Error, el id de HOTEL no se encuentra en la base de datos:" + id_hotel);
        }
        hotelRepository.deleteById(id_hotel);
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

        hotel.setAlojamiento(alojamientoService.modificarObjeto(hotel.getAlojamiento(), mapearAlojamiento(dto)));

        return hotelRepository.save(hotel);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    private Alojamiento mapearAlojamiento(HotelModificarDTO dto) {

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
                TipoInmueble.HOTEL,
                anfitrion,
                direccion,
                servicio
        );
    }

    private Alojamiento mapearAlojamiento(HotelRegistroDTO dto) {

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
                TipoInmueble.HOTEL,
                usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()),
                direccion,
                servicio
        );
    }
}