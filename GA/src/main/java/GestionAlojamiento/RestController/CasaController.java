package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Service.CasaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/Casa")
public class CasaController {

    private final CasaService casaService;

    @GetMapping("/listar")
    public List<Casa> listar() {
        return casaService.listar();
    }

    @GetMapping("/mostrar/{id}")
    public Casa mostrarPorId(@PathVariable Long id) {
        return casaService.obtenerPorId(id);
    }

    /// Permite ver todos los hoteles registrados
    @GetMapping("/listar/propios")
    public List<Casa> listarPropios() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return casaService.listarPorAnfitrion(auth.getName());
    }

    @PostMapping("/registrar")
    public Casa registrar(@RequestBody CasaRegistroDTO casaRegistroDTO) {
        return casaService.crear(casaRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Casa actualizar(@RequestBody CasaModificarDTO casaModificarDTO) {
        return casaService.modificar(casaModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        casaService.borrarPorId(id);
    }


}
