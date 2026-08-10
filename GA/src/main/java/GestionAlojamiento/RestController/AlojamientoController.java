package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.AlojamientoModificarDTO;
import GestionAlojamiento.DTO.AlojamientoRegistroDTO;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Gallery;
import GestionAlojamiento.Service.AlojamientoService;
import GestionAlojamiento.Service.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/// Controller UNICO para los 3 tipos de alojamiento (reemplaza a los viejos
/// CasaController/DepartamentoController/HotelController). Alojamiento paso a
/// ser una tabla unica (ver Alojamiento.java) en vez de una jerarquia de
/// herencia con una tabla por tipo, asi que ya no hace falta un controller
/// por subtipo: el "tipo" es solo un campo mas del alta/edicion.
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

    @GetMapping("/listar/propios")
    public List<Alojamiento> listarPropios(Authentication auth) {
        return alojamientoService.listarPorAnfitrion(auth.getName());
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

    /// Crea el alojamiento y su galeria vacia en el mismo paso (se orquesta aca,
    /// no dentro del Service, para no crear una dependencia circular con GalleryService).
    @PostMapping("/registrar")
    public Alojamiento registrar(@Valid @RequestBody AlojamientoRegistroDTO dto) {
        Alojamiento alojamiento = alojamientoService.crear(dto);
        Gallery gallery = new Gallery(null, dto.getTitulo(), alojamiento);
        galleryService.createGallery(gallery);
        return alojamiento;
    }

    @PutMapping("/actualizar")
    public Alojamiento actualizar(@Valid @RequestBody AlojamientoModificarDTO dto, Authentication auth) {
        return alojamientoService.modificar(dto, auth.getName());
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id, Authentication auth) {
        alojamientoService.desactivar(id, auth.getName());
    }

    /// El anfitrion (o el admin) publica el alojamiento (queda visible en /buscar)
    /// una vez que cumple el minimo de fotos exigido.
    @PatchMapping("/{id}/publicar")
    public void publicar(@PathVariable Long id, Authentication auth) {
        galleryService.publicar(id, auth.getName());
    }

    @PatchMapping("/{id}/despublicar")
    public void despublicar(@PathVariable Long id, Authentication auth) {
        galleryService.despublicar(id, auth.getName());
    }

    /// El anfitrion (o el admin) reactiva un alojamiento que estaba dado de baja.
    /// Queda inactivo->activo pero SIN publicar: hay que volver a publicarlo a mano
    /// (asi no reaparece de golpe en el listado publico sin que el anfitrion lo revise).
    @PatchMapping("/{id}/reactivar")
    public void reactivar(@PathVariable Long id, Authentication auth) {
        alojamientoService.reactivar(id, auth.getName());
    }
}
