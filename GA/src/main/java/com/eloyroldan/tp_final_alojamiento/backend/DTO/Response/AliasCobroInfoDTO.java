package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Resultado de validar un alias/CBU/CVU: nunca incluye titular (no es posible
/// obtenerlo por API, ver documentacion del proyecto). Solo formato + banco.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliasCobroInfoDTO {
    private boolean valido;
    private String tipo;       // CBU, CVU_U_OTRO o ALIAS
    private String banco;      // null si no se pudo identificar
    private String mensaje;
}
