package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReviewModificarDTO;
import GestionAlojamiento.DTO.ReviewRegistroDTO;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/listar")
    public List<Review> listar() {
        return reviewService.listarTodas();
    }

    @GetMapping("/mostrar/{id}")
    public Review obtenerPorId(@PathVariable Long id) {
        return reviewService.obtenerPorId(id);
    }

    @PostMapping("/registrar")
    public Review crear(@RequestBody @Valid ReviewRegistroDTO dto, Authentication auth) {
        return reviewService.crear(dto, auth.getName());
    }

    @PutMapping("/actualizar")
    public Review modificar(@RequestBody @Valid ReviewModificarDTO dto, Authentication auth) {
        return reviewService.modificar(dto, auth.getName());
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication auth) {
        reviewService.borrarPorId(id, auth.getName());
        return "La review fue eliminada correctamente.";
    }

    @GetMapping("/alojamiento/{idAlojamiento}")
    public List<Review> listarPorAlojamiento(@PathVariable Long idAlojamiento) {
        return reviewService.listarPorAlojamiento(idAlojamiento);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Review> mostrarPorIdCliente(@PathVariable Long idCliente) {
        return reviewService.listarPorCliente(idCliente);
    }
}
