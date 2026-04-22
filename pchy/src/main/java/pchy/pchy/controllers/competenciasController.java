package pchy.pchy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class competenciasController {

    @GetMapping("/Administrador/Competencias")
    public String competencias() {
        return "competencia/competencias"; 
    }

    @GetMapping("/Administrador/Competencias/NuevaCompetencia")
    public String nuevaCompetencia() {
        return "competencia/nuevaCompetencia"; 
    }

    @GetMapping("/Administrador/Competencias/EditarCompetencia")
    public String editarCompetencia() {
        return "competencia/editarCompetencia"; 
    }

    @GetMapping("/Administrador/Competencias/NuevaCompetencia/NuevoNivel")
    public String nuevoNivel() {
        return "competencia/anadirNivel"; 
    }

    @GetMapping("/Administrador/Competencias/revisionCompetencias")
    public String revisionCompetencia() {
        return "competencia/revisionCompetencia"; 
    }
}

