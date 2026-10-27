package com.eloyroldan.tp_final_alojamiento.backend.Exception;

import org.springframework.security.core.AuthenticationException;

/// Se lanza desde CustomUserDetailsService cuando el usuario existe y la
/// contrasenia seria valida, pero todavia no verifico su correo. Al extender
/// AuthenticationException (y no UsernameNotFoundException), Spring Security no
/// la reescribe como BadCredentialsException generico: llega intacta hasta
/// GlobalExceptionHandler con su mensaje especifico.
public class CuentaNoVerificadaException extends AuthenticationException {
    public CuentaNoVerificadaException(String mensaje) {
        super(mensaje);
    }
}
