package com.eloyroldan.tp_final_alojamiento.backend.Exception;

/// Se lanza cuando se intenta crear algo que ya existe segun una regla de
/// unicidad de la aplicacion (email, telefono, usuario_login, nombre de un
/// catalogo). GlobalExceptionHandler la mapea a 409.
public class RecursoDuplicadoException extends RuntimeException {
    public RecursoDuplicadoException(String message) {
        super(message);
    }
}
