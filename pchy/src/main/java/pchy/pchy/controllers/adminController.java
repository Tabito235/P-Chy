package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pchy.pchy.models.Clase;
import pchy.pchy.models.Competencia;
import pchy.pchy.models.Niveles;
import pchy.pchy.models.Usuario;
import pchy.pchy.models.CasoPrueba;
import pchy.pchy.service.CasoPruebaService;
import pchy.pchy.service.ClaseService;
import pchy.pchy.service.CompetenciaService;
import pchy.pchy.service.NivelesService;

import java.util.List;

@Controller
public class adminController {

    @Autowired private ClaseService claseService;
    @Autowired private CompetenciaService competenciaService;
    @Autowired private NivelesService nivelesService;
    @Autowired private CasoPruebaService casoPruebaService;

    @Value("${pchy.base-url}")
    private String baseUrl;

    // Inicio de sesion y el perfil

    @GetMapping("/Administrador/Inicio")
    public String principalAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", session.getAttribute("rol"));
        return "Administrador/principalAdmin";
    }

    @GetMapping("/Administrador/Perfil")
    public String perfilAdmin() {
        return "redirect:/Perfil";
    }

    @GetMapping("/Administrador/Registro")
    public String registroAdmin() {
        return "Administrador/registroAdmin";
    }

    // Las clases

    @GetMapping("/Administrador/Clases")
    public String clasesAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";
        model.addAttribute("clases",
            claseService.listarMisClases(usuario.getIdUsuario()));
        return "Administrador/Clases/clasesadmin";
    }

    @GetMapping("/Administrador/Nueva/Clase")
    public String nuevaClaseVista(HttpSession session) {
        if (session.getAttribute("usuario") == null) return "redirect:/Login";
        return "Administrador/Clases/nuevaClase";
    }

    @PostMapping("/Administrador/Nueva/Clase")
    public String crearClase(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            claseService.crearClase(nombre, descripcion, usuario.getIdUsuario());
            return "redirect:/Administrador/Clases?ok";
        } catch (Exception e) {
            return "redirect:/Administrador/Nueva/Clase?error";
        }
    }

    @GetMapping("/Administrador/Clase/{id}")
    public String verClase(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            Clase clase = claseService.obtenerClase(id, usuario.getIdUsuario());
            model.addAttribute("clase", clase);
            model.addAttribute("urlClase", baseUrl + "/unirse/" + clase.getCodigo());
            model.addAttribute("competencias",
                competenciaService.listarPorClase(id));
        } catch (Exception e) {
            return "redirect:/Administrador/Clases";
        }

        return "Administrador/Clases/Enclase";
    }

    @PostMapping("/Administrador/Clase/{id}/Editar")
    public String editarClase(
            @PathVariable int id,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            claseService.editarClase(id, nombre, descripcion, usuario.getIdUsuario());
            return "redirect:/Administrador/Clase/" + id + "?editOk";
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + id + "?editError";
        }
    }

    @PostMapping("/Administrador/Clase/{id}/Eliminar")
    public String eliminarClase(
            @PathVariable int id,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        claseService.eliminarClase(id, usuario.getIdUsuario());
        return "redirect:/Administrador/Clases?eliminada";
    }

    // Las competencias

    @PostMapping("/Administrador/Clase/{idClase}/Competencia/Nueva")
    public String crearCompetencia(
            @PathVariable int idClase,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam String lenguaje,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            HttpSession session) {

        if (session.getAttribute("usuario") == null) return "redirect:/Login";

        try {
            int idComp = competenciaService.crearCompetencia(
                idClase, nombre, descripcion, lenguaje, fechaInicio, fechaFin
            );
            return "redirect:/Administrador/Clase/" + idClase +
                   "/Competencia/" + idComp;
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase + "?compError";
        }
    }

    @GetMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}")
    public String verCompetencia(
            @PathVariable int idClase,
            @PathVariable int idComp,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            Clase clase = claseService.obtenerClase(idClase, usuario.getIdUsuario());
            Competencia comp = competenciaService.obtener(idComp);
            List<Niveles> niveles = nivelesService.listarPorCompetencia(idComp);

            model.addAttribute("clase", clase);
            model.addAttribute("competencia", comp);
            model.addAttribute("niveles", niveles);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase;
        }

        return "Administrador/Competencias/competencia";
    }

    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Editar")
    public String editarCompetencia(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam String lenguaje,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            HttpSession session) {

        if (session.getAttribute("usuario") == null) return "redirect:/Login";

        try {
            competenciaService.editarCompetencia(
                idComp, idClase, nombre, descripcion, lenguaje, fechaInicio, fechaFin
            );
            return "redirect:/Administrador/Clase/" + idClase +
                   "/Competencia/" + idComp + "?editOk";
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                   "/Competencia/" + idComp + "?editError";
        }
    }

    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Publicar")
    public String publicarCompetencia(
            @PathVariable int idClase,
            @PathVariable int idComp,
            HttpSession session) {

        if (session.getAttribute("usuario") == null) return "redirect:/Login";
        competenciaService.publicar(idComp);
        return "redirect:/Administrador/Clase/" + idClase +
               "/Competencia/" + idComp;
    }

    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Eliminar")
    public String eliminarCompetencia(
            @PathVariable int idClase,
            @PathVariable int idComp,
            HttpSession session) {

        if (session.getAttribute("usuario") == null) return "redirect:/Login";
        competenciaService.eliminar(idComp, idClase);
        return "redirect:/Administrador/Clase/" + idClase;
    }

    // Los niveles


    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/Nuevo")
    public String crearNivel(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @RequestParam String titulo,
            @RequestParam String enunciado,
            @RequestParam(defaultValue = "10") int tiempoLimite,
            HttpSession session) {

        if (session.getAttribute("usuario") == null) return "redirect:/Login";

        try {
            nivelesService.crearNivel(idComp, titulo, enunciado, tiempoLimite);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                   "/Competencia/" + idComp + "?nivelError";
        }

        return "redirect:/Administrador/Clase/" + idClase +
               "/Competencia/" + idComp;
    }



   
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Eliminar")
    public String eliminarNivel(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            HttpSession session) {

        if (session.getAttribute("usuario") == null) return "redirect:/Login";
        nivelesService.eliminar(idNivel, idComp);
        return "redirect:/Administrador/Clase/" + idClase +
               "/Competencia/" + idComp;
    }


