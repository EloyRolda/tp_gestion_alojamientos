package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Repository.CasaRepository;
import GestionAlojamiento.Service.CasaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Casa")
public class CasaController {

    private final CasaService casaService;

    public CasaController(CasaService casaService) {
        this.casaService = casaService;
    }

    @GetMapping("/listar")
    public List<Casa> listar() {
        return casaService.listar();
    }

    @GetMapping("/mostrar/{id}")
    public Casa mostrarPorId(@PathVariable Long id) {
        return casaService.obtenerPorId(id);
    }

    @PostMapping("/registrar")
    public Casa registrar(@RequestBody CasaRegistroDTO casaRegistroDTO) {
        return casaService.crear(casaRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Casa actualizar(@RequestBody CasaModificarDTO casaModificarDTO) {
        return casaService.modificar(casaModificarDTO);
    }

    @DeleteMapping("/borrar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        casaService.borrarPorId(id);
    }

}
