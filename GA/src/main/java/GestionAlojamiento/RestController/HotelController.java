package GestionAlojamiento.RestController;


import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/Hotel")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/listar")
    public List<Hotel> listar() {
        return hotelService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Hotel mostrarPorId(@PathVariable Long id) {
        return hotelService.obtenerPorId(id);
    }

    @PostMapping("/registrar")
    public Hotel registrar(@RequestBody HotelRegistroDTO hotelRegistroDTO) {
        return hotelService.crear(hotelRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Hotel actualizar(@RequestBody HotelModificarDTO hotelModificarDTO){
        return hotelService.actualizar(hotelModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id){
        hotelService.borrarPorId(id);
    }

}
