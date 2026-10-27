package com.eloyroldan.tp_final_alojamiento.backend.Model;

import com.eloyroldan.tp_final_alojamiento.backend.Converter.TipoUsuarioConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/// Representa a cualquier persona registrada en el sistema (cliente, anfitrion o admin).
/// El usuario puede iniciar sesion con email, usuario_login o telefono indistintamente.
//Lombok
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//JPA
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    /// Nombre de usuario para iniciar sesion, alternativo al email/telefono.
    /// Convencion tipica de usernames: letras, numeros, punto y guion bajo, sin espacios.
    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9_.]{3,50}$", message = "El usuario solo puede contener letras, numeros, punto y guion bajo")
    @Column(name = "usuario_login", length = 50, nullable = false, unique = true)
    private String usuarioLogin;

    @NotNull
    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /// Guarda el hash de la contrasenia (bcrypt), nunca texto plano. WRITE_ONLY como
    /// segunda barrera de seguridad, aunque ya no deberiamos serializar el Usuario
    /// directamente (se usa UsuarioResponseDTO, que ni siquiera tiene este campo).
    @NotBlank
    @Size(min = 6, max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @NotBlank
    @Size(max = 30)
    @Column(name = "telefono", length = 30, nullable = false, unique = true)
    private String telefono;

    @NotNull
    @Convert(converter = TipoUsuarioConverter.class)
    @Column(name = "tipo", nullable = false)
    private TIPOUSUARIO tipo;

    /// Bloqueo total mientras sea false: CustomUserDetailsService no deja
    /// loguearse hasta que se confirme el correo (ver TokenVerificacion).
    @NotNull
    @Builder.Default
    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = Boolean.FALSE;

    /// Alias, CBU o CVU (texto libre) a donde se le transfiere al anfitrion via
    /// Payouts de MercadoPago. Nunca se valida titularidad (no es posible por API,
    /// ver charla de diseño), solo formato + identificacion de banco.
    @Column(name = "alias_cobro", length = 255)
    private String aliasCobro;
}
