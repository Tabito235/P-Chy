package pchy.pchy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pchy.pchy.models.Usuario;
import pchy.pchy.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/Usuario/Registro")
    public String vistaRegistroAlumno(){
        return "Usuario/registroUsuario";
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