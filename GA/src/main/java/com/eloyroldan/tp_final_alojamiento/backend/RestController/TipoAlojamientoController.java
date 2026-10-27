package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.TipoAlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReemplazoCatalogoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Service.TipoAlojamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/TipoAlojamiento")
@RequiredArgsConstructor
public class TipoAlojamientoController {

    private final TipoAlojamientoService tipoAlojamientoService;

    @GetMapping("/listar")
    public List<TipoAlojamientoResponseDTO> listar() {
        return tipoAlojamientoService.listarTodos().stream().map(TipoAlojamientoResponseDTO::desde).toList();
    }

    /// El admin fusiona un tipo duplicado/mal escrito dentro de otro.
    @PutMapping("/reemplazar")
    public void reemplazar(@Valid @RequestBody ReemplazoCatalogoDTO dto) {
        tipoAlojamientoService.reemplazar(dto.getIdOrigen(), dto.getIdDestino());
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        tipoAlojamientoService.borrarPorId(id);
    }
}
