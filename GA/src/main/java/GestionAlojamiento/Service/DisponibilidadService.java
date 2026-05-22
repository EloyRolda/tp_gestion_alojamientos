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

    public Disponibilidad obtenerPorFecha(LocalDate fecha_inicio) {
        if (!disponibilidadRepository.existsByFecha(fecha_inicio)) {
            throw new RuntimeException("Error, fecha no encontrada.");
        }
        return disponibilidadRepository.findByFecha(fecha_inicio);
    }

    public List<Disponibilidad> listarPorDisponibilidad() {

        return disponibilidadRepository.findByDisponibleTrue();
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Disponibilidad crear(Disponibilidad disponibilidad) {
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

    @Transactional
    public Disponibilidad liberarFecha(LocalDate localDate) {
        Disponibilidad disponibilidad = obtenerPorFecha(localDate);
        disponibilidad.setDisponible(true);
        return disponibilidadRepository.save(disponibilidad);
    }

    @Transactional
    public Disponibilidad ocuparFecha(LocalDate localDate) {
        Disponibilidad disponibilidad = obtenerPorFecha(localDate);
        disponibilidad.setDisponible(false);
        return disponibilidadRepository.save(disponibilidad);
    }

    @Transactional
    public void cambiarDisponibleInicioAFin(LocalDate fecha_inicio, LocalDate fecha_fin) {
        List<Disponibilidad> rango = disponibilidadRepository.findByFechaBetween(fecha_inicio, fecha_fin);


        //Habilita disponibilidad
        if (fecha_inicio.isAfter(fecha_fin)) {
            rango.remove(obtenerPorFecha(fecha_fin));
            rango.stream().forEach(a -> a.setDisponible(true));
        }

        //Quita disponibilidad
        if (fecha_inicio.isAfter(fecha_fin)) {
            fecha_fin.plusDays(1);//Asi no analizamos la fecha final actual -Eloy
            if (rango.stream().anyMatch(disponibilidad -> !disponibilidad.isDisponible())) {
                throw new RuntimeException("Dentro del rango, hay un dia no disponible, para aumentar la fecha de reserva.");
            }
            rango.stream().forEach(a -> a.setDisponible(false));
        }
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