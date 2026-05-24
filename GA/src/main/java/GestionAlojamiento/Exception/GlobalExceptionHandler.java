package GestionAlojamiento.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(IdNoEncontradoException.class)
    public ResponseEntity<?> manejarIdNoEncontrado(IdNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<?> manejarParametroInvalido(ParametroInvalidoException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }



}
