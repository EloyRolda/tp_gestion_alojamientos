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

    /// Obtains all the images from a gallery
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
}
