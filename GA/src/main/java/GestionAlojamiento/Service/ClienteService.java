package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ClienteModificarDTO;
import GestionAlojamiento.DTO.ClienteRegistroDTO;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ClienteRepository;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    //------------------------ LISTAR POR ------------------------
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "usuario.nombre"));
    }

    public List<Cliente> listarPorMetodoPago(String metodo_pago) {
        return clienteRepository.findByMetodoPago(metodo_pago.toLowerCase());
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
    }

    //------------------------ GUARDAR/BORRAR/CREAR ------------------------

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

    @Transactional
    public void borrarPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado en la base de datos");
        }
        clienteRepository.deleteById(id);
    }

    //------------------------ MODIFICAR ------------------------
    @Transactional
    public Cliente actualizar(ClienteModificarDTO clienteModificarDTO) {
        Cliente cliente = clienteRepository.findById(clienteModificarDTO.getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
        Usuario usuario = cliente.getUsuario();

        if (clienteModificarDTO.getEmail() != null) {
            usuario.setEmail(clienteModificarDTO.getEmail());
        }
        if (clienteModificarDTO.getTelefono() != null) {
            usuario.setTelefono(clienteModificarDTO.getTelefono());
        }
        if (clienteModificarDTO.getPassword() != null) {
            usuario.setPassword(clienteModificarDTO.getPassword());
        }
        if (clienteModificarDTO.isActivo() != usuario.isActivo()) {
            usuario.setActivo(clienteModificarDTO.isActivo());
        }

        if (clienteModificarDTO.getMetodoPago() != null) {
            cliente.setMetodoPago(clienteModificarDTO.getMetodoPago());
        }

        return clienteRepository.save(cliente);
    }
}
