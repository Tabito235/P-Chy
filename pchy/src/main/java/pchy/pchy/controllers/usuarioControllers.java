package pchy.pchy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class usuarioControllers {

    @GetMapping("/Usuario/Registro")
    public String registroUsuario() {
        return "Usuario/registroUsuario"; 
    }


}

