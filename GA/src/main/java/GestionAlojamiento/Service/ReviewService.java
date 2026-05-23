package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReviewModificarDTO;
import GestionAlojamiento.DTO.ReviewRegistroDTO;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;


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
    public Review crear(ReviewRegistroDTO dto) {

        Cliente cliente = clienteService.obtenerPorId(dto.getIdCliente());

        Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());


        // VALIDACIONES

        if (dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
            throw new RuntimeException("El puntaje debe estar entre 1 y 5.");
        }

        if (dto.getComentario() == null || dto.getComentario().isBlank()) {
            throw new RuntimeException("El comentario no puede estar vacio.");
        }


        // REVIEW

        Review review = new Review();
        review.setPuntuacion(dto.getPuntuacion());
        review.setComentario(dto.getComentario());
        review.setCliente(cliente);
        review.setAlojamiento(alojamiento);
        review.setFecha(LocalDateTime.now());

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
    public Review modificar(ReviewModificarDTO dto) {

        Review review = obtenerPorId(dto.getId());


        // [REVIEW]

        if (dto.getPuntuacion() != null) {

            if (dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
                throw new RuntimeException("El puntaje debe estar entre 1 y 5.");
            }

            review.setPuntuacion(dto.getPuntuacion());
        }

        if (dto.getComentario() != null && !dto.getComentario().isBlank()) {
            review.setComentario(dto.getComentario());
        }


        // [CLIENTE]

        if (dto.getIdCliente() != null) {

            Cliente cliente = clienteService.obtenerPorId(dto.getIdCliente());

            review.setCliente(cliente);
        }


        // [ALOJAMIENTO]

        if (dto.getIdAlojamiento() != null) {

            Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());

            review.setAlojamiento(alojamiento);
        }


        return reviewRepository.save(review);
    }

    public List<Review> listarPorAlojamiento(Long idAlojamiento) {

        Alojamiento alojamiento = alojamientoService.obtenerPorId(idAlojamiento);

        return reviewRepository.findByAlojamiento(alojamiento);
    }

    public List<Review> listarPorCliente(Long idCliente) {

        Cliente cliente = clienteService.obtenerPorId(idCliente);

        return reviewRepository.findByCliente(cliente);
    }

}