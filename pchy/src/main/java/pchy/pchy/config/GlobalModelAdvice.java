package pchy.pchy.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import pchy.pchy.models.Usuario;
import pchy.pchy.repository.UsuarioRepository;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${pchy.base-url}")
    private String baseUrl;

    @ModelAttribute
    public void agregarUsuarioAlModelo(
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer rol     = (Integer) session.getAttribute("rol");

        if (usuario != null) {
            Usuario completo = usuarioRepository.obtenerPerfilCompleto(
                usuario.getCorreo()
            );
            if (completo != null) {
                model.addAttribute("usuario", completo);
                session.setAttribute("usuario", completo);
            }
        }

        if (rol != null) {
            model.addAttribute("rol", rol);
        }

        // Disponible en todas las vistas
        model.addAttribute("baseUrl", baseUrl);
    }
}