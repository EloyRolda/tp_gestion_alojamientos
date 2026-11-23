package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.LoginDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.LoginResponse;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReenviarVerificacionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.CuentaSuspendidaException;
import com.eloyroldan.tp_final_alojamiento.backend.Service.JwtService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.VerificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final VerificacionService verificacionService;
    private final UsuarioService usuarioService;

    /// El identificador puede ser email, usuario_login o telefono. Si el usuario
    /// no verifico su correo, CustomUserDetailsService corta ahi mismo con
    /// CuentaNoVerificadaException (ver GlobalExceptionHandler).
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getIdentificador(), request.getPassword()));
        } catch (DisabledException e) {
            Optional<String> mensaje = usuarioService.mensajeDeSuspension(
                    request.getIdentificador(), request.getPassword());
            if (mensaje.isPresent()) {
                throw new CuentaSuspendidaException(mensaje.get());
            }
            throw new BadCredentialsException("Usuario o contrasenia invalidos.");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getIdentificador());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    /// El front llega aca desde el link del mail: /verificar-cuenta?token=XXXX.
    @GetMapping("/verificar")
    public ResponseEntity<String> verificar(@RequestParam String token) {
        verificacionService.verificarToken(token);
        return ResponseEntity.ok("Cuenta verificada correctamente. Ya puede iniciar sesion.");
    }

    /// Por si el token vencio (24hs) o el correo nunca llego.
    @PostMapping("/reenviar-verificacion")
    public ResponseEntity<String> reenviarVerificacion(@Valid @RequestBody ReenviarVerificacionDTO dto) {
        verificacionService.reenviarVerificacion(dto.getEmail());
        return ResponseEntity.ok("Se reenvio el correo de verificacion.");
    }
}
