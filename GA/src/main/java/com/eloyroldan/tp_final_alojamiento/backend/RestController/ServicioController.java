package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ServicioResponseDTO;
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
