package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.CasaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CasaService {

    private final CasaRepository casaRepository;
    private final AlojamientoRepository alojamientoRepository;

    //------------------------ LISTAR ------------------------
    private List<Casa> listarCasa() {
        return casaRepository.findAll();
    }

    private Casa obtenerPorId(Long id) {
        return casaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, el id de la casa no se encuentra en la base de datos."));
    }

    private List<Casa> listarPorPatio() {
        return casaRepository.findByTiene_patio();
    }

    private List<Casa> listarPorParrilla() {
        return casaRepository.findByTiene_parrilla();
    }

    private List<Casa> listarPorPileta() {
        return casaRepository.findByTiene_Pileta();
    }

    /*
    Deberian haber combos? ej parrilla y pileta? pileta y patio?
    */
    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Casa crear(Long id_alojamiento, Casa casa) {
        casa.setAlojamiento(alojamientoRepository.findById(id_alojamiento)
                .orElseThrow(() -> new RuntimeException("Error, el id del alojamiento no se encuentra en la base de datos.")));

        return casaRepository.save(casa);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!casaRepository.existsById(id)) {
            throw new RuntimeException("Error, el id de la casa no se encuentra en la base de datos.");
        }
        casaRepository.deleteById(id);
    }

    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Casa actualizarTiene_Pileta(Long id, boolean nuevoEstado) {

        Casa casa = casaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, el id de la casa no se encuentra en la base de datos."));
        casa.setTiene_pileta(nuevoEstado);
        return casaRepository.save(casa);
    }

    @Transactional
    public Casa actualizarTiene_Patio(Long id, boolean nuevoEstado) {

        Casa casa = casaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, el id de la casa no se encuentra en la base de datos."));
        casa.setTiene_patio(nuevoEstado);
        return casaRepository.save(casa);
    }

    @Transactional
    public Casa actualizarTiene_Parrilla(Long id, boolean nuevoEstado) {

        Casa casa = casaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, el id de la casa no se encuentra en la base de datos."));
        casa.setTiene_parrilla(nuevoEstado);
        return casaRepository.save(casa);
    }

}
