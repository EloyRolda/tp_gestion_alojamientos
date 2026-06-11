package GestionAlojamiento.Service;


import GestionAlojamiento.Model.Image;
import GestionAlojamiento.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    /// Obtiene todas las imagenes de una galeria
    public List<Image> obtainByGallery(Long id_gallery) {
        return imageRepository.findByGalleryId(id_gallery);
    }

    /// Saves the image in the database
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(()-> new RuntimeException("Imagen not found"));
        cloudinaryService.delete(image.getPublicId());
        imageRepository.deleteById(id);
    }

    /// Borra todas las imágenes de una galería y tambien en Cloudinary
    public void borrarPorGalleryId(Long galleryId) {
        List<Image> imagenes = imageRepository.findByGalleryId(galleryId);
        for (Image image : imagenes) {
            cloudinaryService.delete(image.getPublicId());
        }
        imagenes.forEach(image -> imageRepository.deleteById(image.getId()));
    }
}
