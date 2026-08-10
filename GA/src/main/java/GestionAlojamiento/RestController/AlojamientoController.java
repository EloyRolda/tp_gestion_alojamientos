package GestionAlojamiento.RestController;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Service.AlojamientoService;
import GestionAlojamiento.Service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/// Listado/busqueda UNIFICADA de alojamientos (mezcla Casa/Departamento/Hotel),
/// gracias a la herencia JPA JOINED de Alojamiento. Es el endpoint que usa el
/// listado publico estilo Airbnb; los endpoints /Casa, /Departamento y /Hotel
/// siguen existiendo para el CRUD especifico de cada subtipo.
@RestController
@RequestMapping("/Alojamiento")
@RequiredArgsConstructor
public class AlojamientoController {

    private final AlojamientoService alojamientoService;
    private final GalleryService galleryService;

    @GetMapping("/listar")
    public List<Alojamiento> listarTodos() {
        return alojamientoService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Alojamiento mostrarPorId(@PathVariable Long id) {
        return alojamientoService.obtenerPorId(id);
    }

    /// Listado publico con filtros (todos opcionales): ciudad, capacidad minima,
    /// rango de precio y tipo (CASA/DEPARTAMENTO/HOTEL).
    @GetMapping("/buscar")
    public List<Alojamiento> buscar(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) String tipo
    ) {
        return alojamientoService.buscarPublico(ciudad, capacidadMinima, precioMin, precioMax, tipo);
    }

    /// El anfitrion publica su alojamiento (queda visible en /buscar) una vez que
    /// cumple el minimo de fotos exigido.
    @PatchMapping("/{id}/publicar")
    public void publicar(@PathVariable Long id, Authentication auth) {
        galleryService.publicar(id, auth.getName());
    }

    @PatchMapping("/{id}/despublicar")
    public void despublicar(@PathVariable Long id) {
        galleryService.despublicar(id);
    }
}
