package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Gallery;
import GestionAlojamiento.Repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GalleryService {

    /// Minimo de fotos exigido por el anfitrion para poder publicar un alojamiento (visible en el listado publico).
    public static final int MINIMO_IMAGENES_PARA_PUBLICAR = 3;

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

    /// Retorna una Gallery por id, verificando que el solicitante sea el dueno (o admin).
    public Gallery getGalleryById(Long galleryId, String userEmail) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IdNoEncontradoException("Gallery not found"));

        alojamientoService.validarPropietarioOAdmin(gallery.getAlojamiento(), userEmail);

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

    /// Carpeta de Cloudinary para las imagenes de este alojamiento.
    public String carpetaCloudinary(Long idAlojamiento) {
        return "alojamientos/" + idAlojamiento;
    }

    /// El anfitrion (o el admin) intenta publicar (hacer visible en el listado) el alojamiento.
    /// Solo se permite si tiene al menos MINIMO_IMAGENES_PARA_PUBLICAR fotos cargadas
    /// (el precio ya es obligatorio a nivel de columna, no hace falta validarlo aca).
    public void publicar(Long idAlojamiento, String emailSolicitante) {
        Alojamiento alojamiento = alojamientoService.obtenerPorId(idAlojamiento);
        alojamientoService.validarPropietarioOAdmin(alojamiento, emailSolicitante);

        Gallery gallery = getGalleryByAlojamiento(idAlojamiento);
        int cantidadImagenes = imageService.obtainByGallery(gallery.getId()).size();

        if (cantidadImagenes < MINIMO_IMAGENES_PARA_PUBLICAR) {
            throw new ParametroInvalidoException(
                    "Necesitas al menos " + MINIMO_IMAGENES_PARA_PUBLICAR + " fotos para publicar el alojamiento (tenes " + cantidadImagenes + ").");
        }

        alojamientoService.actualizarPublicado(idAlojamiento, true);
    }

    public void despublicar(Long idAlojamiento, String emailSolicitante) {
        Alojamiento alojamiento = alojamientoService.obtenerPorId(idAlojamiento);
        alojamientoService.validarPropietarioOAdmin(alojamiento, emailSolicitante);
        alojamientoService.actualizarPublicado(idAlojamiento, false);
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
