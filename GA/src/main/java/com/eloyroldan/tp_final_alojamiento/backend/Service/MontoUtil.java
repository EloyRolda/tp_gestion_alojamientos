package com.eloyroldan.tp_final_alojamiento.backend.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/// Redondeo monetario compartido entre PagoService y las PoliticaCancelacion
/// (Service/Cancelacion/): todo monto que se persiste o se manda a MercadoPago
/// pasa por aca, para no repetir el mismo setScale(2, HALF_UP) en cada lugar
/// que hace una cuenta con plata.
public final class MontoUtil {

    private MontoUtil() {
    }

    public static BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
