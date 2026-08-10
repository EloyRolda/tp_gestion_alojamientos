package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Model.Departamento;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Repository.AlojamientoRepository;
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
    private final DireccionService direccionService;

    /// Devuelve TODOS los alojamientos (cualquier tipo, activos o no). Uso administrativo/interno.
    public List<Alojamiento> listarTodos() {
        return alojamientoRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
    }

    /// Listado publico estilo Airbnb: mezcla Casa/Departamento/Hotel, solo activos y publicados,
    /// con filtros opcionales (todos pueden venir null y se ignoran). El filtro de tipo se
    /// aplica en memoria sobre el resultado ya acotado por la query (evita pasarle un Class<?> a JPQL).
    public List<Alojamiento> buscarPublico(String ciudad, Integer capacidadMinima, BigDecimal precioMin, BigDecimal precioMax, String tipo) {
        List<Alojamiento> resultado = alojamientoRepository.buscar(ciudad, capacidadMinima, precioMin, precioMax);

        if (tipo == null || tipo.isBlank()) {
            return resultado;
        }

        Class<? extends Alojamiento> claseFiltro = mapearTipo(tipo);
        return resultado.stream().filter(claseFiltro::isInstance).collect(Collectors.toList());
    }

    private Class<? extends Alojamiento> mapearTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return null;
        }
        return switch (tipo.trim().toUpperCase()) {
            case "CASA" -> Casa.class;
            case "DEPARTAMENTO" -> Departamento.class;
            case "HOTEL" -> Hotel.class;
            default -> throw new ParametroInvalidoException("Tipo de alojamiento invalido: " + tipo);
        };
    }

    /// Devuelve la lista de ids de alojamientos del anfitrion (cualquier subtipo).
    public List<Long> obtenerIdsPorAnfitrion(Long anfitrionId) {
        return alojamientoRepository.findByAnfitrionId(anfitrionId)
                .stream()
                .map(Alojamiento::getId)
                .collect(Collectors.toList());
    }

    public Alojamiento obtenerPorId(Long id) {
        return alojamientoRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, alojamiento no encontrado en la base de datos."));
    }

    /// Copia al objeto ORIGINAL los campos no nulos de CAMBIOS (patron ya usado en todo el proyecto).
    /// No persiste: quien llama decide cuando guardar (lo hace el Service del subtipo correspondiente).
    public Alojamiento modificarObjeto(Alojamiento original, Alojamiento cambios) {

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
        if (cambios.getAmenities() != null && !cambios.getAmenities().isEmpty()) {
            original.setAmenities(cambios.getAmenities());
        }
        return original;
    }

    /// Baja logica: la usa el dueño o el admin. No borra reservas ni reviews (se preservan para historial/estadisticas).
    @Transactional
    public void desactivar(Long id) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setActivo(false);
        alojamiento.setPublicado(false);
        alojamientoRepository.save(alojamiento);
    }

    @Transactional
    public void reactivar(Long id) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setActivo(true);
        alojamientoRepository.save(alojamiento);
    }

    /// Cambia el flag de publicado. Lo invoca GalleryService una vez que verifico
    /// que el alojamiento cumple el minimo de fotos requerido.
    @Transactional
    public void actualizarPublicado(Long id, boolean publicado) {
        Alojamiento alojamiento = obtenerPorId(id);
        alojamiento.setPublicado(publicado);
        alojamientoRepository.save(alojamiento);
    }
}
