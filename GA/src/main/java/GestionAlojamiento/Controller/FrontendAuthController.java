package GestionAlojamiento.Controller;

import GestionAlojamiento.DTO.ClienteRegistroDTO;
import GestionAlojamiento.DTO.UsuarioLoginDTO;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.ClienteService;
import GestionAlojamiento.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class FrontendAuthController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    @GetMapping({"/", "/login"})
    public String mostrarLogin(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }

        if (!model.containsAttribute("usuarioLoginDTO")) {
            model.addAttribute("usuarioLoginDTO", new UsuarioLoginDTO());
        }
        return "frontend/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute UsuarioLoginDTO usuarioLoginDTO,
                                BindingResult bindingResult,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "frontend/login";
        }

        try {
            Usuario usuario = usuarioService.autenticar(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getPassword());
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/home";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("usuarioLoginDTO", usuarioLoginDTO);
            return "redirect:/login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }

        if (!model.containsAttribute("clienteRegistroDTO")) {
            model.addAttribute("clienteRegistroDTO", new ClienteRegistroDTO());
        }
        return "frontend/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute ClienteRegistroDTO clienteRegistroDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "frontend/registro";
        }

        try {
            clienteService.crear(clienteRegistroDTO);
            redirectAttributes.addFlashAttribute("exito", "Registro creado correctamente. Ya podés iniciar sesión.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("clienteRegistroDTO", clienteRegistroDTO);
            return "redirect:/registro";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "frontend/home";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("exito", "Sesión cerrada correctamente.");
        return "redirect:/login";
    }
}
