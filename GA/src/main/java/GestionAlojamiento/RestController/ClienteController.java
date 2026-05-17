package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ClienteModificarDTO;
import GestionAlojamiento.DTO.ClienteRegistroDTO;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @GetMapping("/listar")
    public List<Cliente> listar() {
        return clienteService.listarClientes();
    }

    @GetMapping("/mostrar/{id}")
    public Cliente mostrar(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @DeleteMapping("/borrar/{id}")
    public void borrar(@PathVariable Long id) {
        clienteService.borrarPorId(id);
    }

    @PostMapping("/registrar")
    public Cliente registrar(@RequestBody ClienteRegistroDTO clienteRegistroDTO) {
        return clienteService.crear(clienteRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Cliente actualizar(@RequestBody ClienteModificarDTO clienteModificarDTO) {
        return clienteService.actualizar(clienteModificarDTO);
    }


}
