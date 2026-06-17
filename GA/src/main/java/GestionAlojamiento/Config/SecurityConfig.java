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
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ── CUALQUIER AUTENTICADO ─────────────────────────────
                        .requestMatchers("/Usuario/me").authenticated()

                        // ── SOLO ADMIN — ENDPOINTS ────────────────────────────
                        .requestMatchers(
                                "/Usuario/listar",
                                "/Usuario/listar/clientes",
                                "/Usuario/listar/anfitriones",
                                "/Usuario/listar/administradores",
                                "/Usuario/eliminar/**",
                                "/Usuario/registrar/administrador",
                                "/Usuario/actualizar/admin",
                                "/Reserva/listar"
                        ).hasRole("ADMIN")

                        // ── ADMIN y el propio usuario ──────────────────────────
                        .requestMatchers(
                                "/Usuario/mostrar/**",
                                "/Usuario/actualizar"
                        ).hasAnyRole("ADMIN", "CLIENTE", "ANFITRION")

                        // ── ALOJAMIENTOS — LISTAR (todos los roles) ───────────
                        .requestMatchers(
                                "/Casa/listar",         "/Casa/mostrar/**",
                                "/Hotel/listar",        "/Hotel/mostrar/**",
                                "/Departamento/listar", "/Departamento/mostrar/**"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")

                        // ── ALOJAMIENTOS — PROPIOS (admin + anfitrion) ────────
                        .requestMatchers(
                                "/Casa/listar/propios",
                                "/Hotel/listar/propios",
                                "/Departamento/listar/propios"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        // ── ALOJAMIENTOS — REGISTRAR / MODIFICAR ─────────────
                        .requestMatchers(
                                "/Casa/registrar",
                                "/Hotel/registrar",
                                "/Departamento/registrar"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        .requestMatchers(
                                "/Casa/actualizar",         "/Casa/eliminar/**",
                                "/Hotel/actualizar",        "/Hotel/eliminar/**",
                                "/Departamento/actualizar", "/Departamento/eliminar/**"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        // ── RESERVAS ──────────────────────────────────────────
                        .requestMatchers("/Reserva/listar/propios").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/Reserva/listar/anfitrion").hasAnyRole("ADMIN", "ANFITRION")
                        .requestMatchers("/Reserva/mostrar/**").hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Reserva/registrar").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/Reserva/actualizar", "/Reserva/eliminar/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/Reserva/finalizar/**").hasAnyRole("ADMIN", "ANFITRION")
                        // ── REVIEWS ───────────────────────────────────────────
                        .requestMatchers(
                                "/Review/listar",
                                "/Review/mostrar/**",
                                "/Review/alojamiento/**"
                        ).hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Review/cliente/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/Review/registrar").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(
                                "/Review/actualizar",
                                "/Review/eliminar/**"
                        ).hasAnyRole("ADMIN", "CLIENTE")

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