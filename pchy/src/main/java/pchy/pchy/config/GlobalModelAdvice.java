package pchy.pchy.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import pchy.pchy.models.Usuario;
import pchy.pchy.repository.UsuarioRepository;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @ModelAttribute
    public void agregarUsuarioAlModelo(
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer rol     = (Integer) session.getAttribute("rol");

        if (usuario != null) {
            // Siempre usamos el perfil completo (incluye fotoPerfil)
            Usuario completo = usuarioRepository.obtenerPerfilCompleto(
                usuario.getCorreo()
            );
            if (completo != null) {
                model.addAttribute("usuario", completo);
                // Actualizamos sesión también para que esté siempre fresco
                session.setAttribute("usuario", completo);
            }
        }

        if (rol != null) {
            model.addAttribute("rol", rol);
        }
    }
}