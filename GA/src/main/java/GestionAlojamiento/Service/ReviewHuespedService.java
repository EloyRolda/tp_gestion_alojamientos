package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReviewHuespedRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Model.ReviewHuesped;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ReviewHuespedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewHuespedService {

    private final ReviewHuespedRepository reviewHuespedRepository;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    public List<ReviewHuesped> listarPorHuesped(Long idHuesped) {
        return reviewHuespedRepository.findByHuespedId(idHuesped);
    }

    public ReviewHuesped obtenerPorId(Long id) {
        return reviewHuespedRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Resena de huesped no encontrada: " + id));
    }

    @Transactional
    public ReviewHuesped crear(ReviewHuespedRegistroDTO dto, String emailAnfitrion) {
        Reserva reserva = reservaService.obtenerPorId(dto.getIdReserva());
        Usuario anfitrion = usuarioService.obtenerPorEmail(emailAnfitrion);

        if (!reserva.getAlojamiento().getAnfitrion().getId().equals(anfitrion.getId())) {
            throw new ParametroInvalidoException("Esta reserva no es de uno de tus alojamientos.");
        }
        if (reserva.getEstado() != EstadoReserva.FINALIZADA) {
            throw new ParametroInvalidoException("Solo podes resenar huespedes de estadias ya finalizadas.");
        }
        if (reviewHuespedRepository.existsByReservaId(reserva.getId())) {
            throw new ParametroInvalidoException("Ya resenaste al huesped de esta estadia.");
        }

        ReviewHuesped review = new ReviewHuesped();
        review.setReserva(reserva);
        review.setAnfitrion(anfitrion);
        review.setHuesped(reserva.getCliente());
        review.setComentario(dto.getComentario());
        review.setFecha(LocalDateTime.now());

        return reviewHuespedRepository.save(review);
    }
}
