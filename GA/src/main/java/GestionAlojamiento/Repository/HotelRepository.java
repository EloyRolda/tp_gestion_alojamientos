package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByServicioLimpiezaTrue();


    List<Hotel> findByIncluyeDesayunoTrue();

}
