package GestionAlojamiento.RestController;


import GestionAlojamiento.DTO.AdministradorModificarDTO;
import GestionAlojamiento.DTO.AdministradorRegistroDTO;
import GestionAlojamiento.Model.Administrador;
import GestionAlojamiento.Service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/Administrador")

public class AdministradorController {

    private final AdministradorService administradorService;

    @GetMapping("/listar")
    public List<Administrador> listar() {
        return administradorService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Administrador mostrar(@PathVariable Long id) {
        return administradorService.obtenerPorId(id);
    }

    @PostMapping("/registrar")
    public Administrador registrar(@RequestBody AdministradorRegistroDTO administradorRegistroDTO) {
        return administradorService.crear(administradorRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Administrador actualizar(@RequestBody AdministradorModificarDTO administradorModificarDTO) {
        return administradorService.actualizar(administradorModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        administradorService.borrarPorId(id);
    }

}
