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

    ///Obtiene un OBJETO DIRECCION y lo crea en la base de datos
    @Transactional
    public Direccion crear(Direccion direccion) {
        //Normalizado a lowercase
        direccion.setPais(direccion.getPais().toLowerCase());
        direccion.setCalle(direccion.getCalle().toLowerCase());
        direccion.setProvincia(direccion.getProvincia().toLowerCase());
        //Fin normalizado
        return direccionRepository.save(direccion);
    }
    ///Obtiene un ID direccion y lo elimina de la base de datos
    @Transactional
    public void borrarPorId(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new IdNoEncontradoException("ID no encontrado en la base de datos: " + id);
        }
        direccionRepository.deleteById(id);
    }

    /// Busca en la base de datos y devuelve el Objeto Direccion obtenido
    public Direccion obtenerPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("ID no encontrado en la base de datos: " + id));
    }

    /// Modifica el objeto Direccion ORIGNAL con los parametros cargados en CAMBIO SIN TOCAR LA BASE DE DATOS y devuelve el objeto modificado
    public Direccion modificarObjeto(Direccion original, Direccion cambios) {
        if (cambios.getAltura() != null) {
            original.setAltura(cambios.getAltura());
        }
        if (cambios.getCalle() != null) {
            original.setCalle(cambios.getCalle());
        }
        if (cambios.getCiudad() != null) {
            original.setCiudad(cambios.getCiudad());
        }
        if (cambios.getCodigoPostal() != null) {
            original.setCodigoPostal(cambios.getCodigoPostal());
        }
        if (cambios.getPais() != null) {
            original.setPais(cambios.getPais());
        }
        if (cambios.getProvincia() != null) {
            original.setProvincia(cambios.getProvincia());
        }
        return original;
    }
}