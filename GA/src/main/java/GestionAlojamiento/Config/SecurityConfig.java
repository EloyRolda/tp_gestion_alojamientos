package GestionAlojamiento.Config;

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

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

                        // ── PÚBLICO ──────────────────────────────────────────
                        .requestMatchers(
                                "/",
                                "/favicon.ico",
                                "/*.html",
                                "/login",
                                "/auth/login",
                                "/Usuario/registrar",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/ws/**" // handshake de WebSocket: la autenticacion real ocurre en el frame CONNECT (ver WebSocketConfig)
                        ).permitAll()

                        // ── CUALQUIER AUTENTICADO ─────────────────────────────
                        .requestMatchers("/Usuario/me", "/Usuario/foto").authenticated()

                        // ── SOLO ADMIN — ENDPOINTS ────────────────────────────
                        .requestMatchers(
                                "/Usuario/listar",
                                "/Usuario/listar/clientes",
                                "/Usuario/listar/anfitriones",
                                "/Usuario/listar/administradores",
                                "/Usuario/eliminar/**",
                                "/Usuario/registrar/administrador",
                                "/Usuario/actualizar/admin",
                                "/Reserva/listar",
                                "/Log/**",
                                "/Reporte/listar",
                                "/Reporte/listar/pendientes",
                                "/Reporte/*/estado",
                                "/Amenity/registrar",
                                "/Amenity/eliminar/**",
                                "/Chat/listar"
                        ).hasRole("ADMIN")

                        // ── ADMIN y el propio usuario ──────────────────────────
                        .requestMatchers(
                                "/Usuario/mostrar/**",
                                "/Usuario/actualizar"
                        ).hasAnyRole("ADMIN", "CLIENTE", "ANFITRION")

                        // ── ALOJAMIENTOS — LISTAR/BUSCAR (todos los roles) ────
                        .requestMatchers(
                                "/Casa/listar",         "/Casa/mostrar/**",
                                "/Hotel/listar",        "/Hotel/mostrar/**",
                                "/Departamento/listar", "/Departamento/mostrar/**",
                                "/Alojamiento/listar",  "/Alojamiento/mostrar/**", "/Alojamiento/buscar",
                                "/Amenity/listar"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")

                        // ── ALOJAMIENTOS — PROPIOS (admin + anfitrion) ────────
                        .requestMatchers(
                                "/Casa/listar/propios",
                                "/Hotel/listar/propios",
                                "/Departamento/listar/propios"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        // ── ALOJAMIENTOS — REGISTRAR / MODIFICAR / PUBLICAR ───
                        .requestMatchers(
                                "/Casa/registrar",         "/Casa/actualizar",         "/Casa/eliminar/**",
                                "/Hotel/registrar",        "/Hotel/actualizar",        "/Hotel/eliminar/**",
                                "/Departamento/registrar", "/Departamento/actualizar", "/Departamento/eliminar/**",
                                "/Alojamiento/*/publicar", "/Alojamiento/*/despublicar"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        // ── RESERVAS ──────────────────────────────────────────
                        .requestMatchers("/Reserva/listar/propios", "/Reserva/solicitar", "/Reserva/*/cancelar")
                                .hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(
                                "/Reserva/listar/anfitrion",
                                "/Reserva/solicitudes/pendientes",
                                "/Reserva/*/aceptar",
                                "/Reserva/*/rechazar",
                                "/Reserva/finalizar/**",
                                "/Reserva/anfitrion/ingresos-por-mes",
                                "/ReviewHuesped/registrar"
                        ).hasAnyRole("ADMIN", "ANFITRION")
                        .requestMatchers("/Reserva/mostrar/**", "/Reserva/disponibilidad/**")
                                .hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")

                        // ── REVIEWS ───────────────────────────────────────────
                        .requestMatchers(
                                "/Review/listar",
                                "/Review/mostrar/**",
                                "/Review/alojamiento/**",
                                "/ReviewHuesped/huesped/**"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers(
                                "/Review/cliente/**",
                                "/Review/registrar",
                                "/Review/actualizar",
                                "/Review/eliminar/**"
                        ).hasAnyRole("ADMIN", "CLIENTE")

                        // ── PAGO (simulado) ───────────────────────────────────
                        .requestMatchers("/Pago/**").hasAnyRole("ADMIN", "CLIENTE")

                        // ── REPORTES (cualquier autenticado puede reportar) ───
                        .requestMatchers("/Reporte/registrar").hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")

                        // ── NOTIFICACIONES / CHAT / ESTADISTICAS (propios, se valida en el service) ──
                        .requestMatchers("/Notificacion/**", "/Chat/**", "/Estadisticas/**")
                                .hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")

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
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
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
