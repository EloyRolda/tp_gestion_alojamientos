package GestionAlojamiento.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/// Tareas de fondo. Requiere @EnableScheduling en GaApplication.
@Component
@RequiredArgsConstructor
public class SchedulerService {

    private final ReservaService reservaService;
    private final ChatService chatService;

    /// Cada 15 minutos: vence las reservas ACEPTADAS cuya ventana de 48hs para pagar ya paso.
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void vencerSolicitudesSinPagar() {
        reservaService.vencerSolicitudesSinPagar();
    }

    /// Cada 1 hora: cierra los chats cuya estadia finalizo hace mas de 48hs.
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cerrarChatsVencidos() {
        chatService.cerrarChatsVencidos();
    }
}
