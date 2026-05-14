package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
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
    public Hotel crear(Long id_alojamiento, Hotel hotel) {
        Alojamiento alojamiento = alojamientoRepository.findById(id_alojamiento)
                .orElseThrow(() -> new RuntimeException("Error, id de alojamiento inválido o no encontrado."));

        hotel.setAlojamiento(alojamiento);
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
    public Hotel actualizarEstrellas(Long id, Integer nuevasEstrellas) {
        if (nuevasEstrellas < 1 || nuevasEstrellas > 5) {
            throw new RuntimeException("Las estrellas deben estar entre 1 y 5.");
        }
        Hotel hotel = obtenerPorId(id);
        hotel.setEstrellas(nuevasEstrellas);
        return hotelRepository.save(hotel);
    }

    @Transactional
    public Hotel actualizarIncluyeDesayuno(Long id, boolean nuevoEstado) {
        Hotel hotel = obtenerPorId(id);
        hotel.setIncluyeDesayuno(nuevoEstado);
        return hotelRepository.save(hotel);
    }

    @Transactional
    public Hotel actualizarServicioLimpieza(Long id, boolean nuevoEstado) {
        Hotel hotel = obtenerPorId(id);
        hotel.setServicioLimpieza(nuevoEstado);
        return hotelRepository.save(hotel);
    }
}