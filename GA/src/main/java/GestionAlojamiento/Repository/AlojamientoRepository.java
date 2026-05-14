package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.TipoInmueble;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    //findBy precioNoche
    List<Alojamiento> findByPrecioNocheLessThan(double precio, Sort sort);

    List<Alojamiento> findByPrecioNocheGreaterThan(double precio, Sort sort);

    List<Alojamiento> findByPrecioNocheBetween(Double precio_min, Double precio_max, Sort sort);
    //findBy Capacidades
    List<Alojamiento> findAllByCapacidadGreaterThanEqual(Integer capacidad, Sort sort);
    List<Alojamiento> findAllByCapacidadLessThanEqual(Integer capacidad, Sort sort);

    List<Alojamiento> findAllByCapacidadBetween(Integer capacidad_min, Integer capacidad_max, Sort sort);


    //findBy Ambientes
    List<Alojamiento> findByCantAmbientesGreaterThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantAmbientesLessThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantAmbientesBetween(Integer cant_min, Integer cant_max, Sort sort);

    //findBy Habitaciones
    List<Alojamiento> findByCantHabitacionesGreaterThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantHabitacionesLessThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantHabitacionesBetween(Integer cant_min, Integer cant_max, Sort sort);

    //findBy Camas
    List<Alojamiento> findByCantCamasGreaterThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantCamasLessThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantCamasBetween(Integer cant_min, Integer cant_max, Sort sort);

    //findBy Baños
    List<Alojamiento> findByCantBaniosGreaterThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantBaniosLessThanEqual(Integer cant, Sort sort);

    List<Alojamiento> findByCantBaniosBetween(Integer cant_min, Integer cant_max, Sort sort);

    //findBy Estado
    List<Alojamiento> findByActivo(boolean activo, Sort sort);

    List<Alojamiento> findByTipoInmueble(TipoInmueble tipo, Sort sort);

}
