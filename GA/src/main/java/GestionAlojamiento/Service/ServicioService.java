package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Servicio;
import GestionAlojamiento.Repository.ServicioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final ServicioRepository servicioRepository;

    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el servicio no existe en la base de datos: " + id));
    }

    @Transactional
    public Servicio crear(Servicio servicio) {
        return servicioRepository.save(servicio);
    }


    /// Modifica el objeto SERVICIO SIN TOCAR LA BASE DE DATOS y devuelve el objeto modificado
    public Servicio modificarObjeto(Servicio servicio, boolean tiene_cocina, boolean tiene_estacionamiento, boolean tiene_lavarropa, boolean tiene_wifi) {

        if (tiene_cocina != servicio.isTieneCocina()) {
            servicio.setTieneCocina(tiene_cocina);
        }

        if (tiene_estacionamiento != servicio.isTieneEstacionamiento()) {
            servicio.setTieneEstacionamiento(tiene_estacionamiento);
        }

        if (tiene_lavarropa != servicio.isTieneLavarropa()) {
            servicio.setTieneLavarropa(tiene_lavarropa);
        }

        if (tiene_wifi != servicio.isTieneWifi()) {
            servicio.setTieneWifi(tiene_wifi);
        }

        return servicio;
    }

}