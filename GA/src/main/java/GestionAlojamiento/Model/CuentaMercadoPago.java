package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// Datos de cobro de un anfitrion. Tiene DOS modos, independientes entre si:
///
///  1) ALIAS MANUAL (siempre disponible, no depende de credenciales de MP):
///     el anfitrion carga su alias/CVU a mano. Es solo INFORMATIVO -- le sirve
///     al admin para hacer la liquidacion/transferencia por fuera del sistema
///     mientras no haya integracion real. La API de Mercado Pago no tiene forma
///     de rutear un pago a una cuenta de terceros solo con un alias.
///
///  2) CUENTA CONECTADA POR OAUTH (requiere que la app tenga credenciales reales
///     de Mercado Pago: MP_CLIENT_ID / MP_CLIENT_SECRET, ver MercadoPagoCuentaService).
///     Recien con esto conectado se puede armar un pago "marketplace" que le
///     acredita la plata directo al anfitrion (menos la comision de la plataforma).
///     Hasta que eso no este conectado, "conectado" queda en false.
@Data
@NoArgsConstructor
@Entity
@Table(name = "cuenta_mercado_pago")
public class CuentaMercadoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta_mp")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_anfitrion", nullable = false, unique = true)
    private Usuario anfitrion;

    /// Alias o CVU cargado a mano por el anfitrion (modo manual/informativo).
    @Column(name = "alias", length = 100)
    private String alias;

    /// Id del vendedor en Mercado Pago (lo devuelve MP al conectar via OAuth).
    @Column(name = "collector_id")
    private Long collectorId;

    @Column(name = "access_token", length = 255)
    private String accessToken;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "fecha_conexion")
    private LocalDateTime fechaConexion;

    /// true unicamente si completo el flujo OAuth real (no alcanza con cargar el alias).
    @Column(name = "conectado", nullable = false)
    private Boolean conectado = false;
}
