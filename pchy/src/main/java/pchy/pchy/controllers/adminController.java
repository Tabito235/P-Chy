package pchy.pchy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminController {

    @GetMapping("/Administrador/Inicio")
    public String principalAdmin() {
        return "administrador/principalAdmin"; 
    }

    @GetMapping("/Administrador/Perfil")
    public String perfilAdmin() {
        return "administrador/perfilAdmin"; 
    }

    @GetMapping("/clasesadmin")
        public String clasesAdmin() {
    return "administrador/Clases/clasesadmin";
    }

    @GetMapping("/enclase")
    public String enclase() {
        return "administrador/Clases/Enclase";
    }
}

