package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.CuentaMercadoPagoRespuestaDTO;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.CuentaMercadoPago;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.CuentaMercadoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

/// ---------------------------------------------------------------------------
///  Cuenta de cobro del anfitrion. Dos modos, ver el javadoc de CuentaMercadoPago:
///
///   MODO MANUAL (alias/CVU):
///     - Funciona siempre, no depende de ninguna credencial externa.
///     - Es solo informativo: no rutea plata automaticamente.
///
///   MODO OAUTH (conexion real "Marketplace" de Mercado Pago):
///     - Requiere haber creado una aplicacion en https://www.mercadopago.com.ar/developers
///       y cargar MP_CLIENT_ID / MP_CLIENT_SECRET / MP_REDIRECT_URI como variables
///       de entorno (ver .env.example). Mientras esas variables esten vacias,
///       generarUrlConexion() tira un error claro en vez de armar una URL rota.
///     - El intercambio del "code" por el access_token/refresh_token pega
///       directo contra la API real de Mercado Pago (oauth/token). Esto todavia
///       no se probo con credenciales reales porque no las tenemos cargadas.
/// ---------------------------------------------------------------------------
@Service
@RequiredArgsConstructor
public class MercadoPagoCuentaService {

    private final CuentaMercadoPagoRepository cuentaRepository;
    private final UsuarioService usuarioService;

    @Value("${mercadopago.client-id:}")
    private String clientId;

    @Value("${mercadopago.client-secret:}")
    private String clientSecret;

    @Value("${mercadopago.redirect-uri:}")
    private String redirectUri;

    private final RestClient restClient = RestClient.create();

    //---------------------------------------- CONSULTA ----------------------------------------

    public CuentaMercadoPagoRespuestaDTO obtenerMiCuenta(String emailAnfitrion) {
        Usuario anfitrion = validarAnfitrion(emailAnfitrion);
        CuentaMercadoPago cuenta = cuentaRepository.findByAnfitrionId(anfitrion.getId()).orElse(null);

        if (cuenta == null) {
            return new CuentaMercadoPagoRespuestaDTO(null, false, null);
        }
        return new CuentaMercadoPagoRespuestaDTO(
                cuenta.getAlias(),
                Boolean.TRUE.equals(cuenta.getConectado()),
                cuenta.getFechaConexion() != null ? cuenta.getFechaConexion().toString() : null
        );
    }

    /// Usado por PagoService antes de generar un cobro: sin esto, el cliente no puede pagar.
    public CuentaMercadoPago exigirCuentaConfigurada(Usuario anfitrion) {
        CuentaMercadoPago cuenta = cuentaRepository.findByAnfitrionId(anfitrion.getId()).orElse(null);

        boolean tieneAlgo = cuenta != null && (
                (cuenta.getAlias() != null && !cuenta.getAlias().isBlank()) || Boolean.TRUE.equals(cuenta.getConectado())
        );

        if (!tieneAlgo) {
            throw new ParametroInvalidoException(
                    "El anfitrion todavia no configuro su alias/cuenta de Mercado Pago. No se puede cobrar esta reserva.");
        }
        return cuenta;
    }

    //---------------------------------------- MODO MANUAL (ALIAS) ----------------------------------------

    @Transactional
    public CuentaMercadoPagoRespuestaDTO actualizarAlias(String emailAnfitrion, String alias) {
        Usuario anfitrion = validarAnfitrion(emailAnfitrion);
        CuentaMercadoPago cuenta = obtenerOCrear(anfitrion);
        cuenta.setAlias(alias);
        cuentaRepository.save(cuenta);
        return obtenerMiCuenta(emailAnfitrion);
    }

    //---------------------------------------- MODO OAUTH (CONEXION REAL) ----------------------------------------

    /// Arma la URL a la que hay que mandar al anfitrion para que autorice la conexion.
    /// El "state" lleva el id del anfitrion para poder identificarlo cuando MP nos redirige de vuelta.
    public String generarUrlConexion(String emailAnfitrion) {
        Usuario anfitrion = validarAnfitrion(emailAnfitrion);
        validarCredencialesConfiguradas();

        String redirectCodificado = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return "https://auth.mercadopago.com.ar/authorization"
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&platform_id=mp"
                + "&state=" + anfitrion.getId()
                + "&redirect_uri=" + redirectCodificado;
    }

    /// Mercado Pago redirige aca despues de que el anfitrion autoriza. Intercambiamos
    /// el "code" por un access_token/refresh_token reales pegandole a la API de MP.
    @SuppressWarnings("unchecked")
    @Transactional
    public void procesarCallback(String code, Long idAnfitrion) {
        validarCredencialesConfiguradas();

        Usuario anfitrion = usuarioService.obtenerPorId(idAnfitrion);

        Map<String, Object> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", redirectUri
        );

        Map<String, Object> respuesta = restClient.post()
                .uri("https://api.mercadopago.com/oauth/token")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        if (respuesta == null || respuesta.get("access_token") == null) {
            throw new ParametroInvalidoException("Mercado Pago no devolvio un token valido. Reintenta la conexion.");
        }

        CuentaMercadoPago cuenta = obtenerOCrear(anfitrion);
        cuenta.setAccessToken((String) respuesta.get("access_token"));
        cuenta.setRefreshToken((String) respuesta.get("refresh_token"));
        cuenta.setCollectorId(respuesta.get("user_id") != null ? Long.valueOf(respuesta.get("user_id").toString()) : null);
        cuenta.setConectado(true);
        cuenta.setFechaConexion(LocalDateTime.now());
        cuentaRepository.save(cuenta);
    }

    //---------------------------------------- PRIVADOS ----------------------------------------

    private CuentaMercadoPago obtenerOCrear(Usuario anfitrion) {
        return cuentaRepository.findByAnfitrionId(anfitrion.getId()).orElseGet(() -> {
            CuentaMercadoPago nueva = new CuentaMercadoPago();
            nueva.setAnfitrion(anfitrion);
            nueva.setConectado(false);
            return nueva;
        });
    }

    private Usuario validarAnfitrion(String email) {
        Usuario usuario = usuarioService.obtenerPorEmail(email);
        if (usuario.getTipoUsuario() != TipoUsuario.ANFITRION) {
            throw new ParametroInvalidoException("Solo un anfitrion puede configurar una cuenta de cobro.");
        }
        return usuario;
    }

    private void validarCredencialesConfiguradas() {
        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            throw new ParametroInvalidoException(
                    "La conexion real con Mercado Pago todavia no esta habilitada en el servidor " +
                    "(faltan MP_CLIENT_ID / MP_CLIENT_SECRET). Mientras tanto, usa el alias manual.");
        }
    }
}
