package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Servicio;
import GestionAlojamiento.Repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final ServicioRepository servicioRepository;


    public Long obtenerIdUsuario(HttpSession httpSession) {
        Long id = (Long) httpSession.getAttribute("id_usuario");

        return id;
    }

    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el servicio no existe en la base de datos: " + id));
    }

    @Transactional
    ///Crea un usuario y lo guarda en la base de datos
    public Servicio crear(Servicio servicio) {
        return servicioRepository.save(servicio);
    }


    /// Modifica el objeto SERVICIO ORIGNAL con los parametros cargados en CAMBIO SIN TOCAR LA BASE DE DATOS y devuelve el objeto modificado
    public Servicio modificarObjeto(Servicio original, Servicio cambios) {

        if (cambios.getTieneCocina() != null) {
            original.setTieneCocina(cambios.getTieneCocina());
        }

        if (cambios.getTieneEstacionamiento() != null) {
            original.setTieneEstacionamiento(cambios.getTieneEstacionamiento());
        }

        if (cambios.getTieneLavarropa() != null) {
            original.setTieneLavarropa(cambios.getTieneLavarropa());
        }

        if (cambios.getTieneWifi() != null) {
            original.setTieneWifi(cambios.getTieneWifi());
        }

        return original;
    }

}