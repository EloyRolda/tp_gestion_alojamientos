package GestionAlojamiento.RestController;

import GestionAlojamiento.Model.Gallery;
import GestionAlojamiento.Model.Image;
import GestionAlojamiento.Service.AlojamientoService;
import GestionAlojamiento.Service.CloudinaryService;
import GestionAlojamiento.Service.GalleryService;
import GestionAlojamiento.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;
    private final AlojamientoService alojamientoService;

    // Obtiene la galería de un alojamiento específico
    @GetMapping("/alojamiento/{idAlojamiento}")
    public ResponseEntity<Gallery> getGallery(
            @PathVariable Long idAlojamiento,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Gallery gallery = galleryService.getGalleryByAlojamiento(idAlojamiento);
        return ResponseEntity.ok(gallery);
    }

    // Sube una imagen a la galería de un alojamiento (máx 10)
    @PostMapping("/upload")
    public ResponseEntity<Image> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("alt") String alt,
            @RequestParam("galleryId") Long galleryId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Gallery gallery = galleryService.getGalleryById(galleryId, userDetails.getUsername());

        List<Image> existingImages = imageService.obtainByGallery(galleryId);
        if (existingImages.size() >= 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Map result = cloudinaryService.upload(file);
        Image image = new Image(null, alt, (String) result.get("secure_url"), (String) result.get("public_id"), gallery);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(image));
    }

    // Crea una galería para un alojamiento (normalmente se hace al crear el alojamiento)
    @PostMapping("/create/{idAlojamiento}")
    public ResponseEntity<Gallery> createGallery(
            @PathVariable Long idAlojamiento,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Gallery gallery = new Gallery(null, "Galería de alojamiento " + idAlojamiento,
                alojamientoService.obtenerPorId(idAlojamiento));
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.createGallery(gallery));
    }

    // Lista imágenes de una galería (accesible por cualquier usuario autenticado)
    @GetMapping("/{id}/images")
    public ResponseEntity<List<Image>> galleryImages(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Solo verificamos que la galería exista, sin validar ownership
        // para que clientes también puedan ver las fotos del alojamiento
        galleryService.getGalleryByIdPublic(id);
        return ResponseEntity.ok(imageService.obtainByGallery(id));
    }

    // Elimina una imagen (de Cloudinary y BD)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }
}