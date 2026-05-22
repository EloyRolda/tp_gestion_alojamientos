package GestionAlojamiento.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    //No tomar como validas SON DE PRUEBA
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<?> manejarIdNoEncontrado(IdNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<?> manejarParametroInvalido(ParametroInvalidoException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(TestException.class)
    public ResponseEntity<?> TestExeptionHandler(TestException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
