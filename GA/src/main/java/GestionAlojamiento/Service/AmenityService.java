package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.AmenityRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.RecursoDuplicadoException;
import GestionAlojamiento.Model.Amenity;
import GestionAlojamiento.Repository.AmenityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public List<Amenity> listarTodos() {
        return amenityRepository.findAll();
    }

    public Amenity obtenerPorId(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Amenity no encontrado: " + id));
    }

    /// Resuelve una lista de ids (la que manda el frontend al crear/editar un alojamiento)
    /// a las entidades reales. Si viene null o vacia, devuelve un set vacio (alojamiento sin amenities).
    public Set<Amenity> resolver(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return ids.stream().map(this::obtenerPorId).collect(Collectors.toSet());
    }

    @Transactional
    public Amenity crear(AmenityRegistroDTO dto) {
        if (amenityRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new RecursoDuplicadoException("Ya existe un amenity con ese nombre.");
        }
        Amenity amenity = new Amenity();
        amenity.setNombre(dto.getNombre());
        amenity.setCategoria(dto.getCategoria());
        return amenityRepository.save(amenity);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new IdNoEncontradoException("Amenity no encontrado: " + id);
        }
        amenityRepository.deleteById(id);
    }
}
