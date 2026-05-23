package GestionAlojamiento.RestController;


import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Service.HotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/listar")
    public List<Hotel> listar() {
        return hotelService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Hotel mostrarPorId(@PathVariable Long id) {
        return hotelService.obtenerPorId(id);
    }

    @PostMapping("/Registrar")
    public Hotel registrar(@RequestBody HotelRegistroDTO hotelRegistroDTO) {
        return hotelService.crear(hotelRegistroDTO);        //Terminar
    }

    @PutMapping("/actualizar")
    public Hotel actualizar(@RequestBody HotelModificarDTO hotelModificarDTO){
        return hotelService.actualizar(hotelModificarDTO);  //Terminar
    }

    @DeleteMapping("/borrar/{id}")
    public void borrarPorId(@PathVariable Long id){
        hotelService.borrarPorId(id);
    }

}
