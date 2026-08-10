package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.AmenityRegistroDTO;
import GestionAlojamiento.Model.Amenity;
import GestionAlojamiento.Service.AmenityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Amenity")
@RequiredArgsConstructor
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping("/listar")
    public List<Amenity> listar() {
        return amenityService.listarTodos();
    }

    /// Solo administrador (ver SecurityConfig): alta de un nuevo item de catalogo.
    @PostMapping("/registrar")
    public Amenity crear(@RequestBody @Valid AmenityRegistroDTO dto) {
        return amenityService.crear(dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        amenityService.borrarPorId(id);
    }
}
