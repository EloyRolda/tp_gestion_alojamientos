package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.PagoConfirmacionDTO;
import GestionAlojamiento.Model.Pago;
import GestionAlojamiento.Service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/// Flujo de pago. Hoy trabaja en modo SIMULADO (ver PagoService) porque todavia
/// no hay credenciales de Mercado Pago cargadas: /generar devuelve un link falso,
/// y /confirmar lo dispara el propio frontend en vez de un webhook real.
/// El dia que se carguen las credenciales, esto pasa a llamarse desde el
/// webhook real de MP sin tocar nada del resto del flujo de reservas.
@RestController
@RequestMapping("/Pago")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/generar/{idReserva}")
    public Pago generar(@PathVariable Long idReserva, Authentication auth) {
        return pagoService.generarPreferencia(idReserva, auth.getName());
    }

    /// SIMULADO: en produccion este endpoint lo llama el webhook de Mercado Pago, no el frontend.
    @PostMapping("/confirmar")
    public Pago confirmar(@RequestBody PagoConfirmacionDTO dto) {
        return pagoService.confirmar(dto.getIdReserva(), dto.isAprobado());
    }
}
