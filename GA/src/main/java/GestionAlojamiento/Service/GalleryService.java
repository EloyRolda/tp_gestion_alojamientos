package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Gallery;
import GestionAlojamiento.Repository.GalleryRepository;
//Amongus
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final AlojamientoService alojamientoService;
    private final ImageService imageService;

    /// Retorna la galería asociada a un alojamiento
    public Gallery getGalleryByAlojamiento(Long idAlojamiento) {
        Alojamiento alojamiento = alojamientoService.obtenerPorId(idAlojamiento);
        Gallery gallery = galleryRepository.findByAlojamientoId(alojamiento.getId());
        if (gallery == null) {
            throw new IdNoEncontradoException("Gallery not found for alojamiento " + idAlojamiento);
        }
        return gallery;
    }

    /// Retorna una Gallery por id, verificando que el email del anfitrión coincida
    public Gallery getGalleryById(Long galleryId, String userEmail) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IdNoEncontradoException("Gallery not found"));

        if (!gallery.getAlojamiento().getAnfitrion().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This gallery doesn't belong to you");
        }

        return gallery;
    }

    /// Retorna una Gallery por id sin verificar ownership (para lectura pública autenticada)
    public Gallery getGalleryByIdPublic(Long galleryId) {
        return galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IdNoEncontradoException("Gallery not found"));
    }

    /// Crea una nueva galería
    public Gallery createGallery(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    /// Borra la galería asociada a un alojamiento (usado al eliminar un alojamiento)
    public void borrarPorAlojamientoId(Long alojamientoId) {
        Gallery gallery = galleryRepository.findByAlojamientoId(alojamientoId);
        if (gallery != null) {
            imageService.borrarPorGalleryId(gallery.getId());
            galleryRepository.deleteByAlojamientoId(alojamientoId);
        }
    }
}