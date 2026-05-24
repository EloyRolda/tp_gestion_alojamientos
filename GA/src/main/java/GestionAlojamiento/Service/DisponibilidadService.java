package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
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

    public List<Disponibilidad> listarTodos() {
        return disponibilidadRepository.findAll();
    }

    public Disponibilidad obtenerPorId(Long id) {
        return disponibilidadRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("ID no encontrado en la base de datos."));
    }

    @Transactional
    public Disponibilidad crear(Disponibilidad disponibilidad) {return disponibilidadRepository.save(disponibilidad);}

    @Transactional
    public void borrarPorId(Long id_disponibilidad) {
        if (!disponibilidadRepository.existsById(id_disponibilidad)) {
            throw new IdNoEncontradoException("ID no encontrado en la base de datos.");
        }
        disponibilidadRepository.deleteById(id_disponibilidad);
    }

    @Transactional
    public void liberarFechas(LocalDate fecha_inicio, LocalDate fecha_fin) {
        List<Disponibilidad> rango = disponibilidadRepository.findByFechaBetween(fecha_inicio, fecha_fin);
        rango.stream().forEach(a -> a.setDisponible(true));
    }

    @Transactional
    public void ocuparFechas(LocalDate fecha_inicio, LocalDate fecha_fin) {
        List<Disponibilidad> rango = disponibilidadRepository.findByFechaBetween(fecha_inicio, fecha_fin);

        if (rango.stream().anyMatch(a -> !a.isDisponible())) {
            throw new RuntimeException("Uno o varios dias estan ocupados");
        }
    }

    @Transactional
    public Disponibilidad crearOActualizar(Disponibilidad disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

}