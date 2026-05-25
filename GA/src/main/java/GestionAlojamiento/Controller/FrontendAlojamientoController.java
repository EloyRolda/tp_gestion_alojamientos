package GestionAlojamiento.Controller;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Service.AlojamientoService;
import GestionAlojamiento.Service.CasaService;
import GestionAlojamiento.Service.DepartamentoService;
import GestionAlojamiento.Service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FrontendAlojamientoController {

    private final AlojamientoService alojamientoService;
    private final CasaService casaService;
    private final DepartamentoService departamentoService;
    private final HotelService hotelService;

    @GetMapping("/alojamientos")
    public String listarAlojamientos(Model model) {
        model.addAttribute("alojamientos", alojamientoService.listarActivos());
        return "frontend/alojamientos/listado";
    }

    @GetMapping("/alojamientos/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        Alojamiento alojamiento = alojamientoService.obtenerPorId(id);

        if (!Boolean.TRUE.equals(alojamiento.getActivo())) {
            return "redirect:/alojamientos";
        }

        model.addAttribute("alojamiento", alojamiento);
        cargarDatosPropios(alojamiento, model);

        return "frontend/alojamientos/detalle";
    }

    @GetMapping("/reservas/nueva")
    public String mostrarFormularioReservaPreparado(@RequestParam Long alojamientoId, Model model) {
        model.addAttribute("alojamiento", alojamientoService.obtenerPorId(alojamientoId));
        return "frontend/reservas/formulario";
    }

    private void cargarDatosPropios(Alojamiento alojamiento, Model model) {
        switch (alojamiento.getTipoInmueble()) {
            case CASA -> model.addAttribute("casa", casaService.obtenerPorId(alojamiento.getId()));
            case DEPARTAMENTO -> model.addAttribute("departamento", departamentoService.obtenerPorId(alojamiento.getId()));
            case HOTEL -> model.addAttribute("hotel", hotelService.obtenerPorId(alojamiento.getId()));
        }
    }
}
