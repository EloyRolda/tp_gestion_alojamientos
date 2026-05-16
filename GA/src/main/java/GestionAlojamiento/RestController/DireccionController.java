package GestionAlojamiento.RestController;

import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping("/lista")
    public List<Direccion> mostrar() {
        return direccionService.listarDirecciones();
    }


    @PostMapping("/guardar")
    public Direccion guardar(@RequestBody Direccion direccion) {

        return direccionService.crear(direccion);
    }

    @DeleteMapping("/borrar/{id}")
    public void eliminar(@PathVariable Long id){
        direccionService.borrarPorId(id);
    }


    @GetMapping("/saludar")
    public String saludar() {
        return "Hola";
    }

}