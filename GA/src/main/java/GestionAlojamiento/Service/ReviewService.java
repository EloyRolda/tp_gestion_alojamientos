package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReviewModificarDTO;
import GestionAlojamiento.DTO.ReviewRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Model.Usuario;
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
    private final UsuarioService usuarioService;
    private final ReservaService reservaService;
    private final AlojamientoService alojamientoService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Review> listarTodas() {
        return reviewRepository.findAll();
    }

    public List<Review> listarPorAlojamiento(Long idAlojamiento) {

        Alojamiento alojamiento = alojamientoService.obtenerPorId(idAlojamiento);

        return reviewRepository.findByAlojamiento(alojamiento);
    }

    public List<Review> listarPorCliente(Long idCliente) {

        Usuario cliente = usuarioService.obtenerClientePorId(idCliente);

        return reviewRepository.findByCliente(cliente);
    }

    public Review obtenerPorId(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, id de review no encontrado."));
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Review crear(ReviewRegistroDTO dto) {

        Usuario cliente = usuarioService.obtenerClientePorId(dto.getIdCliente());
        Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());

        if (!reservaService.tuvisteReservaConfirmada(cliente.getId(), alojamiento.getId())) {
            throw new ParametroInvalidoException("Solo puede dejar reseña si tuvo una reserva confirmada y ya finalizó.");
        }

        // VALIDACIONES
        validarPuntuacion(dto.getPuntuacion());

        if (dto.getComentario() == null || dto.getComentario().isBlank()) {
            throw new ParametroInvalidoException("El comentario no puede estar vacio.");
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

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id_review) {
        if (!reviewRepository.existsById(id_review)) {
            throw new IdNoEncontradoException("Error, el id de la review no existe.");
        }
        reviewRepository.deleteById(id_review);
    }


    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Review modificar(ReviewModificarDTO dto) {

        Review review = obtenerPorId(dto.getId());

        // [REVIEW]
        if (dto.getPuntuacion() != null) {
            validarPuntuacion(dto.getPuntuacion());
            review.setPuntuacion(dto.getPuntuacion());
        }

        if (dto.getComentario() != null && !dto.getComentario().isBlank()) {
            review.setComentario(dto.getComentario());
        }

        // [CLIENTE]
        if (dto.getIdCliente() != null) {
            Usuario cliente = usuarioService.obtenerClientePorId(dto.getIdCliente());
            review.setCliente(cliente);
        }

        // [ALOJAMIENTO]
        if (dto.getIdAlojamiento() != null) {
            Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamiento());
            review.setAlojamiento(alojamiento);
        }

        return reviewRepository.save(review);
    }

    //Extra
    private void validarPuntuacion(int puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new ParametroInvalidoException("El puntaje debe estar entre 1 y 5.");
        }
    }

}