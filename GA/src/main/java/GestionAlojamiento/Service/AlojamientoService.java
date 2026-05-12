package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.TipoInmueble;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.AnfitrionRepository;
import GestionAlojamiento.Repository.DireccionRepository;
import GestionAlojamiento.Repository.ServicioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final AnfitrionRepository anfitrionRepository;
    private final DireccionRepository direccionRepository;
    private final ServicioRepository servicioRepository;


    //------------------------ LISTAR ------------------------
    /*
     * Nota: Deberia Ordenarse por valor o por cantidad de lo que se lista? -> Preguntar  al profe? -Eloy
     */
    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
    }

    //Listado por precios tienen 4 opciones de parametros TODO, MENOR, MAYOR, ENTRE
    public List<Alojamiento> listarPorPrecio() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "precio_noche"));
    }

    public List<Alojamiento> listarPorPrecioMenorQue(double precio_deseado) {
        return alojamientoRepository.findByPrecioNocheLessThan(precio_deseado, Sort.by(Sort.Direction.ASC));
    }

    public List<Alojamiento> listarPorPrecioMayorrQue(double precio_deseado) {
        return alojamientoRepository.findByPrecioNocheGreaterThan(precio_deseado, Sort.by(Sort.Direction.ASC));
    }

    public List<Alojamiento> listarPorEntrePrecios(double precio_min, double precio_max) {
        return alojamientoRepository.findByPrecio_nocheBetween(precio_min, precio_max, Sort.by(Sort.Direction.ASC));
    }

    //Segun capacidad
    public List<Alojamiento> listarPorCapacidad() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "capacidad"));
    }

    public List<Alojamiento> listarPorCapacidadMin(Integer capacidad) {
        return alojamientoRepository.findAllByCapacidadMin(capacidad, Sort.by(Sort.Direction.ASC));
    }

    public List<Alojamiento> listarPorCapacidadMax(Integer capacidad, Sort sort) {
        return alojamientoRepository.findAllByCapacidadMax(capacidad, Sort.by(Sort.Direction.ASC));
    }

    public List<Alojamiento> listarPorEntreCapacidad(Integer capacidad_min, Integer capacidad_max, Sort sort) {
        return alojamientoRepository.findAllByCapacidadBetween(capacidad_min, capacidad_max, Sort.by(Sort.Direction.ASC, "capacidad"));
    }

    // Según Cantidad de Ambientes
    public List<Alojamiento> listarPorAmbientesMin(Integer cant) {
        return alojamientoRepository.findByCantAmbientesGreaterThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantAmbientes"));
    }

    public List<Alojamiento> listarPorAmbientesMax(Integer cant) {
        return alojamientoRepository.findByCantAmbientesLessThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantAmbientes"));
    }

    public List<Alojamiento> listarPorEntreAmbientes(Integer cant_min, Integer cant_max) {
        return alojamientoRepository.findByCantAmbientesBetween(cant_min, cant_max, Sort.by(Sort.Direction.ASC, "cantAmbientes"));
    }

    // Según Cantidad de Habitaciones
    public List<Alojamiento> listarPorHabitacionesMin(Integer cant) {
        return alojamientoRepository.findByCantHabitacionesGreaterThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantHabitaciones"));
    }

    public List<Alojamiento> listarPorHabitacionesMax(Integer cant) {
        return alojamientoRepository.findByCantHabitacionesLessThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantHabitaciones"));
    }

    public List<Alojamiento> listarPorEntreHabitaciones(Integer cant_min, Integer cant_max) {
        return alojamientoRepository.findByCantHabitacionesBetween(cant_min, cant_max, Sort.by(Sort.Direction.ASC, "cantHabitaciones"));
    }

    // Según Cantidad de Camas
    public List<Alojamiento> listarPorCamasMin(Integer cant) {
        return alojamientoRepository.findByCantCamasGreaterThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantCamas"));
    }

    public List<Alojamiento> listarPorCamasMax(Integer cant) {
        return alojamientoRepository.findByCantCamasLessThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantCamas"));
    }

    public List<Alojamiento> listarPorEntreCamas(Integer cant_min, Integer cant_max) {
        return alojamientoRepository.findByCantCamasBetween(cant_min, cant_max, Sort.by(Sort.Direction.ASC, "cantCamas"));
    }

    // Según Cantidad de Baños
    public List<Alojamiento> listarPorBaniosMin(Integer cant) {
        return alojamientoRepository.findByCantBaniosGreaterThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantBanios"));
    }

    public List<Alojamiento> listarPorBaniosMax(Integer cant) {
        return alojamientoRepository.findByCantBaniosLessThanEqual(cant, Sort.by(Sort.Direction.ASC, "cantBanios"));
    }

    public List<Alojamiento> listarPorEntreBanios(Integer cant_min, Integer cant_max) {
        return alojamientoRepository.findByCantBaniosBetween(cant_min, cant_max, Sort.by(Sort.Direction.ASC, "cantBanios"));
    }

    //Segun Activo
    public List<Alojamiento> listarPorEstado(boolean estado) {
        return alojamientoRepository.findByEstado(estado, Sort.by(Sort.Direction.ASC, "titulo"));
    }

    //Segun tipoInmueble
    public List<Alojamiento> listarPorInmueble(TipoInmueble tipoInmueble) {
        return alojamientoRepository.findByTipo(tipoInmueble, Sort.by(Sort.Direction.ASC, "titulo"));
    }

  /*
  Esto deberia LISTARSE con un DTO?

    > CONSTRAINT fk_aloj_anfitrion FOREIGN KEY (id_anfitrion) REFERENCES anfitrion(id_usuario) ON DELETE CASCADE,
    > CONSTRAINT fk_aloj_direccion FOREIGN KEY (id_direccion) REFERENCES direccion(id_direccion),
	> CONSTRAINT fk_aloj_servicios FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
   */

    //------------------------ GUARDAR/BORRAR ------------------------
    @Transactional
    public Alojamiento crear(Alojamiento alojamiento, Long id_anfitrion, Long id_direccion, Long id_servicio) {
        //Checks
        alojamiento.setAnfitrion(anfitrionRepository.findById(id_anfitrion).orElseThrow(() -> new RuntimeException("Error, el anfitrion no existe en la base de datos.")));
        alojamiento.setDireccion(direccionRepository.findById(id_direccion).orElseThrow(() -> new RuntimeException("Error, la direccion no existe en la base de datos.")));
        alojamiento.setServicio(servicioRepository.findById(id_servicio).orElseThrow(() -> new RuntimeException("Error, el servicio no existe en la base de datos.")));

        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public void borrar(Long id_alojamiento) {
        if (!alojamientoRepository.existsById(id_alojamiento)) {
            throw new RuntimeException("Error, El alojamiento no se encuentra en la base de datos.");
        }
        alojamientoRepository.deleteById(id_alojamiento);
    }
    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Alojamiento actualizarTitulo(Long id, String nuevoTitulo) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setTitulo(nuevoTitulo);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarDescripcion(Long id, String nuevaDescripcion) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setDescripcion(nuevaDescripcion);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarPrecioNoche(Long id, BigDecimal nuevoPrecio) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setPrecio_noche(nuevoPrecio);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarCapacidad(Long id, Integer nuevaCapacidad) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setCapacidad(nuevaCapacidad);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarCantAmbientes(Long id, Integer nuevaCantidad) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setCant_ambientes(nuevaCantidad);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarCantHabitaciones(Long id, Integer nuevaCantidad) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setCant_habitaciones(nuevaCantidad);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarCantCamas(Long id, Integer nuevaCantidad) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setCant_camas(nuevaCantidad);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarCantBanios(Long id, Integer nuevaCantidad) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setCant_banios(nuevaCantidad);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarActivo(Long id, Boolean nuevoEstado) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setActivo(nuevoEstado);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarTipo(Long id, TipoInmueble nuevoTipo) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setTipoInmueble(nuevoTipo);
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarAnfitrion(Long id, Long idAnfitrion) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setAnfitrion(anfitrionRepository.findById(idAnfitrion).orElseThrow(() -> new RuntimeException("El anfitrion no existe en la base de datos.")));
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarDireccion(Long id, Long idDireccion) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setDireccion(direccionRepository.findById(idDireccion).orElseThrow(() -> new RuntimeException("La direccion no existe en la base de datos.")));
        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public Alojamiento actualizarServicio(Long id, Long idServicio) {
        Alojamiento alojamiento = alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        alojamiento.setServicio(servicioRepository.findById(idServicio).orElseThrow(() -> new RuntimeException("El servicio no existe en la base de datos.")));
        return alojamientoRepository.save(alojamiento);
    }
//Como seguridad extra se podria agregar que los valores respeten ciertos valores no sean menores que 0, ej en habitaciones haciendo doble check de seguridad -Eloy

}
