package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Servicio;
import GestionAlojamiento.Repository.ServicioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final ServicioRepository servicioRepository;

    //------------------------ LISTAR ------------------------
    public List<Servicio> listarServicio() {
        return servicioRepository.findAll();
    }


    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

    //------------------------ CREAR/GUARDAR ------------------------
    @Transactional
    public Servicio crear(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("No existe el servicio con ID: " + id);
        }
        servicioRepository.deleteById(id);
    }
}


