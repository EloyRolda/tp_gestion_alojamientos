package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ServicioResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.NombreCatalogoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReemplazoCatalogoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Servicio.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Servicio")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping("/listar")
    public List<ServicioResponseDTO> listar() {
        return servicioService.listarTodos().stream().map(ServicioResponseDTO::desde).toList();
    }

    /// Alta manual del admin (RF10) -- el catalogo tambien se completa solo
    /// cuando un anfitrion publica con un servicio nuevo (obtenerOcrear), pero
    /// esto permite cargarlo de antemano.
    @PostMapping("/crear")
    public ServicioResponseDTO crear(@Valid @RequestBody NombreCatalogoDTO dto) {
        return ServicioResponseDTO.desde(servicioService.crear(dto.getNombre()));
    }

    /// Renombrar en el lugar (RF10) -- distinto de "reemplazar", que fusiona dos existentes.
    @PutMapping("/{id}")
    public ServicioResponseDTO renombrar(@PathVariable Long id, @Valid @RequestBody NombreCatalogoDTO dto) {
        return ServicioResponseDTO.desde(servicioService.renombrar(id, dto.getNombre()));
    }

    /// El admin fusiona un servicio duplicado/mal escrito dentro de otro
    /// (ej: "Labarropa" -> "Lavarropa"), reasignando todos los alojamientos.
    @PutMapping("/reemplazar")
    public void reemplazar(@Valid @RequestBody ReemplazoCatalogoDTO dto) {
        servicioService.reemplazar(dto.getIdOrigen(), dto.getIdDestino());
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        servicioService.borrarPorId(id);
    }
}
