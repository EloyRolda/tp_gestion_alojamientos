package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AliasCobroInfoDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Pattern;

/// Valida el alias/CBU/CVU que carga un anfitrion para cobrar via Payout de
/// MercadoPago. Solo valida ESTRUCTURA (22 digitos + digito verificador) e
/// identifica el banco/billetera por el codigo de entidad (primeros 3 digitos) —
/// NUNCA el titular de la cuenta, porque esa info no esta disponible por ninguna
/// API publica (se lo explicamos al usuario en el frontend, no ac).
@Service
public class AliasCobroService {

    private static final Pattern PATRON_ALIAS = Pattern.compile("^[A-Za-z0-9.\\-]{6,20}$");

    /// Codigos de entidad (BCRA) mas comunes. Lista no exhaustiva a proposito:
    /// si el codigo no esta, se informa igual como "banco no identificado" sin
    /// invalidar el CBU/CVU (el digito verificador ya lo confirma matematicamente).
    private static final Map<String, String> BANCOS_POR_CODIGO = Map.ofEntries(
            Map.entry("003", "Banco de la Nacion Argentina"),
            Map.entry("007", "Banco de Galicia"),
            Map.entry("011", "Banco de la Nacion Argentina"),
            Map.entry("014", "Banco de la Provincia de Buenos Aires"),
            Map.entry("015", "Industrial and Commercial Bank of China"),
            Map.entry("017", "BBVA Argentina"),
            Map.entry("020", "Banco de Corrientes"),
            Map.entry("027", "Banco Suprvielle"),
            Map.entry("034", "Banco Patagonia"),
            Map.entry("072", "Banco Santander Argentina"),
            Map.entry("143", "Brubank"),
            Map.entry("191", "Banco Credicoop"),
            Map.entry("198", "Banco de Valores"),
            Map.entry("285", "Banco Macro"),
            Map.entry("299", "Banco Comafi"),
            Map.entry("330", "Nuevo Banco de Santa Fe"),
            Map.entry("389", "Banco Columbia"),
            Map.entry("431", "Banco Coinag")
            // Nota: no incluyo el codigo de Mercado Pago ni otras fintech/billeteras
            // por no tener 100% de certeza del codigo vigente — mejor no mostrar un
            // banco incorrecto que dejarlo como "no identificado" (ver metodo de abajo).
    );

    public AliasCobroInfoDTO validar(String valor) {
        if (valor == null || valor.isBlank()) {
            return new AliasCobroInfoDTO(false, null, null, "El alias/CBU/CVU no puede estar vacio.");
        }

        String limpio = valor.trim();

        if (limpio.matches("^\\d{22}$")) {
            return validarCbuOCvu(limpio);
        }

        if (PATRON_ALIAS.matcher(limpio).matches()) {
            return new AliasCobroInfoDTO(true, "ALIAS", null,
                    "Formato de alias valido. No podemos confirmar a que cuenta apunta ni quien es el titular: verificalo con cuidado.");
        }

        return new AliasCobroInfoDTO(false, null, null,
                "No parece un CBU/CVU (22 digitos) ni un alias valido (6-20 caracteres, letras/numeros/puntos/guiones).");
    }

    private AliasCobroInfoDTO validarCbuOCvu(String cbu) {
        if (!digitosVerificadoresValidos(cbu)) {
            return new AliasCobroInfoDTO(false, "CBU_CVU", null,
                    "El numero tiene 22 digitos pero el digito verificador no cierra: revisa que este bien tipeado.");
        }
        String codigoEntidad = cbu.substring(0, 3);
        String banco = BANCOS_POR_CODIGO.get(codigoEntidad);
        String mensaje = banco != null
                ? "Cuenta valida. No podemos confirmar el titular: verifica que sea tuya antes de guardarla."
                : "Cuenta con formato valido, pero no reconocemos el banco/billetera (codigo " + codigoEntidad + "). Verificala igual.";
        return new AliasCobroInfoDTO(true, "CBU_CVU", banco, mensaje);
    }

    /// Algoritmo estandar de digito verificador de CBU (BCRA): dos bloques de 8 y 14
    /// digitos, cada uno con su propio DV calculado con pesos fijos.
    private boolean digitosVerificadoresValidos(String cbu) {
        int[] pesosBloque1 = {7, 1, 3, 9, 7, 1, 3};
        int[] pesosBloque2 = {3, 9, 7, 1, 3, 9, 7, 1, 3, 9, 7, 1, 3};

        int sumaBloque1 = 0;
        for (int i = 0; i < 7; i++) {
            sumaBloque1 += Character.getNumericValue(cbu.charAt(i)) * pesosBloque1[i];
        }
        int dv1Esperado = (10 - (sumaBloque1 % 10)) % 10;
        int dv1Real = Character.getNumericValue(cbu.charAt(7));

        int sumaBloque2 = 0;
        for (int i = 0; i < 13; i++) {
            sumaBloque2 += Character.getNumericValue(cbu.charAt(8 + i)) * pesosBloque2[i];
        }
        int dv2Esperado = (10 - (sumaBloque2 % 10)) % 10;
        int dv2Real = Character.getNumericValue(cbu.charAt(21));

        return dv1Esperado == dv1Real && dv2Esperado == dv2Real;
    }
}
