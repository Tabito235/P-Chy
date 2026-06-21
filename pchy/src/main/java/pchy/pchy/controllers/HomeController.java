package pchy.pchy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import pchy.pchy.service.LoginService;

@Controller
public class HomeController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/temporal")
    public String temporal() {
        return "temporal";
    }

     @GetMapping("/desafioDiario")
    public String desafioDiario() {
        return "desafioDiario";
    }

    @GetMapping("/Login")
    public String vistaLogin() {
        return "inicioSesionA";
    }

    @PostMapping("/Login")
    public String login(
            @RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session) {

        return loginService.autenticar(
                correo,
                contrasena,
                session
        );
    }

      @GetMapping("/ayudaProfesor")
    public String ayudaProfesor() {
        return "ayudaProfesor";
    }

      @GetMapping("/ayudaUsuario")
    public String ayudaUsuario() {
        return "ayudaUsuario";
    }
}