package GestionAlojamiento.Service;


import GestionAlojamiento.DTO.ClienteModificarDTO;
import GestionAlojamiento.DTO.ClienteRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }


    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Usuario no encontrado en la base de datos"));
    }


    //---------------------------------------- CREAR ----------------------------------------

    @Transactional
    public Cliente crear(ClienteRegistroDTO clienteRegistroDTO) {
        Usuario usuario = new Usuario();
        usuario.setActivo(true);//valor por defecto
        usuario.setEmail(clienteRegistroDTO.getEmail());
        usuario.setNombre(clienteRegistroDTO.getNombre());
        usuario.setPassword(clienteRegistroDTO.getPassword());
        usuario.setTelefono(clienteRegistroDTO.getTelefono());
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuarioService.crear(usuario));
        cliente.setMetodoPago(clienteRegistroDTO.getMetodoPago());
        return clienteRepository.save(cliente);
    }

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IdNoEncontradoException("Usuario no encontrado en la base de datos");
        }
        clienteRepository.deleteById(id);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Cliente actualizar(ClienteModificarDTO clienteModificarDTO) {
        Cliente cliente = clienteRepository.findById(clienteModificarDTO.getId()).orElseThrow(() -> new IdNoEncontradoException("Usuario no encontrado en la base de datos"));

        Usuario usuario = cliente.getUsuario();
        usuario = usuarioService.modificarObjeto(usuario, mapearUsuario(clienteModificarDTO));

        if (clienteModificarDTO.getMetodoPago() != null) {
            cliente.setMetodoPago(clienteModificarDTO.getMetodoPago());
        }
        cliente.setUsuario(usuario);
        return clienteRepository.save(cliente);
    }
//---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    /// Mapea el DTO de MODIFICAR a un USUARIO y lo RETORNA
    private Usuario mapearUsuario(ClienteRegistroDTO clienteRegistroDTO) {
        Usuario usuario = new Usuario(
                null,
                clienteRegistroDTO.getNombre(),
                clienteRegistroDTO.getEmail(),
                clienteRegistroDTO.getPassword(),
                clienteRegistroDTO.getTelefono(),
                LocalDateTime.now(),
                true,
                TipoUsuario.CLIENTE
        );
        return usuario;
    }

    /// Mapea el DTO de MODIFICAR a un USUARIO y lo RETORNA
    private Usuario mapearUsuario(ClienteModificarDTO clienteModificarDTO) {
        Usuario usuario = new Usuario(
                clienteModificarDTO.getId(),
                clienteModificarDTO.getNombre(),
                clienteModificarDTO.getEmail(),
                clienteModificarDTO.getPassword(),
                clienteModificarDTO.getTelefono(),
                null,   //Fecha registro
                clienteModificarDTO.getActivo(),
                TipoUsuario.CLIENTE
        );
        return usuario;
    }
}
