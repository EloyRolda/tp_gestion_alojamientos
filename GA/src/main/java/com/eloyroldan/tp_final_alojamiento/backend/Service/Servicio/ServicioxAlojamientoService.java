package com.eloyroldan.tp_final_alojamiento.backend.Service.Servicio;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ServicioxAlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.Servicio;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio.ServicioxAlojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.Servicio.ServicioxAlojamientoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// Administra la asignacion de Servicios a un Alojamiento (tabla puente). Es el unico
/// que toca ServicioxAlojamientoRepository: ServicioService la usa a traves de este.
@Service
@RequiredArgsConstructor
public class ServicioxAlojamientoService {

    private final ServicioxAlojamientoRepository servicioxAlojamientoRepository;
    private final ServicioService servicioService;

    public List<ServicioxAlojamiento> listarPorAlojamiento(Long idAlojamiento) {
        return servicioxAlojamientoRepository.findByAlojamiento_IdAlojamiento(idAlojamiento);
    }

    @Transactional
    public List<ServicioxAlojamientoResponseDTO> listarPorAlojamientoDTO(Long idAlojamiento) {
        return listarPorAlojamiento(idAlojamiento).stream().map(ServicioxAlojamientoResponseDTO::desde).toList();
    }

    /// Asigna una lista de servicios (por nombre) a un alojamiento. Cada nombre se
    /// resuelve/crea en el catalogo (ServicioService.obtenerOcrear) y, si el alojamiento
    /// todavia no lo tenia asignado, se crea el vinculo.
    @Transactional
    public void asignarServicios(Alojamiento alojamiento, List<String> nombresServicios) {
        if (nombresServicios == null) {
            return;
        }
        for (String nombre : nombresServicios) {
            Servicio servicio = servicioService.obtenerOcrear(nombre);
            boolean yaAsignado = servicioxAlojamientoRepository.existsByAlojamiento_IdAlojamientoAndServicio_IdServicio(
                    alojamiento.getIdAlojamiento(), servicio.getIdServicio());
            if (!yaAsignado) {
                servicioxAlojamientoRepository.save(
                        ServicioxAlojamiento.builder()
                                .alojamiento(alojamiento)
                                .servicio(servicio)
                                .build());
            }
        }
    }

    @Transactional
    public void quitarServicio(Long idAlojamiento, Long idServicio) {
        ServicioxAlojamiento vinculo = servicioxAlojamientoRepository
                .findByAlojamiento_IdAlojamiento(idAlojamiento).stream()
                .filter(sxa -> sxa.getServicio().getIdServicio().equals(idServicio))
                .findFirst()
                .orElseThrow(() -> new IdNoEncontradoException("El alojamiento no tiene asignado ese servicio."));
        servicioxAlojamientoRepository.delete(vinculo);
    }

    @Transactional
    public void borrarPorAlojamientoId(Long idAlojamiento) {
        servicioxAlojamientoRepository.deleteByAlojamiento_IdAlojamiento(idAlojamiento);
    }

    //---------------------------------------- USO INTERNO (ServicioService) ----------------------------------------

    public boolean tieneAsignaciones(Long idServicio) {
        return !servicioxAlojamientoRepository.findByServicio_IdServicio(idServicio).isEmpty();
    }

    /// El admin fusiona "origen" dentro de "destino": cada vinculo que apuntaba a
    /// origen pasa a apuntar a destino, salvo que el alojamiento ya tuviera destino
    /// asignado (en ese caso se descarta el duplicado, por la unique constraint).
    @Transactional
    public void fusionarAsignaciones(Long idServicioOrigen, Servicio destino) {
        List<ServicioxAlojamiento> asignaciones = servicioxAlojamientoRepository.findByServicio_IdServicio(idServicioOrigen);
        for (ServicioxAlojamiento asignacion : asignaciones) {
            boolean yaTieneDestino = servicioxAlojamientoRepository.existsByAlojamiento_IdAlojamientoAndServicio_IdServicio(
                    asignacion.getAlojamiento().getIdAlojamiento(), destino.getIdServicio());
            if (yaTieneDestino) {
                servicioxAlojamientoRepository.delete(asignacion);
            } else {
                asignacion.setServicio(destino);
                servicioxAlojamientoRepository.save(asignacion);
            }
        }
    }
}
