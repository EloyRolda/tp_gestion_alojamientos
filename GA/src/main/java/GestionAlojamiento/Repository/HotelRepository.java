package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Long> {


}
