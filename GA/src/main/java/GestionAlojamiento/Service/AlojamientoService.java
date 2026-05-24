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
    private final AnfitrionRepository anfitrionRepository;
    private final DireccionRepository direccionRepository;
    private final ServicioRepository servicioRepository;

    private final AnfitrionService anfitrionService;
    private final DireccionService direccionService;
    private final ServicioService servicioService;


    //------------------------ LISTAR ------------------------
    /*
     * Nota: Deberia Ordenarse por valor o por cantidad de lo que se lista? -> Preguntar  al profe? -Eloy
     */
    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
    }

    public Alojamiento obtenerPorId(Long id) {
        return alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, alojamiento no encontrado en la base de datos."));
    }

    //------------------------ GUARDAR/BORRAR ------------------------
    @Transactional
    public Alojamiento crear(Alojamiento alojamiento) {
        //Checks
        alojamiento.setAnfitrion(anfitrionService.obtenerPorId(alojamiento.getId()));
        alojamiento.setDireccion(direccionService.obtenerPorId(alojamiento.getDireccion().getId()));
        alojamiento.setServicio(servicioService.obtenerPorId(alojamiento.getServicio().getId()));

        return alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public void borrar(Long id_alojamiento) {
        if (!alojamientoRepository.existsById(id_alojamiento)) {
            throw new IdNoEncontradoException("ID no encontrado en la base de datos.");
        }
        alojamientoRepository.deleteById(id_alojamiento);
    }
    //Como seguridad extra se podria agregar que los valores respeten ciertos valores no sean menores que 0, ej en habitaciones haciendo doble check de seguridad -Eloy

}
