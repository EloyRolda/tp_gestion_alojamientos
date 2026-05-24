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

    /// Busca en la base de datos y devuelve el Objeto Direccion obtenido
    public Direccion obtenerPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("ID no encontrado en la base de datos: " + id));
    }

    /// Modifica el objeto Direccion SIN TOCAR LA BASE DE DATOS y devuelve el objeto modificado
    public Direccion modificarObjeto(Direccion direccion, Integer altura, String calle, String ciudad, String codigo_postal, String pais, String provincia) {
        if (altura != null) {
            direccion.setAltura(altura);
        }
        if (calle != null) {
            direccion.setCalle(calle);
        }
        if (ciudad != null) {
            direccion.setCiudad(ciudad);
        }
        if (codigo_postal != null) {
            direccion.setCiudad(ciudad);
        }
        if (pais != null) {
            direccion.setPais(pais);
        }
        if (provincia != null) {
            direccion.setProvincia(provincia);
        }
        return direccion;
    }
}