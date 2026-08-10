package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.EstadisticasAnfitrionDTO;
import GestionAlojamiento.DTO.EstadisticasClienteDTO;
import GestionAlojamiento.Service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/// Seccion de analisis de datos: ingresos, historial y demas metricas para
/// anfitrion y cliente (Data Analysis).
@RestController
@RequestMapping("/Estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @GetMapping("/anfitrion")
    public EstadisticasAnfitrionDTO estadisticasAnfitrion(Authentication auth) {
        return estadisticasService.estadisticasAnfitrion(auth.getName());
    }

    @GetMapping("/cliente")
    public EstadisticasClienteDTO estadisticasCliente(Authentication auth) {
        return estadisticasService.estadisticasCliente(auth.getName());
    }
}
