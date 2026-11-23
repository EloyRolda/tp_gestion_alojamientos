/// Excepción lanzada cuando un usuario con cuenta suspendida intenta iniciar
/// sesión con la contraseña correcta. Permite que el manejador global devuelva
/// un 403 con el detalle de la suspensión, sin revelar cuentas existentes.
package com.eloyroldan.tp_final_alojamiento.backend.Exception;

public class CuentaSuspendidaException extends RuntimeException {
    public CuentaSuspendidaException(String message) {
        super(message);
    }
}
