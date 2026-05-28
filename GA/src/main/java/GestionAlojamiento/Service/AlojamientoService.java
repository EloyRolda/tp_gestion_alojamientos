package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Repository.AlojamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final DireccionService direccionService;
    private final ServicioService servicioService;


    /// Devuelve una lista de todos los alojamientos registrados
    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
    }

    /// Devuelve una lista de alojamientos activos para el frontend publico
    public List<Alojamiento> listarActivos() {
        return alojamientoRepository.findByActivoTrueOrderByTituloAsc();
    }

    /// Devuelve un Objeto Alojamiento en base al id
    public Alojamiento obtenerPorId(Long id) {
        return alojamientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, alojamiento no encontrado en la base de datos."));
    }

    //Como seguridad extra se podria agregar que los valores respeten ciertos valores no sean menores que 0, ej en habitaciones haciendo doble check de seguridad -Eloy

    /// Modifica el objeto SERVICE SIN TOCAR LA BASE DE DATOS LLAMANDO A DISPONIBILIDAD, SERVICIO Y DIRECCION y devuelve el objeto modificado
    /// Original hace referencia al objeto que queremos cambiar, CAMBIOS hace referencia a los campos modificados
    public Alojamiento modificarObjeto(Alojamiento original, Alojamiento cambios) {

        original.setServicio(servicioService.modificarObjeto(original.getServicio(), cambios.getServicio()));
        original.setDireccion(direccionService.modificarObjeto(original.getDireccion(), cambios.getDireccion()));

        if (cambios.getTitulo() != null) {
            original.setTitulo(cambios.getTitulo());
        }
        if (cambios.getDescripcion() != null) {
            original.setDescripcion(cambios.getDescripcion());
        }
        if (cambios.getPrecioNoche() != null) {
            original.setPrecioNoche(cambios.getPrecioNoche());
        }
        if (cambios.getCapacidad() != null) {
            original.setCapacidad(cambios.getCapacidad());
        }
        if (cambios.getCantAmbientes() != null) {
            original.setCantAmbientes(cambios.getCantAmbientes());
        }
        if (cambios.getCantHabitaciones() != null) {
            original.setCantHabitaciones(cambios.getCantHabitaciones());
        }
        if (cambios.getCantCamas() != null) {
            original.setCantCamas(cambios.getCantCamas());
        }
        if (cambios.getCantBanios() != null) {
            original.setCantBanios(cambios.getCantBanios());
        }
        if (cambios.getAnfitrion() != null) {
            original.setAnfitrion(cambios.getAnfitrion());
        }
        if (cambios.getActivo() != null) {
            original.setActivo(cambios.getActivo());
        }
        return original;
    }


}
