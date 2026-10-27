package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.DireccionResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Service.DireccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Direccion")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @GetMapping("/listar")
    public List<DireccionResponseDTO> listar() {
        return direccionService.listarTodas().stream().map(DireccionResponseDTO::desde).toList();
    }

    @GetMapping("/mostrar/{id}")
    public DireccionResponseDTO mostrar(@PathVariable Long id) {
        return DireccionResponseDTO.desde(direccionService.obtenerPorId(id));
    }

    /// Para que un anfitrion encuentre una direccion ya cargada y la reutilice
    /// (ej: un predio con varias cabanias que comparten el mismo ingreso).
    @GetMapping("/buscar")
    public List<DireccionResponseDTO> buscar(@RequestParam String calle, @RequestParam Integer altura, @RequestParam String ciudad) {
        return direccionService.buscar(calle, altura, ciudad).stream().map(DireccionResponseDTO::desde).toList();
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        direccionService.borrarPorId(id);
    }
}
