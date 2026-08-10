package GestionAlojamiento.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /// Sube un archivo a una carpeta especifica de Cloudinary (ej: "alojamientos/12", "usuarios/7").
    /// Cada entidad organiza sus propias imagenes en su propia carpeta en vez de
    /// mandar todo a una carpeta generica "imagenes" como antes.
    public Map upload(MultipartFile file, String folder) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "auto",
                    "transformation", new Transformation().width(800).height(800).crop("limit").quality("auto"),
                    "format", "webp")
            );
        } catch (IOException e) {
            throw new RuntimeException("Error uploading at cloudinary", e);
        }
    }

    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting at Cloudinary", e);
        }
    }
}
