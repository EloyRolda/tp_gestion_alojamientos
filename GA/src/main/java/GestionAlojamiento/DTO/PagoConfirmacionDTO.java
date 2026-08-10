package GestionAlojamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Simula el webhook/retorno que en produccion mandaria Mercado Pago tras el
/// checkout. Mientras no haya credenciales reales, este endpoint lo dispara
/// el propio frontend para simular "el pago se aprobo" / "se rechazo".
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoConfirmacionDTO {
    private Long idReserva;
    private boolean aprobado;
}
