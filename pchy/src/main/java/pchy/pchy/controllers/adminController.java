package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pchy.pchy.models.Clase;
import pchy.pchy.models.Usuario;
import pchy.pchy.service.ClaseService;

import java.util.List;

@Controller
public class adminController {

    @Autowired
    private ClaseService claseService;

    // Esto es para el inicio:
    @GetMapping("/Administrador/Inicio")
    public String principalAdmin(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        int rol = (int) session.getAttribute("rol");
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);

        return "Administrador/principalAdmin";
    }

    // Esto es para el perfil:
    @GetMapping("/Administrador/Perfil")
    public String perfilAdmin() {
        return "redirect:/Perfil";
    }

    // Esto que sigue para las clases:
    //La lista de cuales son y en cuales esta
    @GetMapping("/Administrador/Clases")
    public String clasesAdmin(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        List<Clase> clases = claseService.listarMisClases(usuario.getIdUsuario());
        model.addAttribute("clases", clases);

        return "Administrador/Clases/clasesadmin";
    }

    // Para crear una nueva clase
    @GetMapping("/Administrador/Nueva/Clase")
    public String nuevaClase(HttpSession session) {
        if (session.getAttribute("usuario") == null) return "redirect:/Login";
        return "Administrador/Clases/nuevaClase";
    }

    // El post para que se haga la clase
    @PostMapping("/Administrador/Nueva/Clase")
    public String crearClase(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            claseService.crearClase(nombre, descripcion, usuario.getIdUsuario());
            return "redirect:/Administrador/Clases?ok";
        } catch (Exception e) {
            return "redirect:/Administrador/Nueva/Clase?error";
        }
    }

    // Para ver dentro de cada una de las clases
@GetMapping("/Administrador/Clase/{id}")
public String verClase(
        @PathVariable int id,
        HttpSession session,
        Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    try {
        Clase clase = claseService.obtenerClase(id, usuario.getIdUsuario());
        model.addAttribute("clase", clase);
        model.addAttribute("urlClase", baseUrl + "/unirse/" + clase.getCodigo());
    } catch (Exception e) {
        return "redirect:/Administrador/Clases";
    }

    return "Administrador/Clases/Enclase";
}

    // Metodo para que se edite
    @PostMapping("/Administrador/Clase/{id}/Editar")
    public String editarClase(
            @PathVariable int id,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            claseService.editarClase(id, nombre, descripcion, usuario.getIdUsuario());
            return "redirect:/Administrador/Clase/" + id + "?editOk";
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + id + "?editError";
        }
    }

    //Deleteeeeeeeeeeeeeeeeeeeeeeeeeee
    @PostMapping("/Administrador/Clase/{id}/Eliminar")
    public String eliminarClase(
            @PathVariable int id,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        claseService.eliminarClase(id, usuario.getIdUsuario());
        return "redirect:/Administrador/Clases?eliminada";
    }

    @Value("${pchy.base-url}")
    private String baseUrl;

    
  
    // Existen: 
    @GetMapping("/Administrador/Registro")
    public String registroAdmin() { return "Administrador/registroAdmin"; }

    @GetMapping("/Administrador/Revision")
    public String revisionCompetencia() { return "Administrador/Clases/revisionCompetencia"; }

    @GetMapping("/Administrador/Competencias/competencia")
    public String EditarCompetencia() { return "Administrador/Competencias/competencia"; }

    @GetMapping("/Administrador/Competencias/Revision")
    public String revisionCompetenciaDetalle() { return "Administrador/Competencias/revision"; }
}