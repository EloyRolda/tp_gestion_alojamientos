package GestionAlojamiento.Config;


import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) {

        Usuario user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No encontrado"));

        String rol = switch (user.getTipoUsuario()) {
            case ADMINISTRADOR -> "ROLE_ADMIN";
            case CLIENTE -> "ROLE_CLIENTE";
            case ANFITRION -> "ROLE_ANFITRION";
        };

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(rol))
        );
    }
}