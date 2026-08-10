package GestionAlojamiento.RestController;

import GestionAlojamiento.Model.Log;
import GestionAlojamiento.Service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Auditoria: solo accesible por ADMIN (ver SecurityConfig).
@RestController
@RequestMapping("/Log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/listar")
    public List<Log> listar() {
        return logService.listarTodos();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Log> listarPorUsuario(@PathVariable Long idUsuario) {
        return logService.listarPorUsuario(idUsuario);
    }
}
