package com.eloyroldan.tp_final_alojamiento.backend.Exception;

/// Se lanza cuando se busca una entidad por id y no existe. GlobalExceptionHandler
/// la mapea a 404.
public class IdNoEncontradoException extends RuntimeException {
    public IdNoEncontradoException(String message) {
        super(message);
    }
}
