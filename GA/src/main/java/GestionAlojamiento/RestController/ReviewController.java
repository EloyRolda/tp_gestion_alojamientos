package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReviewModificarDTO;
import GestionAlojamiento.DTO.ReviewRegistroDTO;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
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

    @PostMapping("/crear")
    public Review crear(@RequestBody @Valid ReviewRegistroDTO reviewRegistroDTO) {
        return reviewService.crear(reviewRegistroDTO);
    }




    @PutMapping("/modificar")
    public Review modificar(@RequestBody @Valid ReviewModificarDTO dto) {
        return reviewService.modificar(dto);
    }


    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        reviewService.borrarPorId(id);

        return "La review fue eliminada correctamente.";
    }

    @GetMapping("/alojamiento/{idAlojamiento}")
    public List<Review> listarPorAlojamiento(@PathVariable Long idAlojamiento) {
        return reviewService.listarPorAlojamiento(idAlojamiento);
    }

    @GetMapping("/cliente/{id}")
    public List<Review> mostrarPorIdCliente(@PathVariable Long idCliente) {
        return reviewService.listarPorCliente(idCliente);
    }
}