package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // Inyectamos los Services en lugar de los Repositories
    private final ClienteService clienteService;
    private final AlojamientoService alojamientoService;

    //------------------------ LISTAR ------------------------
    public List<Review> listarTodas() {
        return reviewRepository.findAll();
    }

    public Review obtenerPorId(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, id de review no encontrado."));
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Review crear(Long id_cliente, Long id_alojamiento, Review review) {
        // Usamos los métodos 'obtenerPorId' que ya tienen sus propias excepciones
        Cliente cliente = clienteService.obtenerPorId(id_cliente);
        Alojamiento alojamiento = alojamientoService.obtenerPorId(id_alojamiento);

        review.setCliente(cliente);
        review.setAlojamiento(alojamiento);

        return reviewRepository.save(review);
    }

    @Transactional
    public void borrarPorId(Long id_review) {
        if (!reviewRepository.existsById(id_review)) {
            throw new RuntimeException("Error, el id de la review no existe.");
        }
        reviewRepository.deleteById(id_review);
    }

    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Review actualizarPuntuacion(Long id, Integer nuevaPuntuacion) {
        if (nuevaPuntuacion < 1 || nuevaPuntuacion > 5) {
            throw new RuntimeException("La puntuación debe estar entre 1 y 5.");
        }
        Review review = obtenerPorId(id);
        review.setPuntuacion(nuevaPuntuacion);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review actualizarComentario(Long id, String nuevoComentario) {
        Review review = obtenerPorId(id);
        review.setComentario(nuevoComentario);
        return reviewRepository.save(review);
    }
}