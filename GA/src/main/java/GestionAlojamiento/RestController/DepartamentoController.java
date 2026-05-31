package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.DepartamentoModificarDTO;
import GestionAlojamiento.DTO.DepartamentoRegistroDTO;
import GestionAlojamiento.Model.Departamento;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Departamento")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @GetMapping("/listar")
    public List<Departamento> listar() {
        return departamentoService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Departamento mostrarPorId(@PathVariable Long id) {
        return departamentoService.obtenerPorId(id);
    }

    /// Permite ver todos los Departamentos registrados
    @GetMapping("/listar/propios")
    public List<Departamento> listarPropios() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return departamentoService.listarPorAnfitrion(auth.getName());
    }

    @PostMapping("/registrar")
    public Departamento registrar(@RequestBody DepartamentoRegistroDTO departamentoRegistroDTO) {
        return departamentoService.crear(departamentoRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Departamento actualizar(@RequestBody DepartamentoModificarDTO departamentoModificarDTO) {
        return departamentoService.actualizar(departamentoModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        departamentoService.borrarPorId(id);
    }


}
