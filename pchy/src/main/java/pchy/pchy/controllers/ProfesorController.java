package pchy.pchy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pchy.pchy.models.Usuario;
import pchy.pchy.service.UsuarioService;

@Controller
public class ProfesorController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/Profesor/Registro")
    public String vistaRegistroProfesor(){
        return "Profesor/registroProfesor";
    }

    @PostMapping("/Profesor/Registro")
    public String registrarProfesor(Usuario usuario){

        usuarioService.registrarProfesor(usuario);

        return "redirect:/Login";
    }
}