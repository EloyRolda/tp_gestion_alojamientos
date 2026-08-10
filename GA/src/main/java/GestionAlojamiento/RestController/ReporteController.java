package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReporteRegistroDTO;
import GestionAlojamiento.Model.Enums.EstadoReporte;
import GestionAlojamiento.Model.Reporte;
import GestionAlojamiento.Service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Reporte")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping("/registrar")
    public Reporte crear(@RequestBody @Valid ReporteRegistroDTO dto, Authentication auth) {
        return reporteService.crear(dto, auth.getName());
    }

    /// Solo admin (ver SecurityConfig).
    @GetMapping("/listar")
    public List<Reporte> listar() {
        return reporteService.listarTodos();
    }

    @GetMapping("/listar/pendientes")
    public List<Reporte> listarPendientes() {
        return reporteService.listarPorEstado(EstadoReporte.PENDIENTE);
    }

    @PatchMapping("/{id}/estado")
    public Reporte cambiarEstado(@PathVariable Long id, @RequestParam EstadoReporte nuevoEstado) {
        return reporteService.cambiarEstado(id, nuevoEstado);
    }
}
