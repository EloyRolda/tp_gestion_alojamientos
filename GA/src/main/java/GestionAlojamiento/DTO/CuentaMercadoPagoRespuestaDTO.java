package GestionAlojamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Lo que ve el anfitrion sobre su propia cuenta de cobro. Nunca expone
/// accessToken/refreshToken (son datos sensibles, se quedan solo en el backend).
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaMercadoPagoRespuestaDTO {
    private String alias;
    private boolean conectadoPorOauth;
    private String fechaConexion;
}
