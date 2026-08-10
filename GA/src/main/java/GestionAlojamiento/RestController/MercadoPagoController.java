package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.AliasMercadoPagoDTO;
import GestionAlojamiento.DTO.CuentaMercadoPagoRespuestaDTO;
import GestionAlojamiento.Service.MercadoPagoCuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/MercadoPago")
@RequiredArgsConstructor
public class MercadoPagoController {

    private final MercadoPagoCuentaService mercadoPagoCuentaService;

    /// El anfitrion consulta el estado de su propia cuenta de cobro.
    @GetMapping("/mi-cuenta")
    public CuentaMercadoPagoRespuestaDTO miCuenta(Authentication auth) {
        return mercadoPagoCuentaService.obtenerMiCuenta(auth.getName());
    }

    /// Modo manual: el anfitrion carga/actualiza su alias o CVU (informativo, liquidacion manual).
    @PutMapping("/alias")
    public CuentaMercadoPagoRespuestaDTO actualizarAlias(@RequestBody @Valid AliasMercadoPagoDTO dto, Authentication auth) {
        return mercadoPagoCuentaService.actualizarAlias(auth.getName(), dto.getAlias());
    }

    /// Modo real: devuelve la URL de Mercado Pago a la que el frontend tiene que redirigir
    /// al anfitrion para que autorice la conexion de su cuenta (requiere MP_CLIENT_ID configurado).
    @GetMapping("/conectar")
    public Map<String, String> conectar(Authentication auth) {
        return Map.of("url", mercadoPagoCuentaService.generarUrlConexion(auth.getName()));
    }

    /// Mercado Pago redirige aca (sin JWT, es una navegacion del browser) despues de que
    /// el anfitrion autoriza. "state" es el id del anfitrion que mandamos en generarUrlConexion().
    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam String code, @RequestParam("state") Long idAnfitrion) {
        mercadoPagoCuentaService.procesarCallback(code, idAnfitrion);
        return ResponseEntity.status(302).location(java.net.URI.create("/mercadopago-conectado.html")).build();
    }
}
