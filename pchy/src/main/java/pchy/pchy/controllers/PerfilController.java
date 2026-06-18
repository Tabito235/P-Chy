package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pchy.pchy.models.Usuario;
import pchy.pchy.service.CloudinaryService;
import pchy.pchy.service.RankingService;
import pchy.pchy.service.UsuarioService;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;
  @Autowired
private RankingService rankingService;

    // ─── Ver perfil ────────────────────────────────────────────────

@GetMapping("/Perfil")
public String verPerfil(HttpSession session, Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    Usuario perfil = usuarioService.obtenerPerfil(usuario.getCorreo());
    int rol = (int) session.getAttribute("rol");

    model.addAttribute("usuario", perfil);
    model.addAttribute("rol", rol);

    // Solo para alumnos
    if (rol == 3) {
        model.addAttribute("medalla",
            rankingService.calcularMedalla(perfil.getPuntaje()));
    }

    return "perfil"; // ← siempre la misma vista
}



    // ─── Actualizar datos personales ───────────────────────────────
@PostMapping("/Perfil/Actualizar")
public String actualizarPerfil(
        @RequestParam String nombre,
        @RequestParam String apellido,
        @RequestParam(required = false) String fechaNacimiento,
        HttpSession session) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    Usuario datos = new Usuario();
    datos.setNombre(nombre);
    datos.setApellido(apellido);
    // institución no se toca

    if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
        datos.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento));
    }

    usuarioService.actualizarPerfil(usuario.getCorreo(), datos);

    Usuario actualizado = usuarioService.obtenerPerfil(usuario.getCorreo());
    session.setAttribute("usuario", actualizado);

    return "redirect:/Perfil?ok";
}

    // ─── Cambiar contraseña ────────────────────────────────────────
    @PostMapping("/Perfil/Password")
    public String cambiarPassword(
            @RequestParam String actual,
            @RequestParam String nueva,
            @RequestParam String confirmar,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) return "redirect:/Login";

        try {
            usuarioService.cambiarPassword(
                usuario.getCorreo(), actual, nueva, confirmar
            );
            return "redirect:/Perfil?passOk";

        } catch (Exception e) {
            return "redirect:/Perfil?passError=" +
                java.net.URLEncoder.encode(
                    e.getMessage(),
                    java.nio.charset.StandardCharsets.UTF_8
                );
        }
    }

    // ─── Subir foto de perfil ──────────────────────────────────────
    @Autowired
private CloudinaryService cloudinaryService;

@PostMapping("/Perfil/Foto")
public String subirFoto(
        @RequestParam("foto") MultipartFile foto,
        HttpSession session) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");

    if (usuario == null) return "redirect:/Login";
    if (foto.isEmpty())  return "redirect:/Perfil?fotoError";

    try {
        // Nombre único por usuario (sin extensión, Cloudinary la maneja)
        String nombreArchivo = "perfil_" + usuario.getIdUsuario();

        // Sube a Cloudinary y obtiene la URL pública
        String urlPublica = cloudinaryService.subirFoto(foto, nombreArchivo);

        // Guarda la URL en BD
        usuarioService.actualizarFoto(usuario.getCorreo(), urlPublica);

        // Refresca sesión
        Usuario actualizado = usuarioService.obtenerPerfil(usuario.getCorreo());
        session.setAttribute("usuario", actualizado);

    } catch (Exception e) {
        return "redirect:/Perfil?fotoError";
    }

    return "redirect:/Perfil?fotoOk";
}

@GetMapping("/Perfil/{idUsuario}")
public String verPerfilPublico(
        @PathVariable int idUsuario,
        HttpSession session,
        Model model) {

    if (session.getAttribute("usuario") == null)
        return "redirect:/Login";

    try {
        Usuario perfil = usuarioService.obtenerPerfilPorId(idUsuario);
        if (perfil == null) return "redirect:/";

        int rolVisto = usuarioService.obtenerRolPorId(idUsuario);

        model.addAttribute("usuario", perfil);
        model.addAttribute("rol", rolVisto);
        model.addAttribute("viendoOtro", true);

        if (rolVisto == 3) {
            model.addAttribute("medalla",
                rankingService.calcularMedalla(perfil.getPuntaje()));
        }

    } catch (Exception e) {
        return "redirect:/";
    }

    return "perfil"; // ← siempre la misma vista
}

}