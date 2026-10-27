package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReviewRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoRankingDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReviewResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Review;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/// La review se hace sobre una reserva puntual (no sobre el alojamiento en general),
/// por lo que un mismo cliente puede reseñar cada una de sus estadias por separado.
/// Es inmodificable una vez creada: solo se puede eliminar.
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    public List<Review> listarTodas() {
        return reviewRepository.findAll();
    }

    public List<Review> listarPorAlojamiento(Long idAlojamiento) {
        return reviewRepository.findByReserva_Alojamiento_IdAlojamiento(idAlojamiento);
    }

    public List<Review> listarPorCliente(Long idCliente) {
        return reviewRepository.findByReserva_Cliente_Id(idCliente);
    }

    public Review obtenerPorId(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, id de review no encontrado."));
    }

    @Transactional
    public Review crear(ReviewRegistroDTO dto, String emailSolicitante) {
        Reserva reserva = reservaService.obtenerPorId(dto.getIdReserva());
        Usuario solicitante = usuarioService.obtenerPorIdentificador(emailSolicitante);

        if (!reserva.getCliente().getId().equals(solicitante.getId())) {
            throw new ParametroInvalidoException("Solo el cliente de la reserva puede dejar una reseña sobre ella.");
        }
        if (reserva.getEstadoReserva() != ESTADORESERVA.FINALIZADA) {
            throw new ParametroInvalidoException("Solo se puede reseñar una reserva ya FINALIZADA.");
        }
        if (reviewRepository.existsByReserva_IdReserva(reserva.getIdReserva())) {
            throw new ParametroInvalidoException("Esta reserva ya tiene una reseña cargada.");
        }

        Review review = Review.builder()
                .puntuacion(dto.getPuntuacion())
                .comentario(dto.getComentario())
                .reserva(reserva)
                .build();

        return reviewRepository.save(review);
    }

    /// Solo el cliente dueño de la review o un admin pueden eliminarla.
    @Transactional
    public void borrarPorId(Long id, String emailSolicitante) {
        Review review = obtenerPorId(id);
        Usuario solicitante = usuarioService.obtenerPorIdentificador(emailSolicitante);

        boolean esAdmin = solicitante.getTipo() == TIPOUSUARIO.ADMIN;
        boolean esDuenio = review.getReserva().getCliente().getId().equals(solicitante.getId());

        if (!esAdmin && !esDuenio) {
            throw new ParametroInvalidoException("No autorizado para eliminar esta reseña.");
        }
        reviewRepository.deleteById(id);
    }

    //---------------------------------------- PARA CONTROLLERS (DTO) ----------------------------------------

    @Transactional
    public List<ReviewResponseDTO> listarTodasDTO() {
        return listarTodas().stream().map(ReviewResponseDTO::desde).toList();
    }

    @Transactional
    public List<ReviewResponseDTO> listarPorAlojamientoDTO(Long idAlojamiento) {
        return listarPorAlojamiento(idAlojamiento).stream().map(ReviewResponseDTO::desde).toList();
    }

    @Transactional
    public List<ReviewResponseDTO> listarPorClienteDTO(Long idCliente) {
        return listarPorCliente(idCliente).stream().map(ReviewResponseDTO::desde).toList();
    }

    @Transactional
    public ReviewResponseDTO obtenerDTOPorId(Long id) {
        return ReviewResponseDTO.desde(obtenerPorId(id));
    }

    @Transactional
    public ReviewResponseDTO crearDTO(ReviewRegistroDTO dto, String emailSolicitante) {
        return ReviewResponseDTO.desde(crear(dto, emailSolicitante));
    }

    //---------------------------------------- ESTADISTICAS ----------------------------------------

    /// Null si el alojamiento todavia no tiene ninguna review.
    /// Version en lote (ver EstadisticasController.resumenAnfitrion).
    public java.util.Map<Long, Double> promedioPorAlojamientos(List<Long> idsAlojamiento) {
        if (idsAlojamiento.isEmpty()) {
            return java.util.Map.of();
        }
        java.util.Map<Long, Double> resultado = new java.util.HashMap<>();
        for (Object[] fila : reviewRepository.promedioPorAlojamientosRaw(idsAlojamiento)) {
            resultado.put((Long) fila[0], (Double) fila[1]);
        }
        return resultado;
    }

    public List<AlojamientoRankingDTO> topPorRating(int limite) {
        return reviewRepository.topAlojamientosPorRating(PageRequest.of(0, limite));
    }
}
