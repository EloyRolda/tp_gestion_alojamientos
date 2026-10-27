package com.eloyroldan.tp_final_alojamiento.backend.Config;

import com.eloyroldan.tp_final_alojamiento.backend.Exception.CuentaNoVerificadaException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repo;

    /// "identificador" puede ser el email, el usuario_login o el telefono. El
    /// UserDetails resultante siempre usa el email como username interno, para que
    /// el resto del sistema (JWT, Authentication.getName()) trabaje con un valor
    /// estable sin importar con que campo se logueo la persona.
    @Override
    public UserDetails loadUserByUsername(String identificador) {

        Usuario user = repo.findByEmailOrUsuarioLoginOrTelefono(identificador, identificador, identificador)
                .orElseThrow(() -> new UsernameNotFoundException("No encontrado"));

        // Bloqueo total: no se arma ni siquiera el UserDetails hasta que verifique
        // el correo. Se corta aca (no con .disabled()) para poder dar un mensaje
        // especifico en vez del generico "cuenta deshabilitada".
        if (!user.getEmailVerificado()) {
            throw new CuentaNoVerificadaException(
                    "Debe verificar su correo electronico antes de iniciar sesion. Revise su casilla o solicite un nuevo enlace.");
        }

        String rol = switch (user.getTipo()) {
            case ADMIN -> "ROLE_ADMIN";
            case CLIENTE -> "ROLE_CLIENTE";
            case ANFITRION -> "ROLE_ANFITRION";
        };

        // disabled(true) hace que Spring Security rechace el login con
        // DisabledException antes de comparar la contrasenia: un usuario dado de
        // baja (activo = false) no puede autenticarse aunque conozca su clave.
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(rol))
                .disabled(!user.getActivo())
                .build();
    }
}
