package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Departamento;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final AlojamientoRepository alojamientoRepository;

    //------------------------ LISTAR ------------------------
    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Departamento obtenerPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, id no encontrado en la base de datos."));
    }

    public List<Departamento> listarPorTiene_Ascensor() {
        return departamentoRepository.findByTieneAscensorTrue();
    }

    public List<Departamento> listarPorExpensas_Incluidas() {
        return departamentoRepository.findByExpensasIncluidasTrue();
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Departamento crear(Long id_alojamiento, Departamento departamento) {
        Alojamiento alojamiento = alojamientoRepository.findById(id_alojamiento).orElseThrow(() -> new RuntimeException("Error, id de alojamiento invalido o no se encuentra en la base de datos"));
        departamento.setAlojamiento(alojamiento);
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void borrarPorId(Long id_departamento) {
        if (!departamentoRepository.existsById(id_departamento)) {
            throw new RuntimeException("Error, el id alojamiento no se encuentra en la base de datos.");
        }
        departamentoRepository.deleteById(id_departamento);
    }

    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Departamento actualizarCantPisos(Long id, Integer nuevoPiso) {
        Departamento departamento = departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, id no encontrado en la base de datos."));
        departamento.setPiso(nuevoPiso);
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public Departamento actualizarTiene_ascensor(Long id, boolean nuevoEstado) {
        Departamento departamento = departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, id no encontrado en la base de datos."));
        departamento.setTieneAscensor(nuevoEstado);
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public Departamento actualizarExpensas_incluidas(Long id, boolean nuevoEstado) {
        Departamento departamento = departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, id no encontrado en la base de datos."));
        departamento.setExpensasIncluidas(nuevoEstado);
        return departamentoRepository.save(departamento);
    }
}
