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

    public Map upload(MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "imagenes", "resource_type", "auto",
                    "transformation", new Transformation().width(600).height(600).crop("pad").quality("auto"), "format", "webp")
            );
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading at cloudinary", e);
        }
    }

    public void delete(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting at Cloudinary", e);
        }
    }


}
