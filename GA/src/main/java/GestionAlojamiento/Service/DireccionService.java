package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Repository.DireccionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DireccionService {
    private final DireccionRepository direccionRepository;

    //------------------------ LISTAR POR ------------------------
    public List<Direccion> listarDirecciones() {
        return direccionRepository.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
    }

    public List<Direccion> listarPorPais(String paisBusqueda) {
        return direccionRepository.findAll().stream()
                .filter(direccion -> direccion.getPais().equalsIgnoreCase(paisBusqueda)).collect(Collectors.toList());
    }

    public List<Direccion> listarPorProvincia(String provinciaBusqueda) {
        return direccionRepository.findAll().stream()
                .filter(direccion -> direccion.getProvincia().equalsIgnoreCase(provinciaBusqueda)).collect(Collectors.toList());
    }

    public List<Direccion> listarPorCiudad(String ciudadBusqueda) {
        return direccionRepository.findAll().stream()
                .filter(direccion -> direccion.getCiudad().equalsIgnoreCase(ciudadBusqueda)).collect(Collectors.toList());
    }

    public List<Direccion> listarPorCP(String cpBusqueda) {
        return direccionRepository.findAll().stream()
                .filter(direccion -> direccion.getCodigo_postal().equalsIgnoreCase(cpBusqueda)).collect(Collectors.toList());
    }

    public List<Direccion> listarPorCalle(String calleBusqueda) {
        return direccionRepository.findAll().stream()
                .filter(direccion -> direccion.getCalle().equalsIgnoreCase(calleBusqueda)).collect(Collectors.toList());
    }

    //Si no encuentra por ID lanza exeption
    public Direccion obtenerPorID(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encuentra, el id no concuerda con la base de datos"));
    }
    //------------------------ GUARDAR/BORRAR ------------------------

    @Transactional
    public Direccion guardar(Direccion direccion) {
        //normalizado a lowercase
        direccion.setPais(direccion.getPais().toLowerCase());
        direccion.setCalle(direccion.getCalle().toLowerCase());
        direccion.setProvincia(direccion.getProvincia().toLowerCase());
        //Fin normalizado
        return direccionRepository.save(direccion);
    }

    //Transactional es igual a Transaction en sql/ rollbacks y commits
    @Transactional
    public void borrarPorId(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, el id no concuerda con la base de datos");//Como hay un transactional, la funcion se iria"para atras"
        }
        direccionRepository.deleteById(id);
    }

    //------------------------ MODIFICAR ------------------------
    @Transactional
    public Direccion actualizarPais(Long id, String nuevoPais) {
        Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        direccion.setPais(nuevoPais.toLowerCase());
        return direccionRepository.save(direccion);
    }

    @Transactional
    public Direccion actualizarProvincia(Long id, String nuevaProvincia) {
        Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        direccion.setProvincia(nuevaProvincia.toLowerCase());
        return direccionRepository.save(direccion);
    }

    @Transactional
    public Direccion actualizarCiudad(Long id, String nuevaCiudad) {
        Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        direccion.setCiudad(nuevaCiudad.toLowerCase());
        return direccionRepository.save(direccion);
    }

    @Transactional
    public Direccion actualizarCP(Long id, String nuevoCP) {
        Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        direccion.setCodigo_postal(nuevoCP.toLowerCase());
        return direccionRepository.save(direccion);
    }

    @Transactional
    public Direccion actualizarCalle(Long id, String nuevaCalle) {
        Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        direccion.setCalle(nuevaCalle.toLowerCase());
        return direccionRepository.save(direccion);
    }

    public Direccion actualizarAltura(Long id, Integer nuevaAltura) {
        Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        direccion.setAltura(nuevaAltura);
        return direccionRepository.save(direccion);
    }

}
