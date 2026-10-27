package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.PreferenciaMercadoPagoDTO;

import java.math.BigDecimal;
import java.util.Map;

/// Abstraccion de "pasarela de pago" (DIP): PagoService y las PoliticaCancelacion
/// (Service/Cancelacion/) dependen de esta interfaz, no de MercadoPagoService
/// directo. Hoy hay una sola implementacion (MercadoPago), pero el negocio en
/// si (crear un cobro, consultar su estado, reembolsar, liquidar al anfitrion)
/// no es especifico de MercadoPago — cambiar de proveedor en el futuro seria
/// agregar una implementacion nueva de esta interfaz, no reescribir
/// PagoService/las politicas.
public interface PasarelaDePagoService {

    /// Crea el cobro (Checkout) para que el cliente pague.
    PreferenciaMercadoPagoDTO crearPreferencia(String externalReference, BigDecimal monto,
                                                String descripcion, String emailComprador);

    /// Consulta el estado real de un pago ya creado (nunca confiar ciegamente en
    /// lo que manda un webhook sin volver a preguntar).
    Map<String, Object> consultarPago(String idPagoExterno);

    /// Reembolso total (monto null) o parcial (monto informado) sobre un pago ya aprobado.
    void reembolsar(String idPagoExterno, BigDecimal monto);

    /// Transfiere (Payout) al anfitrion usando su alias/CBU/CVU. Devuelve el id
    /// externo de la transferencia.
    String crearPayout(String aliasCobroDestino, BigDecimal monto, String referencia);
}
