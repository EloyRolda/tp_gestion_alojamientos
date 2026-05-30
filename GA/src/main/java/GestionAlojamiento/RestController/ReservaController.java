package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReservaModificarDTO;
import GestionAlojamiento.DTO.ReservaRegistroDTO;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/Reserva")
public class ReservaController {
    private final ReservaService reservaService;

    @GetMapping("/listar")
    public List<Reserva> listar() {
        return reservaService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Reserva mostrarPorId(@PathVariable Long id) {
        return reservaService.mostrarPorId(id);
    }

    @PostMapping("/registrar")
    public Reserva registrar(@RequestBody ReservaRegistroDTO reservaRegistroDTO) {
        return reservaService.crear(reservaRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Reserva modificar(@RequestBody ReservaModificarDTO reservaModificarDTO) {
        return reservaService.actualizar(reservaModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        reservaService.borrarPorId(id);
    }

}
