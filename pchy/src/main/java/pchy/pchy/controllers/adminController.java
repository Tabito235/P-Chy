package pchy.pchy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class competenciasController {

    @GetMapping("/Administrador/Inicio")
    public String principalAdmin() {
        return "administrador/principalAdmin"; 
    }

    @GetMapping("/Administrador/Perfil")
    public String perfilAdmin() {
        return "administrador/perfilAdmin"; 
    }

}

