package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.DireccionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Direccion;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.DireccionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DireccionService {

    private final DireccionRepository direccionRepository;

    /// AlojamientoService depende de DireccionService (para resolver/crear direcciones),
    /// asi que esta referencia de vuelta se inyecta con @Lazy para evitar el ciclo.
    @Lazy
    private final AlojamientoService alojamientoService;

    public List<Direccion> listarTodas() {
        return direccionRepository.findAll();
    }

    /// Busca una direccion ya cargada por calle+altura+ciudad, para que un anfitrion
    /// pueda reutilizarla en lugar de crear una nueva (ej: mismo predio, varias cabanias).
    public List<Direccion> buscar(String calle, Integer altura, String ciudad) {
        return direccionRepository.findByCalleIgnoreCaseAndAlturaAndCiudadIgnoreCase(calle, altura, ciudad);
    }

    public Direccion obtenerPorId(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("ID de direccion no encontrado en la base de datos: " + id));
    }

    @Transactional
    /// Crea una direccion nueva a partir del DTO (normalizada a minuscula donde corresponde).
    public Direccion crear(DireccionDTO dto) {
        Direccion direccion = new Direccion();
        direccion.setPais(dto.getPais().toLowerCase());
        direccion.setProvincia(dto.getProvincia().toLowerCase());
        direccion.setCiudad(dto.getCiudad().toLowerCase());
        direccion.setCalle(dto.getCalle().toLowerCase());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setAltura(dto.getAltura());
        validarCoordenadas(dto.getLatitud(), dto.getLongitud());
        direccion.setLatitud(dto.getLatitud());
        direccion.setLongitud(dto.getLongitud());
        return direccionRepository.save(direccion);
    }

    @Transactional
    /// Mueve el punto en el mapa de una direccion existente. Si la direccion es
    /// compartida (mismo predio), el punto nuevo vale para todos sus alojamientos.
    public Direccion actualizarCoordenadas(Long idDireccion, Double latitud, Double longitud) {
        validarCoordenadas(latitud, longitud);
        Direccion direccion = obtenerPorId(idDireccion);
        direccion.setLatitud(latitud);
        direccion.setLongitud(longitud);
        return direccionRepository.save(direccion);
    }

    /// Latitud y longitud van siempre juntas: una sola no ubica nada en el mapa.
    private void validarCoordenadas(Double latitud, Double longitud) {
        if ((latitud == null) != (longitud == null)) {
            throw new ParametroInvalidoException("La ubicacion en el mapa necesita latitud y longitud juntas.");
        }
    }

    @Transactional
    /// Solo se puede borrar si ningun alojamiento la sigue usando (puede ser compartida).
    public void borrarPorId(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new IdNoEncontradoException("ID no encontrado en la base de datos: " + id);
        }
        if (alojamientoService.existeConDireccion(id)) {
            throw new ParametroInvalidoException("No se puede borrar: hay alojamientos que usan esta direccion.");
        }
        direccionRepository.deleteById(id);
    }
}
