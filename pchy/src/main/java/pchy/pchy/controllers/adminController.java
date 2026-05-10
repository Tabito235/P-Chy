package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pchy.pchy.models.Usuario;

@Controller
public class adminController {

    @GetMapping("/Administrador/Inicio")
    public String principalAdmin(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        int rol = (int) session.getAttribute("rol");

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);

        return "Administrador/principalAdmin";
    }

    @GetMapping("/Administrador/Perfil")
    public String perfilAdmin() {
        return "redirect:/Perfil";
    }

    // El resto de tus métodos quedan igual...
    @GetMapping("/Administrador/Clases")
    public String clasesAdmin() {
        return "Administrador/Clases/clasesadmin";
    }

    @GetMapping("/Administrador/Nueva/Clase")
    public String nuevaClase() {
        return "Administrador/Clases/nuevaClase";
    }

    @GetMapping("/Administrador/En/Clase")
    public String enclase() {
        return "Administrador/Clases/Enclase";
    }

    @GetMapping("/Administrador/Registro")
    public String registroAdmin() {
        return "Administrador/registroAdmin";
    }

    @GetMapping("/Administrador/Revision")
    public String revisionCompetencia() {
        return "Administrador/Clases/revisionCompetencia";
    }

    @GetMapping("/Administrador/Competencias/competencia")
    public String EditarCompetencia() {
    return "Administrador/Competencias/competencia";
    }

    @GetMapping("/Administrador/Competencias/Revision")
        public String revisionCompetenciaDetalle() {
    return "Administrador/Competencias/revision";
}
}



