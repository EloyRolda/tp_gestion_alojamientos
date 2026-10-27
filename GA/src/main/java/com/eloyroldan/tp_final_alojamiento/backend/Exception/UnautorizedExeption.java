package com.eloyroldan.tp_final_alojamiento.backend.Exception;

/// Casos puntuales de "no autorizado" que no encajan en el flujo estandar de
/// Spring Security (ej: no se encontro el usuario autenticado al resolver su
/// identificador). GlobalExceptionHandler la mapea a 401.
public class UnautorizedExeption extends RuntimeException {
    public UnautorizedExeption(String message) {
        super(message);
    }
}
