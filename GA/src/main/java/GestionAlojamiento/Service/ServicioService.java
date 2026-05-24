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
}