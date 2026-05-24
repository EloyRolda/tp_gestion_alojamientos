package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
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

    private final AnfitrionService anfitrionService;
    private final DireccionService direccionService;
    private final ServicioService servicioService;


    /// Devuelve una lista de todos los alojamientos registrados
    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
    }

    /// Devuelve un Objeto Alojamiento en base al id
    public Alojamiento obtenerPorId(Long id) {
        return alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, alojamiento no encontrado en la base de datos."));
    }

    /// Obtiene un Objeto Alojamiento y lo guarda con sus respectivas referencias a anfitrion, direccion y servicio.
    @Transactional
    public Alojamiento crear(Alojamiento alojamiento) {
        //Checks
        alojamiento.setAnfitrion(anfitrionService.obtenerPorId(alojamiento.getId()));
        alojamiento.setDireccion(direccionService.obtenerPorId(alojamiento.getDireccion().getId()));
        alojamiento.setServicio(servicioService.obtenerPorId(alojamiento.getServicio().getId()));

        return alojamientoRepository.save(alojamiento);
    }


    //Como seguridad extra se podria agregar que los valores respeten ciertos valores no sean menores que 0, ej en habitaciones haciendo doble check de seguridad -Eloy
    /// Modifica el objeto SERVICE SIN TOCAR LA BASE DE DATOS LLAMANDO A DISPONIBILIDAD, SERVICIO Y DIRECCION y devuelve el objeto modificado





}
