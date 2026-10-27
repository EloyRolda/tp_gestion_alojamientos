package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Lo que devuelve MercadoPago al crear una preferencia de Checkout Pro.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenciaMercadoPagoDTO {
    private String idPreferencia;
    private String urlCheckout;
}
