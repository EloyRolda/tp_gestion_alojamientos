/// Endpoints de reportes (RF44): alta de un reporte y catalogo fijo de motivos.
/// El autor sale del JWT, nunca del body.
package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReporteRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MotivoReporteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReporteResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReporteService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Reporte")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crear(@Valid @RequestBody ReporteRegistroDTO dto,
                                                    Authentication auth) {
        Usuario autor = usuarioService.obtenerPorIdentificador(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crearDTO(dto, autor));
    }

    /// Catalogo fijo: el formulario lo pide una sola vez y filtra por categoria.
    @GetMapping("/motivos")
    public ResponseEntity<List<MotivoReporteDTO>> motivos() {
        return ResponseEntity.ok(reporteService.listarMotivos());
    }
}
