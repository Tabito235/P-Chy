package pchy.pchy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import pchy.pchy.models.Usuario;
import pchy.pchy.service.UsuarioService;
import org.springframework.ui.Model;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/Usuario/Registro")
    public String vistaRegistroAlumno() {
        return "Usuario/registroUsuario";
    }

    @GetMapping("/Alumno/Inicio")
    public String principalAlumno(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", 3);

        return "Usuario/principalAlumno";
    }

    @GetMapping("/Alumno/Perfil")
    public String perfilAlumno() {
        return "redirect:/Perfil";
    }

    @PostMapping("/Usuario/Registro")
    public String registrarAlumno(
            Usuario usuario) {

        try {

            usuarioService.registrarAlumno(usuario);

            return "redirect:/Login";

        } catch (Exception e) {

            return "redirect:/Usuario/Registro?error";
        }
    }

    @GetMapping("/Alumno/Competencias")
    public String vistaCompetencias(HttpSession session, Model model) {

        return "Usuario/competencias/compUsuario";
    }

    @GetMapping("/Alumno/Competencias/Niveles")
    public String vistaNiveles(HttpSession session, Model model) {

        return "Usuario/competencias/lvlUsuario";
    }

    @GetMapping("/Alumno/Competencias/Problema")
    public String vistaProblema(HttpSession session, Model model) {

        return "Usuario/competencias/problemaU";
    }

    @GetMapping("/Alumno/Competencias/Ranks")
    public String vistaRanks(HttpSession session, Model model) {

        return "Usuario/competencias/RanksU";
    }

    @GetMapping("/Alumno/Competencias/Resultado")
    public String vistaResultado(HttpSession session, Model model) {

        return "Usuario/competencias/ResultU";
    }

    @GetMapping("/Alumno/Mis/Clases")
    public String misClases(HttpSession session, Model model) {

        return "Usuario/misClases";
    }

        @GetMapping("/Alumno/En/Clase")
    public String enClase(HttpSession session, Model model) {

        return "Usuario/EnClase";
    }

}