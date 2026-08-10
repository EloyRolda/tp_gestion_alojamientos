package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReviewModificarDTO;
import GestionAlojamiento.DTO.ReviewRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Model.Review;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    /// Ventana para poder editar/borrar una reseña ya publicada.
    public static final long HORAS_LIMITE_EDICION = 48;

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
    /// Se reseña una ESTADIA puntual (la reserva), no el alojamiento en abstracto: por eso
    /// un mismo cliente puede dejar varias reseñas del mismo lugar si se hospedo mas de una vez.
    @Transactional
    public Review crear(ReviewRegistroDTO dto, String emailCliente) {

        Reserva reserva = reservaService.obtenerPorId(dto.getIdReserva());
        Usuario cliente = usuarioService.obtenerPorEmail(emailCliente);

        if (!reserva.getCliente().getId().equals(cliente.getId())) {
            throw new ParametroInvalidoException("Esta reserva no te pertenece.");
        }
        if (reserva.getEstado() != EstadoReserva.FINALIZADA) {
            throw new ParametroInvalidoException("Solo podes reseñar estadias ya finalizadas.");
        }
        if (reviewRepository.existsByReservaId(reserva.getId())) {
            throw new ParametroInvalidoException("Ya dejaste una reseña para esta estadia.");
        }

        validarPuntuacion(dto.getPuntuacion());
        if (dto.getComentario() == null || dto.getComentario().isBlank()) {
            throw new ParametroInvalidoException("El comentario no puede estar vacio.");
        }

        Review review = new Review();
        review.setPuntuacion(dto.getPuntuacion());
        review.setComentario(dto.getComentario());
        review.setCliente(cliente);
        review.setAlojamiento(reserva.getAlojamiento());
        review.setReserva(reserva);
        review.setFecha(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long idReview, String emailSolicitante) {
        Review review = obtenerPorId(idReview);
        Usuario solicitante = usuarioService.obtenerPorEmail(emailSolicitante);
        validarPropietarioOAdmin(review, solicitante);
        validarVentanaEdicion(review, solicitante);
        reviewRepository.deleteById(idReview);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    /// Una reseña solo se puede editar dentro de las 48hs de haberse creado (y solo su autor).
    @Transactional
    public Review modificar(ReviewModificarDTO dto, String emailSolicitante) {

        Review review = obtenerPorId(dto.getId());
        Usuario solicitante = usuarioService.obtenerPorEmail(emailSolicitante);
        validarPropietarioOAdmin(review, solicitante);
        validarVentanaEdicion(review, solicitante);

        if (dto.getPuntuacion() != null) {
            validarPuntuacion(dto.getPuntuacion());
            review.setPuntuacion(dto.getPuntuacion());
        }
        if (dto.getComentario() != null && !dto.getComentario().isBlank()) {
            review.setComentario(dto.getComentario());
        }

        return reviewRepository.save(review);
    }

    //---------------------------------------- PRIVADOS ----------------------------------------

    private void validarPropietarioOAdmin(Review review, Usuario solicitante) {
        boolean esAdmin = solicitante.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
        boolean esAutor = review.getCliente().getId().equals(solicitante.getId());
        if (!esAdmin && !esAutor) {
            throw new ParametroInvalidoException("No autorizado sobre esta reseña.");
        }
    }

    private void validarVentanaEdicion(Review review, Usuario solicitante) {
        boolean esAdmin = solicitante.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
        if (esAdmin) {
            return; // el admin puede moderar reseñas fuera de la ventana (ej: por un reporte).
        }
        long horasTranscurridas = Duration.between(review.getFecha(), LocalDateTime.now()).toHours();
        if (horasTranscurridas > HORAS_LIMITE_EDICION) {
            throw new ParametroInvalidoException("Ya pasaron las " + HORAS_LIMITE_EDICION + "hs para editar esta reseña.");
        }
    }

    private void validarPuntuacion(Integer puntuacion) {
        if (puntuacion == null || puntuacion < 0 || puntuacion > 5) {
            throw new ParametroInvalidoException("La puntuacion debe estar entre 0 y 5.");
        }
    }
}
