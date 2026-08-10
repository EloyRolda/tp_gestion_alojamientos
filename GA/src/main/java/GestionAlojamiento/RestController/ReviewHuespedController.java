package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReviewHuespedRegistroDTO;
import GestionAlojamiento.Model.ReviewHuesped;
import GestionAlojamiento.Service.ReviewHuespedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Resena breve que el ANFITRION deja sobre el HUESPED al finalizar la estadia.
@RestController
@RequestMapping("/ReviewHuesped")
@RequiredArgsConstructor
public class ReviewHuespedController {

    private final ReviewHuespedService reviewHuespedService;

    @PostMapping("/registrar")
    public ReviewHuesped crear(@RequestBody @Valid ReviewHuespedRegistroDTO dto, Authentication auth) {
        return reviewHuespedService.crear(dto, auth.getName());
    }

    @GetMapping("/huesped/{idHuesped}")
    public List<ReviewHuesped> listarPorHuesped(@PathVariable Long idHuesped) {
        return reviewHuespedService.listarPorHuesped(idHuesped);
    }
}
