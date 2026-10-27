package com.eloyroldan.tp_final_alojamiento.backend.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/// Maneja errores esperados de nuestro dominio primero (los mas especificos),
/// despues errores tipicos de "el usuario mando algo mal formado" (400), y por
/// ultimo un catch-all (500) para cualquier cosa realmente inesperada — asi
/// nunca se le devuelve una traza de Java cruda a quien llama a la API.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //==================== ERRORES DE DOMINIO (los que ya teniamos) ====================

    @ExceptionHandler(IdNoEncontradoException.class)
    public ResponseEntity<?> manejarIdNoEncontrado(IdNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<?> manejarParametroInvalido(ParametroInvalidoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<?> manejarDuplicado(RecursoDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidacion(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
    }

    @ExceptionHandler(UnautorizedExeption.class)
    public ResponseEntity<?> manejarUnautorizedExeption(UnautorizedExeption e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /// Caso especifico: usuario existe y la clave seria correcta, pero no verifico
    /// el correo todavia. Se maneja antes que el catch-all de AuthenticationException
    /// para dar un mensaje puntual en vez del generico.
    @ExceptionHandler(CuentaNoVerificadaException.class)
    public ResponseEntity<?> manejarCuentaNoVerificada(CuentaNoVerificadaException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /// Sin esto, un login con credenciales invalidas explotaba como 500 generico:
    /// authenticationManager.authenticate() en AuthController lanza esta excepcion
    /// (o una subclase) por fuera de la cadena de filtros de Security, asi que no
    /// pasa por el AuthenticationEntryPoint por defecto.
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> manejarUsuarioDeshabilitado(DisabledException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario esta desactivado.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> manejarCredencialesInvalidas(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contrasenia invalidos.");
    }

    /// Catch-all para cualquier otra AuthenticationException no contemplada arriba.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> manejarAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se pudo autenticar: " + e.getMessage());
    }

    //==================== ERRORES DE "EL USUARIO MANDO ALGO MAL FORMADO" (400) ====================

    /// Ej: GET /Alojamiento/mostrar/abc (donde {id} espera un Long). Sin este
    /// handler, Spring lo resuelve solo casi siempre, pero preferimos un mensaje
    /// propio y consistente con el resto de la API en vez del generico de Spring.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> manejarTipoIncorrecto(MethodArgumentTypeMismatchException e) {
        String mensaje = "El parametro '" + e.getName() + "' no tiene el formato esperado"
                + (e.getRequiredType() != null ? " (se esperaba " + e.getRequiredType().getSimpleName() + ")" : "") + ".";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
    }

    /// Ej: mandar un body que no es JSON valido, o un JSON con un tipo que no
    /// coincide (poner "abc" donde el DTO espera un numero).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> manejarJsonMalFormado(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El cuerpo de la solicitud no es JSON valido o no coincide con los campos esperados.");
    }

    /// Ej: GET /Estadisticas/admin/ingresos-mensuales sin el parametro "anio".
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> manejarParametroFaltante(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Falta el parametro obligatorio: " + e.getParameterName());
    }

    /// Ej: PATCH en una ruta que solo acepta GET/POST. Devuelve 405, no 500.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> manejarMetodoNoSoportado(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("El metodo " + e.getMethod() + " no esta permitido en esta ruta.");
    }

    /// Ej: /Alojamient/listar (typo) o cualquier ruta que no existe.
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> manejarRutaInexistente(NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ruta solicitada no existe.");
    }

    /// Se dispara cuando una validacion de la aplicacion (ej: existsByEmail antes
    /// de guardar) no alcanza a frenar un duplicado por una condicion de carrera
    /// (dos registros simultaneos con el mismo email), y termina siendo la propia
    /// restriccion UNIQUE de la base la que lo frena. Sin esto, esa carrera
    /// especifica se veia como un 500 en vez de un mensaje claro.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> manejarIntegridadDeDatos(DataIntegrityViolationException e) {
        log.warn("Violacion de integridad de datos: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("El dato que intentaste guardar ya existe o viola una restriccion de la base (ej: un valor unico duplicado).");
    }

    /// Errores al llamar a MercadoPago o Brevo (caido, timeout, credenciales
    /// invalidas). Se le informa al usuario que el problema es de un servicio
    /// externo, sin exponerle la respuesta cruda del proveedor.
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<?> manejarErrorServicioExterno(RestClientException e) {
        log.error("Error llamando a un servicio externo (MercadoPago/Brevo): {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Hubo un problema con un servicio externo (pagos o correo). Intenta de nuevo en unos minutos.");
    }

    /// Ej: subir una imagen que supera spring.servlet.multipart.max-file-size (10MB).
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<?> manejarArchivoDemasiadoGrande(org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("El archivo es demasiado grande. El limite es de 10MB.");
    }

    /// Ej: /Estadisticas/admin/top-alojamientos?limite=-5 o limite=0 (Pageable no
    /// acepta tamanios de pagina menores a 1) — sin este handler caia en el catch-all (500).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> manejarArgumentoInvalido(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Parametro invalido: " + e.getMessage());
    }

    //==================== CATCH-ALL: cualquier otra cosa no prevista ====================

    /// Ultima red de seguridad: nunca se le devuelve una traza de Java cruda a
    /// quien llama a la API. El detalle real queda en el log del servidor.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarErrorInesperado(Exception e) {
        log.error("Error inesperado no contemplado por ningun handler especifico: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrio un error inesperado. Si el problema persiste, contacta al administrador.");
    }
}
