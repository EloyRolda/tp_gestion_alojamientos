package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ClienteRepository;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    //------------------------ LISTAR POR ------------------------
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "usuario.nombre"));
    }

    public List<Cliente> listarPorMetodoPago(String metodo_pago) {
        return clienteRepository.findByMetodo_Pago(metodo_pago.toLowerCase());
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
    }

    //------------------------ GUARDAR/BORRAR/CREAR ------------------------
    @Transactional
    public Cliente crear(Long idUsuario, String metodoDePago) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
        if (clienteRepository.existsById(usuario.getId())) {
            throw new RuntimeException("El usuario ya es un cliente");
        }
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setMetodo_pago(metodoDePago.toLowerCase());
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
    public Cliente actualizarMetodoPago(Long id, String nuevoMetodo) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
        cliente.setMetodo_pago(nuevoMetodo);
        return clienteRepository.save(cliente);
    }
}
