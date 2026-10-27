package com.eloyroldan.tp_final_alojamiento.backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/// Matriz de autorizacion por endpoint (stateless, JWT). Regla general: cada
/// bloque de aca abajo cubre un Controller, restringido por rol via hasRole/
/// hasAnyRole. El chequeo de "es el dueño del recurso" (ej: un cliente solo
/// puede cancelar SU reserva) no vive aca — eso lo resuelve cada Service/
/// Controller puntual, esto solo filtra por ROL, no por pertenencia.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /// Origenes permitidos para CORS, separados por coma (ej: "https://alojame.com,https://staging.alojame.com").
    /// Por defecto, solo el frontend configurado (app.frontend-url) — nunca "*"
    /// combinado con allowCredentials(true): esa combinacion deja que cualquier
    /// sitio externo haga requests autenticados (con el JWT) contra esta API
    /// desde el browser del usuario.
    @Value("${cors.allowed-origins}")
    private String corsAllowedOrigins;

    /// CSRF deshabilitado a proposito: la API es stateless (JWT en el header,
    /// sin cookies de sesion), asi que no aplica la proteccion CSRF clasica
    /// pensada para apps que autentican por cookie.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService,
                                                     JwtAuthFilter jwtAuthFilter,
                                                     AuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth

                        // ── PUBLICO ──────────────────────────────────────────
                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/*.html",
                                "/auth/**",
                                "/Usuario/registrar",
                                "/Pago/webhook",
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ── USUARIO ──────────────────────────────────────────
                        .requestMatchers("/Usuario/me").authenticated()
                        .requestMatchers(
                                "/Usuario/listar",
                                "/Usuario/listar/clientes",
                                "/Usuario/listar/anfitriones",
                                "/Usuario/listar/administradores",
                                "/Usuario/registrar/administrador",
                                "/Usuario/actualizar/admin",
                                "/Usuario/eliminar/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/Usuario/mostrar/**",
                                "/Usuario/actualizar"
                        ).hasAnyRole("ADMIN", "CLIENTE", "ANFITRION")
                        .requestMatchers("/Usuario/alias-cobro/**").hasAnyRole("ADMIN", "ANFITRION")

                        // ── DIRECCION ────────────────────────────────────────
                        .requestMatchers("/Direccion/listar", "/Direccion/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/Direccion/mostrar/**", "/Direccion/buscar").hasAnyRole("ADMIN", "ANFITRION")

                        // ── TIPO ALOJAMIENTO ─────────────────────────────────
                        .requestMatchers("/TipoAlojamiento/listar").hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/TipoAlojamiento/reemplazar", "/TipoAlojamiento/eliminar/**").hasRole("ADMIN")

                        // ── SERVICIO ─────────────────────────────────────────
                        .requestMatchers("/Servicio/listar").hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Servicio/reemplazar", "/Servicio/eliminar/**").hasRole("ADMIN")

                        // ── ALOJAMIENTO ──────────────────────────────────────
                        .requestMatchers(
                                "/Alojamiento/listar", "/Alojamiento/mostrar/**",
                                "/Alojamiento/*/servicios"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Alojamiento/listar/propios").hasAnyRole("ADMIN", "ANFITRION")
                        .requestMatchers("/Alojamiento/registrar").hasAnyRole("ADMIN", "ANFITRION")
                        .requestMatchers(
                                "/Alojamiento/actualizar", "/Alojamiento/eliminar/**",
                                "/Alojamiento/*/servicios/**"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        // ── GALERIA ──────────────────────────────────────────
                        .requestMatchers(
                                "/Galeria/alojamiento/**",
                                "/Galeria/*/imagenes"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Galeria/upload", "/Galeria/imagen/**").hasAnyRole("ADMIN", "ANFITRION")

                        // ── RESERVA ──────────────────────────────────────────
                        .requestMatchers("/Reserva/listar", "/Reserva/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/Reserva/listar/propios", "/Reserva/registrar", "/Reserva/actualizar")
                            .hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/Reserva/listar/anfitrion", "/Reserva/confirmar/**", "/Reserva/finalizar/**")
                            .hasAnyRole("ADMIN", "ANFITRION")
                        .requestMatchers("/Reserva/mostrar/**", "/Reserva/cancelar/**")
                            .hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")

                        // ── PAGO ─────────────────────────────────────────────
                        // (/Pago/webhook ya esta en PUBLICO, mas arriba)
                        .requestMatchers("/Pago/reserva/**").hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Pago/reembolso-admin").hasRole("ADMIN")

                        // ── ESTADISTICAS ─────────────────────────────────────
                        .requestMatchers("/Estadisticas/admin/**").hasRole("ADMIN")
                        .requestMatchers("/Estadisticas/anfitrion/**").hasAnyRole("ADMIN", "ANFITRION")
                        .requestMatchers("/Estadisticas/cliente/**").hasAnyRole("ADMIN", "CLIENTE")

                        // ── REVIEW ───────────────────────────────────────────
                        .requestMatchers("/Review/listar").hasRole("ADMIN")
                        .requestMatchers(
                                "/Review/mostrar/**",
                                "/Review/alojamiento/**"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Review/cliente/**", "/Review/registrar", "/Review/eliminar/**")
                            .hasAnyRole("ADMIN", "CLIENTE")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origenes = Arrays.stream(corsAllowedOrigins.split(","))
                .map(String::trim)
                .filter(origen -> !origen.isBlank())
                .toList();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origenes);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
