package com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ImagenResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Imagen;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.Galeria.ImagenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagenService {

    private final ImagenRepository imagenRepository;
    private final CloudinaryService cloudinaryService;

    public List<Imagen> listarPorGaleria(Long idGaleria) {
        return imagenRepository.findByGaleria_Id(idGaleria);
    }

    @Transactional
    public List<ImagenResponseDTO> listarDTOPorGaleria(Long idGaleria) {
        return listarPorGaleria(idGaleria).stream().map(ImagenResponseDTO::desde).toList();
    }

    public Imagen obtenerPorId(Long id) {
        return imagenRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Imagen no encontrada: " + id));
    }

    /// Se resuelve la cadena Imagen->Galeria->Alojamiento->Anfitrion aca adentro (con
    /// sesion de Hibernate activa) porque open-in-view esta desactivado: si se accede
    /// a esta cadena lazy desde el controller, ya fuera de transaccion, explota.
    @Transactional
    public Imagen obtenerPorIdVerificandoDuenio(Long id, String emailSolicitante) {
        Imagen imagen = obtenerPorId(id);
        String duenio = imagen.getGaleria().getAlojamiento().getAnfitrion().getEmail();
        if (!duenio.equals(emailSolicitante)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta imagen no le pertenece");
        }
        return imagen;
    }

    @Transactional
    public ImagenResponseDTO crear(Imagen imagen) {
        return ImagenResponseDTO.desde(imagenRepository.save(imagen));
    }

    /// Borra una imagen puntual, tanto en la base de datos como en Cloudinary.
    @Transactional
    public void borrarPorId(Long id) {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Imagen no encontrada: " + id));
        cloudinaryService.delete(imagen.getPublicId());
        imagenRepository.deleteById(id);
    }

    /// Borra todas las imagenes de una galeria, tanto en la base de datos como en Cloudinary.
    /// El borrado en Cloudinary de cada imagen se tolera individualmente: si una
    /// falla (ej: error de red transitorio), no debe abortar el @Transactional a
    /// mitad de camino, porque eso revierte el borrado en la base pero deja las
    /// imagenes anteriores ya borradas en Cloudinary — la galeria quedaria
    /// sirviendo links rotos sin forma limpia de reintentar. Se prioriza que la
    /// base (lo que de verdad ve la app) siempre quede consistente; un fallo
    /// puntual de Cloudinary solo queda logueado.
    @Transactional
    public void borrarPorGaleriaId(Long idGaleria) {
        List<Imagen> imagenes = imagenRepository.findByGaleria_Id(idGaleria);
        for (Imagen imagen : imagenes) {
            try {
                cloudinaryService.delete(imagen.getPublicId());
            } catch (RuntimeException e) {
                log.warn("No se pudo borrar en Cloudinary la imagen {} (publicId {}): {}", imagen.getId(), imagen.getPublicId(), e.getMessage());
            }
        }
        imagenRepository.deleteByGaleria_Id(idGaleria);
    }
}
