package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.GaleriaResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ImagenResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Galeria;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Imagen;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria.CloudinaryService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria.GaleriaService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Galeria.ImagenService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Galeria")
@RequiredArgsConstructor
public class GaleriaController {

    private final GaleriaService galeriaService;
    private final ImagenService imagenService;
    private final CloudinaryService cloudinaryService;
    private final UsuarioService usuarioService;

    @GetMapping("/alojamiento/{idAlojamiento}")
    public GaleriaResponseDTO obtenerPorAlojamiento(@PathVariable Long idAlojamiento) {
        return galeriaService.obtenerDTOPorAlojamientoId(idAlojamiento);
    }

    @GetMapping("/{id}/imagenes")
    public List<ImagenResponseDTO> listarImagenes(@PathVariable Long id) {
        galeriaService.obtenerPorId(id); // valida que exista
        return imagenService.listarDTOPorGaleria(id);
    }

    /// Maximo 10 imagenes por galeria.
    @PostMapping("/upload")
    public ResponseEntity<ImagenResponseDTO> subir(
            @RequestParam("file") MultipartFile file,
            @RequestParam("alt") String alt,
            @RequestParam("galleryId") Long idGaleria,
            Authentication auth
    ) {
        Galeria galeria = verificarDuenioOAdmin(idGaleria, auth);

        if (file.isEmpty()) {
            throw new ParametroInvalidoException("El archivo esta vacio.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ParametroInvalidoException("El archivo debe ser una imagen (jpg, png, webp, etc).");
        }

        List<Imagen> existentes = imagenService.listarPorGaleria(idGaleria);
        if (existentes.size() >= 10) {
            throw new ParametroInvalidoException("La galeria ya tiene el maximo de 10 imagenes.");
        }

        Map<?, ?> resultado = cloudinaryService.upload(file);
        Imagen imagen = new Imagen();
        imagen.setAlt(alt);
        imagen.setUrl((String) resultado.get("secure_url"));
        imagen.setPublicId((String) resultado.get("public_id"));
        imagen.setGaleria(galeria);

        return ResponseEntity.status(HttpStatus.CREATED).body(imagenService.crear(imagen));
    }

    @DeleteMapping("/imagen/{id}")
    public ResponseEntity<?> eliminarImagen(@PathVariable Long id, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        boolean esAdmin = solicitante.getTipo() == TIPOUSUARIO.ADMIN;

        if (!esAdmin) {
            imagenService.obtenerPorIdVerificandoDuenio(id, solicitante.getEmail());
        }
        imagenService.borrarPorId(id);
        return ResponseEntity.ok().build();
    }

    private Galeria verificarDuenioOAdmin(Long idGaleria, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        if (solicitante.getTipo() == TIPOUSUARIO.ADMIN) {
            return galeriaService.obtenerPorId(idGaleria);
        }
        return galeriaService.obtenerPorIdVerificandoDuenio(idGaleria, solicitante.getEmail());
    }
}
