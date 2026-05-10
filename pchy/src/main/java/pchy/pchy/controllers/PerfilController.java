package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pchy.pchy.models.Usuario;
import pchy.pchy.service.UsuarioService;

import java.io.IOException;
import java.nio.file.*;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @Value("${pchy.uploads.ruta}")
    private String rutaFotos;

    @Value("${pchy.uploads.url}")
    private String urlPublica;

    // ─── Ver perfil ────────────────────────────────────────────────
    @GetMapping("/Perfil")
    public String verPerfil(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) return "redirect:/Login";

        Usuario perfil = usuarioService.obtenerPerfil(usuario.getCorreo());
        int rol = (int) session.getAttribute("rol");

        model.addAttribute("usuario", perfil);
        model.addAttribute("rol", rol);

        return "perfil";
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
    @PostMapping("/Perfil/Foto")
    public String subirFoto(
            @RequestParam("foto") MultipartFile foto,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) return "redirect:/Login";
        if (foto.isEmpty())  return "redirect:/Perfil?fotoError";

        try {
            // Crea la carpeta si no existe
            Files.createDirectories(Paths.get(rutaFotos));

            // Nombre único por usuario
            String extension = foto.getOriginalFilename()
                .substring(foto.getOriginalFilename().lastIndexOf("."));
            String nombreArchivo = "perfil_" + usuario.getIdUsuario() + extension;

            // Guarda el archivo físico
            Path destino = Paths.get(rutaFotos + nombreArchivo);
            foto.transferTo(destino);

            // Ruta que se guarda en BD  (/uploads/perfiles/perfil_5.jpg)
            String rutaBD = urlPublica + nombreArchivo;
            usuarioService.actualizarFoto(usuario.getCorreo(), rutaBD);

            // Refresca sesión
            Usuario actualizado = usuarioService.obtenerPerfil(usuario.getCorreo());
            session.setAttribute("usuario", actualizado);

        } catch (IOException e) {
            return "redirect:/Perfil?fotoError";
        }

        return "redirect:/Perfil?fotoOk";
    }
}