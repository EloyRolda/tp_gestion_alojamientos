package com.eloyroldan.tp_final_alojamiento.backend.Service.Servicio;

import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.Servicio;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.Servicio.ServicioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/// Catalogo compartido de servicios/comodidades (WiFi, Pileta, Lavarropas, etc).
@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    /// ServicioxAlojamientoService depende de ServicioService (obtenerOcrear), asi que
    /// esta referencia de vuelta (para el merge/borrado) se inyecta con @Lazy.
    @Lazy
    private final ServicioxAlojamientoService servicioxAlojamientoService;

    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el servicio no existe en la base de datos: " + id));
    }

    /// Normaliza el nombre (Primera mayuscula, resto minuscula). Si ya existe (sin
    /// distinguir mayusculas/minusculas) lo reutiliza; si no, lo crea en el catalogo.
    @Transactional
    public Servicio obtenerOcrear(String nombre) {
        String normalizado = normalizar(nombre);
        return servicioRepository.findByNombreServicioIgnoreCase(normalizado)
                .orElseGet(() -> servicioRepository.save(
                        Servicio.builder().nombreServicio(normalizado).build()));
    }

    /// El admin fusiona un servicio duplicado/mal escrito (origen) dentro de otro (destino):
    /// reasigna todos los alojamientos que lo tenian y elimina el duplicado.
    @Transactional
    public void reemplazar(Long idOrigen, Long idDestino) {
        if (idOrigen.equals(idDestino)) {
            throw new ParametroInvalidoException("El origen y el destino no pueden ser el mismo servicio.");
        }
        Servicio origen = obtenerPorId(idOrigen);
        Servicio destino = obtenerPorId(idDestino);

        servicioxAlojamientoService.fusionarAsignaciones(origen.getIdServicio(), destino);
        servicioRepository.delete(origen);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (servicioxAlojamientoService.tieneAsignaciones(id)) {
            throw new ParametroInvalidoException("No se puede borrar: hay alojamientos usando este servicio. Utilice reemplazar.");
        }
        if (!servicioRepository.existsById(id)) {
            throw new IdNoEncontradoException("Error, el servicio no existe en la base de datos: " + id);
        }
        servicioRepository.deleteById(id);
    }

    private String normalizar(String nombre) {
        String limpio = nombre.trim().replaceAll("\\s+", " ").toLowerCase();
        if (limpio.isEmpty()) {
            throw new ParametroInvalidoException("El nombre del servicio no puede estar vacio.");
        }
        return Character.toUpperCase(limpio.charAt(0)) + limpio.substring(1);
    }
}