// Ver nivel y casos de prueb
@GetMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}")
public String verNivel(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        HttpSession session,
        Model model) {

    if (session.getAttribute("usuario") == null) return "redirect:/Login";

    try {
        Clase clase           = claseService.obtenerClase(idClase,
                                    ((Usuario) session.getAttribute("usuario")).getIdUsuario());
        Competencia comp      = competenciaService.obtener(idComp);
        Niveles nivel         = nivelesService.obtener(idNivel);
        List<CasoPrueba> casos = casoPruebaService.listarPorNivel(idNivel);

        model.addAttribute("clase", clase);
        model.addAttribute("competencia", comp);
        model.addAttribute("nivel", nivel);
        model.addAttribute("casos", casos);
    } catch (Exception e) {
        return "redirect:/Administrador/Clase/" + idClase +
               "/Competencia/" + idComp;
    }

    return "Administrador/Competencias/nivel";
}

// Editar nivel
@PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Editar")
public String editarNivel(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        @RequestParam String titulo,
        @RequestParam(required = false) String enunciado,
        @RequestParam(defaultValue = "10") int tiempoLimite,
        @RequestParam(defaultValue = "0") int puntaje,
        HttpSession session) {

    if (session.getAttribute("usuario") == null) return "redirect:/Login";

    try {
        nivelesService.editarNivel(idNivel, idComp, titulo,
                                   enunciado, tiempoLimite, puntaje);
    } catch (Exception e) {
        return "redirect:/Administrador/Clase/" + idClase +
               "/Competencia/" + idComp + "/Nivel/" + idNivel + "?error";
    }

    return "redirect:/Administrador/Clase/" + idClase +
           "/Competencia/" + idComp + "/Nivel/" + idNivel + "?ok";
}

// Caso de pruena nuevo
@PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Caso")
public String agregarCaso(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        @RequestParam String entrada,
        @RequestParam String salidaEsperada,
        HttpSession session) {

    if (session.getAttribute("usuario") == null) return "redirect:/Login";

    try {
        casoPruebaService.agregar(idNivel, entrada, salidaEsperada);
    } catch (Exception e) {
        return "redirect:/Administrador/Clase/" + idClase +
               "/Competencia/" + idComp + "/Nivel/" + idNivel + "?casoError";
    }

    return "redirect:/Administrador/Clase/" + idClase +
           "/Competencia/" + idComp + "/Nivel/" + idNivel;
}

// Eliminar caso de prueba
@PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Caso/{idCaso}/Eliminar")
public String eliminarCaso(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        @PathVariable int idCaso,
        HttpSession session) {

    if (session.getAttribute("usuario") == null) return "redirect:/Login";

    casoPruebaService.eliminar(idCaso);
    return "redirect:/Administrador/Clase/" + idClase +
           "/Competencia/" + idComp + "/Nivel/" + idNivel;
}

    // Existen:

    @GetMapping("/Administrador/Revision")
    public String revisionCompetencia() {
        return "Administrador/Clases/revisionCompetencia";
    }

    @GetMapping("/Administrador/Competencias/Revision")
    public String revisionCompetenciaDetalle() {
        return "Administrador/Competencias/revision";
    }


}