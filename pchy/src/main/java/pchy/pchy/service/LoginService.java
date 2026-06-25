package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

import pchy.pchy.models.Usuario;
import pchy.pchy.repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String autenticar(
            String correo,
            String contrasena,
            HttpSession session) {

        Usuario usuario = usuarioRepository.buscarPorCorreo(correo);

        if (usuario == null) {
            return "redirect:/Login?error";
        }

        if (!encoder.matches(
                contrasena,
                usuario.getContrasena())) {

            return "redirect:/Login?error";
        }

        session.setAttribute("usuario", usuario);

        int rol = usuarioRepository.obtenerRol(usuario.getIdUsuario());

        session.setAttribute("rol", rol); 

        if (rol == 1)
            return "redirect:/Administrador/Inicio";

        if (rol == 2)
            return "redirect:/Administrador/Inicio";

        return "redirect:/Alumno/Inicio";
    }

}