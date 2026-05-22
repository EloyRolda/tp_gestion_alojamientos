package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReservaRegistroDTO;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Service.ReservaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserva")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/listar")
    public List<Reserva> listar() {
        return reservaService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Reserva mostrarPorId(@PathVariable Long id) {
        return reservaService.mostarPorId(id);
    }

    @PostMapping("/registrar")
    public Reserva registrar(ReservaRegistroDTO reservaRegistroDTO) {
        return reservaService.crear(reservaRegistroDTO);
    }

}
