package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReviewResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReviewRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReviewService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
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
    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<ReviewResponseDTO> listar() {
        return reviewService.listarTodasDTO();
    }

    @GetMapping("/mostrar/{id}")
    public ReviewResponseDTO mostrar(@PathVariable Long id) {
        return reviewService.obtenerDTOPorId(id);
    }

    @GetMapping("/alojamiento/{idAlojamiento}")
    public List<ReviewResponseDTO> listarPorAlojamiento(@PathVariable Long idAlojamiento) {
        return reviewService.listarPorAlojamientoDTO(idAlojamiento);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<ReviewResponseDTO> listarPorCliente(@PathVariable Long idCliente, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        boolean esAdmin = solicitante.getTipo() == TIPOUSUARIO.ADMIN;
        if (!esAdmin && !solicitante.getId().equals(idCliente)) {
            throw new ParametroInvalidoException("No tiene permiso para ver las reseñas de otro cliente.");
        }
        return reviewService.listarPorClienteDTO(idCliente);
    }

    /// Solo sobre una reserva propia y ya FINALIZADA (ver ReviewService).
    @PostMapping("/registrar")
    public ReviewResponseDTO registrar(@Valid @RequestBody ReviewRegistroDTO dto, Authentication auth) {
        return reviewService.crearDTO(dto, auth.getName());
    }

    /// No existe endpoint de modificacion: la reseña es inmodificable, solo se elimina.
    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication auth) {
        reviewService.borrarPorId(id, auth.getName());
        return "La reseña fue eliminada correctamente.";
    }
}
