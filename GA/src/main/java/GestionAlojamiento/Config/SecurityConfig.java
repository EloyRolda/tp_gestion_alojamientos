package GestionAlojamiento.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   UserDetailsService userDetailsService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth

                        // ── PÚBLICO ──────────────────────────────────────────
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/registrarse.html",
                                "/403.html",
                                "/login",
                                "/Usuario/registrar",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ── CUALQUIER AUTENTICADO ─────────────────────────────
                        .requestMatchers("/Usuario/me").authenticated()

                        // ── PÁGINAS ESTÁTICAS — TODOS LOS ROLES ──────────────
                        .requestMatchers(
                                "/home.html",
                                "/mi-perfil.html",
                                "/actualizar-perfil.html",
                                "/registrar-reserva.html",
                                "/actualizar-reserva.html",
                                "/cancelar-reserva.html",
                                "/registrar-review.html",
                                "/editar-review.html",
                                "/eliminar-review.html",
                                "/listar-alojamientos.html",
                                "/listar-review.html"
                        ).hasAnyRole("ADMIN", "CLIENTE", "ANFITRION")

                        // ── PÁGINAS ESTÁTICAS — ADMIN y ANFITRION ────────────
                        .requestMatchers(
                                "/registrar-casa.html",
                                "/registrar-hotel.html",
                                "/registrar-departamento.html",
                                "/actualizar-casa.html",
                                "/actualizar-hotel.html",
                                "/actualizar-departamento.html",
                                "/eliminar-alojamiento.html"
                        ).hasAnyRole("ADMIN", "ANFITRION")

                        // ── PÁGINAS ESTÁTICAS — SOLO ADMIN ───────────────────
                        .requestMatchers(
                                "/listar-usuarios.html",
                                "/listar-reservas.html",
                                "/admin-editar-usuario.html",
                                "/eliminar-usuario.html"
                        ).hasRole("ADMIN")

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
                                "/Casa/listar",           "/Casa/mostrar/**",
                                "/Hotel/listar",          "/Hotel/mostrar/**",
                                "/Departamento/listar",   "/Departamento/mostrar/**"
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
                        .requestMatchers("/Reserva/mostrar/**").hasAnyRole("ADMIN", "ANFITRION", "CLIENTE")
                        .requestMatchers("/Reserva/registrar").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(
                                "/Reserva/actualizar",
                                "/Reserva/eliminar/**"
                        ).hasAnyRole("ADMIN", "CLIENTE")

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
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login.html")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/403.html")
                        )
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login.html")
                        )
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}