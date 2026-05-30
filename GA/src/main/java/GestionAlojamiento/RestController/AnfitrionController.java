package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.AnfitrionModificarDTO;
import GestionAlojamiento.DTO.AnfitrionRegistroDTO;
import GestionAlojamiento.Model.Anfitrion;
import GestionAlojamiento.Service.AnfitrionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/Anfitrion")
public class AnfitrionController {
    private final AnfitrionService anfitrionService;

    @GetMapping("/listar")
    public List<Anfitrion> listar() {
        return anfitrionService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Anfitrion mostrarPorId(@PathVariable Long id) {
        return anfitrionService.obtenerPorId(id);
    }

    @PostMapping("/registrar")
    public Anfitrion registro(@RequestBody AnfitrionRegistroDTO anfitrionRegistroDTO) {
        return anfitrionService.crear(anfitrionRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Anfitrion actualizar(@RequestBody AnfitrionModificarDTO anfitrionModificarDTO) {
        return anfitrionService.actualizar(anfitrionModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        anfitrionService.borrarPorId(id);
    }


}
