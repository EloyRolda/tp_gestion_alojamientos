package com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/// Wrapper fino sobre el SDK de Cloudinary: sube/borra las imagenes de la
/// galeria de un alojamiento (redimensiona a 600x600, formato webp).
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /// Devuelve el mapa crudo que responde Cloudinary (entre otras claves trae
    /// "secure_url" y "public_id", que son las que usa ImagenService/GaleriaController).
    public Map upload(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "imagenes", "resource_type", "auto",
                    "transformation", new Transformation().width(600).height(600).crop("pad").quality("auto"), "format", "webp")
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