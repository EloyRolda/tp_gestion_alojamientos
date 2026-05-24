package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Repository.DireccionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DireccionService {
    private final DireccionRepository direccionRepository;

    @Transactional
    public Direccion crear(Direccion direccion) {
        //Normalizado a lowercase
        direccion.setPais(direccion.getPais().toLowerCase());
        direccion.setCalle(direccion.getCalle().toLowerCase());
        direccion.setProvincia(direccion.getProvincia().toLowerCase());
        //Fin normalizado
        return direccionRepository.save(direccion);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new IdNoEncontradoException("ID no encontrado en la base de datos: " + id);
        }
        direccionRepository.deleteById(id);
    }

    public Direccion obtenerPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("ID no encontrado en la base de datos: " + id));
    }
}