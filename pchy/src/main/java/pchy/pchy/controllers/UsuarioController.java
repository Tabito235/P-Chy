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
    public String vistaRegistroAlumno(){
        return "Usuario/registroUsuario";
    }

    @GetMapping("/Alumno/Inicio")
public String principalAlumno(HttpSession session, Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

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
        Usuario usuario){

    try{

        usuarioService.registrarAlumno(usuario);

        return "redirect:/Login";

    }catch(Exception e){

        return "redirect:/Usuario/Registro?error";
    }
}
}