package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Disponibilidad;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.DisponibilidadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final AlojamientoRepository alojamientoRepository;

    //------------------------ LISTAR ------------------------
    public List<Disponibilidad> listarTodos() {
        return disponibilidadRepository.findAll();
    }

    public Disponibilidad obtenerPorId(Long id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, id disponibilidad no encontrado."));
    }

    public List<Disponibilidad> listarPorDisponibilidad() {
        return disponibilidadRepository.findByDisponible();
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Disponibilidad crear(Long id_alojamiento, Disponibilidad disponibilidad) {
        Alojamiento alojamiento = alojamientoRepository.findById(id_alojamiento)
                .orElseThrow(() -> new RuntimeException("Error, id de alojamiento no encontrado."));

        disponibilidad.setAlojamiento(alojamiento);
        return disponibilidadRepository.save(disponibilidad);
    }

    @Transactional
    public void borrarPorId(Long id_disponibilidad) {
        if (!disponibilidadRepository.existsById(id_disponibilidad)) {
            throw new RuntimeException("Error, el id de disponibilidad no existe.");
        }
        disponibilidadRepository.deleteById(id_disponibilidad);
    }

    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Disponibilidad actualizarEstadoDisponibilidad(Long id, boolean nuevoEstado) {
        Disponibilidad disponibilidad = obtenerPorId(id);
        disponibilidad.setDisponible(nuevoEstado);
        return disponibilidadRepository.save(disponibilidad);
    }

    @Transactional
    public Disponibilidad actualizarFecha(Long id, LocalDate nuevaFecha) {
        Disponibilidad disponibilidad = obtenerPorId(id);
        disponibilidad.setFecha(nuevaFecha);
        return disponibilidadRepository.save(disponibilidad);
    }
}