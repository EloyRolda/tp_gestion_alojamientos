/// Endpoints de reportes (RF44): alta de un reporte, catalogo fijo de motivos y,
/// para el administrador, listado y resolucion de los casos reportados (SCRUM-79).
/// El autor sale del JWT, nunca del body.
package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReporteRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ResolverReporteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MotivoReporteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReporteAdminDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReporteResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReporteService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /// Listado de moderacion para el administrador. Sin parametro (o ESTADOREPORTE
    /// null) trae todos; con PENDIENTE o RESUELTO filtra por ese estado.
    @GetMapping
    public ResponseEntity<List<ReporteAdminDTO>> listarAdmin(@RequestParam(required = false) ESTADOREPORTE estado) {
        return ResponseEntity.ok(reporteService.listarAdmin(estado));
    }

    /// Resuelve un reporte pendiente con la accion elegida por el admin.
    @PostMapping("/{id}/resolver")
    public ResponseEntity<ReporteAdminDTO> resolver(@PathVariable Long id,
                                                    @Valid @RequestBody ResolverReporteDTO dto) {
        return ResponseEntity.ok(reporteService.resolver(id, dto.getAccion(), dto.getMotivo(), dto.getDetalle()));
    }
}
