package com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.GaleriaResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Galeria;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.Galeria.GaleriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GaleriaService {

    private final GaleriaRepository galeriaRepository;
    private final ImagenService imagenService;

    /// Retorna la galeria asociada a un alojamiento (regla de app: una por alojamiento).
    public Galeria obtenerPorAlojamientoId(Long idAlojamiento) {
        return galeriaRepository.findByAlojamiento_IdAlojamiento(idAlojamiento)
                .orElseThrow(() -> new IdNoEncontradoException("Galeria no encontrada para el alojamiento " + idAlojamiento));
    }

    /// Retorna una galeria por id, verificando que el email del anfitrion coincida.
    /// Se resuelve la cadena lazy Galeria->Alojamiento->Anfitrion aca adentro, con la
    /// sesion de Hibernate todavia activa (open-in-view esta desactivado).
    @Transactional
    public Galeria obtenerPorIdVerificandoDuenio(Long idGaleria, String emailAnfitrion) {
        Galeria galeria = galeriaRepository.findById(idGaleria)
                .orElseThrow(() -> new IdNoEncontradoException("Galeria no encontrada"));

        if (!galeria.getAlojamiento().getAnfitrion().getEmail().equals(emailAnfitrion)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta galeria no le pertenece");
        }
        return galeria;
    }

    /// Retorna una galeria por id sin verificar dueño (lectura publica autenticada).
    public Galeria obtenerPorId(Long idGaleria) {
        return galeriaRepository.findById(idGaleria)
                .orElseThrow(() -> new IdNoEncontradoException("Galeria no encontrada"));
    }

    @Transactional
    public GaleriaResponseDTO obtenerDTOPorAlojamientoId(Long idAlojamiento) {
        return GaleriaResponseDTO.desde(obtenerPorAlojamientoId(idAlojamiento));
    }

    /// Crea la galeria vacia de un alojamiento recien publicado.
    @Transactional
    public Galeria crearParaAlojamiento(Alojamiento alojamiento) {
        Galeria galeria = new Galeria();
        galeria.setNombreGaleria("Galeria de " + alojamiento.getTitulo());
        galeria.setAlojamiento(alojamiento);
        return galeriaRepository.save(galeria);
    }

    /// Borra la galeria (y sus imagenes, incluso en Cloudinary) al eliminar un alojamiento.
    @Transactional
    public void borrarPorAlojamientoId(Long idAlojamiento) {
        galeriaRepository.findByAlojamiento_IdAlojamiento(idAlojamiento).ifPresent(galeria -> {
            imagenService.borrarPorGaleriaId(galeria.getId());
            galeriaRepository.deleteByAlojamiento_IdAlojamiento(idAlojamiento);
        });
    }
}
