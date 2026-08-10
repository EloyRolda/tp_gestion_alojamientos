package GestionAlojamiento.Service;

import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Enums.CategoriaNotificacion;
import GestionAlojamiento.Model.Enums.EstadoPago;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import GestionAlojamiento.Model.Pago;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Repository.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/// ---------------------------------------------------------------------------
///  MODO SIMULADO: todavia no hay credenciales de Mercado Pago cargadas.
///  Este service concentra TODO lo que en el futuro hable con la API real de
///  pagos, para que conectar Checkout Pro sea cambiar solo este archivo:
///
///   1) generarPreferencia(): hoy arma un "init_point" falso. En produccion,
///      aca se llama a la SDK de Mercado Pago (com.mercadopago:sdk-java) con
///      las credenciales (MP_ACCESS_TOKEN) y se devuelve el init_point real
///      que te da la API para redirigir al checkout.
///   2) confirmar(): hoy lo dispara el propio frontend simulando el resultado.
///      En produccion, este metodo lo tiene que llamar el controller que
///      recibe el WEBHOOK de Mercado Pago (POST /Pago/webhook), verificando
///      la firma del payload antes de confirmar.
/// ---------------------------------------------------------------------------
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaService reservaService;
    private final ChatService chatService;
    private final NotificacionService notificacionService;
    private final LogService logService;

    /// Genera (o recupera) la preferencia de pago de una reserva ACEPTADA y devuelve
    /// el link al que el cliente deberia ser redirigido para pagar.
    @Transactional
    public Pago generarPreferencia(Long idReserva, String emailCliente) {
        Reserva reserva = reservaService.obtenerPorId(idReserva);

        if (!reserva.getCliente().getEmail().equals(emailCliente)) {
            throw new ParametroInvalidoException("Esta reserva no te pertenece.");
        }
        if (reserva.getEstado() != EstadoReserva.ACEPTADA) {
            throw new ParametroInvalidoException("Solo se puede pagar una reserva ACEPTADA.");
        }

        Pago pago = pagoRepository.findByReservaId(idReserva).orElseGet(Pago::new);
        pago.setReserva(reserva);
        pago.setMonto(reserva.getPrecioTotal());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setPreferenceId("SIMULADO-" + UUID.randomUUID());
        pago.setFechaCreacion(LocalDateTime.now());

        return pagoRepository.save(pago);
    }

    /// Simula la respuesta de Mercado Pago (en produccion la dispara el webhook real).
    @Transactional
    public Pago confirmar(Long idReserva, boolean aprobado) {
        Pago pago = pagoRepository.findByReservaId(idReserva)
                .orElseThrow(() -> new IdNoEncontradoException("No se genero una preferencia de pago para esta reserva."));

        if (aprobado) {
            pago.setEstado(EstadoPago.APROBADO);
            pago.setPaymentId("SIMULADO-PAY-" + UUID.randomUUID());
            pago.setFechaAcreditacion(LocalDateTime.now());
            pagoRepository.save(pago);

            Reserva reserva = reservaService.confirmarPago(idReserva);
            chatService.crearParaReserva(reserva);

            notificacionService.crear(reserva.getCliente(),
                    "¡Tu reserva de \"" + reserva.getAlojamiento().getTitulo() + "\" fue confirmada!",
                    CategoriaNotificacion.PAGO, "reserva:" + reserva.getId());
            notificacionService.crear(reserva.getAlojamiento().getAnfitrion(),
                    "Se acredito el pago de la reserva de \"" + reserva.getAlojamiento().getTitulo() + "\".",
                    CategoriaNotificacion.PAGO, "reserva:" + reserva.getId());

            logService.registrar(reserva.getCliente(), "PAGO", "RESERVA", reserva.getId(), "Pago aprobado (simulado)");
        } else {
            pago.setEstado(EstadoPago.RECHAZADO);
            pagoRepository.save(pago);
        }

        return pago;
    }
}
