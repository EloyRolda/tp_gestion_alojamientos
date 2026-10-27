package com.eloyroldan.tp_final_alojamiento.backend.Config;

import com.eloyroldan.tp_final_alojamiento.backend.Service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/// Corre ANTES del DispatcherServlet, por fuera del alcance de
/// GlobalExceptionHandler: cualquier excepcion que se le escape acá termina en
/// un 500 crudo del contenedor (Tomcat), no en una respuesta JSON prolija.
/// Por eso todo el parseo/validacion del token esta blindado con try/catch: un
/// JWT vencido, manipulado, o simplemente basura en el header, nunca debe tirar
/// abajo el request — simplemente se trata como "no autenticado" y sigue la
/// cadena normal (que va a terminar en 401/403 segun corresponda la ruta).
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // isEnabled(): sin este chequeo, una cuenta recien desactivada
                // (Usuario.activo=false) sigue autenticando con un JWT emitido
                // antes de la baja hasta que ese token expire solo (24hs).
                if (jwtService.isTokenValid(token, userDetails) && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            // Token vencido, manipulado, mal formado, o firmado con otra clave.
            // Pasa TODOS los dias con tokens vencidos (24hs de vida) — es
            // esperable, no un error del servidor.
            log.debug("Token JWT invalido o vencido, se ignora: {}", e.getMessage());
        } catch (UsernameNotFoundException e) {
            // El token es valido pero el usuario ya no existe (se borro la
            // cuenta despues de emitido el token).
            log.debug("Token JWT valido pero el usuario ya no existe: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
