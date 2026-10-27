package com.eloyroldan.tp_final_alojamiento.backend.Exception;

/// Se lanza ante una regla de negocio violada con datos que en si mismos son
/// validos (a diferencia de un error de formato, que ya frena @Valid antes de
/// llegar al Service). Ej: fechas de reserva superpuestas, cancelar algo que ya
/// esta cancelado. GlobalExceptionHandler la mapea a 400.
public class ParametroInvalidoException extends RuntimeException {
    public ParametroInvalidoException(String message) {
        super(message);
    }
}
