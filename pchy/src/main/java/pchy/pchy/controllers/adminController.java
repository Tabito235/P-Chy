package pchy.pchy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminController {

    @GetMapping("/Administrador/Inicio")
    public String principalAdmin() {
        return "Administrador/principalAdmin"; 
    }

    @GetMapping("/Administrador/Perfil")
    public String perfilAdmin() {
        return "Administrador/perfilAdmin"; 
    }

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



