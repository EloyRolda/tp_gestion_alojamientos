package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.TipoAlojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.TipoAlojamientoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoAlojamientoService {

    private final TipoAlojamientoRepository tipoAlojamientoRepository;

    /// AlojamientoService depende de TipoAlojamientoService (para resolver/crear el
    /// tipo), asi que esta referencia de vuelta se inyecta con @Lazy para evitar el ciclo.
    @Lazy
    private final AlojamientoService alojamientoService;

    public List<TipoAlojamiento> listarTodos() {
        return tipoAlojamientoRepository.findAll();
    }

    public TipoAlojamiento obtenerPorId(Long id) {
        return tipoAlojamientoRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Tipo de alojamiento no encontrado: " + id));
    }

    /// Normaliza el nombre (Primera mayuscula, resto minuscula), reutiliza si ya existe
    /// (sin distinguir mayusculas/minusculas) y lo crea en el catalogo si no.
    @Transactional
    public TipoAlojamiento obtenerOcrear(String nombre) {
        String normalizado = normalizar(nombre);
        return tipoAlojamientoRepository.findByNombreTipoIgnoreCase(normalizado)
                .orElseGet(() -> tipoAlojamientoRepository.save(
                        TipoAlojamiento.builder().nombreTipo(normalizado).build()));
    }

    /// El admin fusiona un tipo duplicado/mal escrito (origen) dentro de otro (destino):
    /// todos los alojamientos que usaban "origen" pasan a usar "destino", y "origen" se elimina.
    @Transactional
    public void reemplazar(Long idOrigen, Long idDestino) {
        if (idOrigen.equals(idDestino)) {
            throw new ParametroInvalidoException("El origen y el destino no pueden ser el mismo tipo.");
        }
        TipoAlojamiento origen = obtenerPorId(idOrigen);
        TipoAlojamiento destino = obtenerPorId(idDestino);

        alojamientoService.reasignarTipoAlojamiento(origen.getIdTipoAlojamiento(), destino);
        tipoAlojamientoRepository.delete(origen);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!alojamientoService.listarPorTipoAlojamiento(id).isEmpty()) {
            throw new ParametroInvalidoException("No se puede borrar: hay alojamientos usando este tipo. Utilice reemplazar.");
        }
        if (!tipoAlojamientoRepository.existsById(id)) {
            throw new IdNoEncontradoException("Tipo de alojamiento no encontrado: " + id);
        }
        tipoAlojamientoRepository.deleteById(id);
    }

    private String normalizar(String nombre) {
        String limpio = nombre.trim().replaceAll("\\s+", " ").toLowerCase();
        if (limpio.isEmpty()) {
            throw new ParametroInvalidoException("El nombre del tipo de alojamiento no puede estar vacio.");
        }
        return Character.toUpperCase(limpio.charAt(0)) + limpio.substring(1);
    }
}
