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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        /*
                                               .requestMatchers("/Casa/listar", "/Casa/mostrar/**").hasRole("CLIENTE")

                                             .requestMatchers("/Departamento/listar", "/Departamento/mostrar/**").hasRole("CLIENTE")
                                             .requestMatchers("/Hotel/listar", "/Hotel/mostrar/**").hasRole("CLIENTE")
                                             .requestMatchers("/Reserva/listar", "/Reserva/mostrar/**").hasRole("CLIENTE")


                                             //ANFITRIONES
                                             .requestMatchers("/Departamento/actualizar", "/Departamento/registrar", "/Departamento/eliminar/**").hasRole("ANFITRION")
                                             .requestMatchers("/Hotel/actualizar", "/Hotel/registrar", "/Hotel/eliminar/**").hasRole("ANFITRION")
                                             .requestMatchers("/Casa/actualizar", "/Casa/registrar", "/Casa/eliminar/**").hasRole("ANFITRION")
                                             .requestMatchers("/Anfitrion/actualizar", "/Anfitrion/eliminar/**").hasRole("ANFITRION")
   */
                        .requestMatchers("/Administrador/**").hasRole("ADMIN")


                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.permitAll())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}