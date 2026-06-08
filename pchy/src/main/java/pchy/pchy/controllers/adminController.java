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
import pchy.pchy.models.Problema;
import pchy.pchy.models.Usuario;
import pchy.pchy.models.CasoPrueba;
import pchy.pchy.service.CasoPruebaService;
import pchy.pchy.service.ClaseService;
import pchy.pchy.service.CompetenciaService;
import pchy.pchy.service.NivelesService;
import pchy.pchy.service.ProblemaService;
import pchy.pchy.service.UsuarioClaseService;
import pchy.pchy.service.UsuarioService;

import java.util.List;

@Controller
public class adminController {

    @Autowired
    private ClaseService claseService;
    @Autowired
    private CompetenciaService competenciaService;
    @Autowired
    private NivelesService nivelesService;
    @Autowired
    private CasoPruebaService casoPruebaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProblemaService problemaService;
    @Autowired
    private UsuarioClaseService usuarioClaseService;

    @Value("${pchy.base-url}")
    private String baseUrl;

    // Inicio de sesion y el perfil

    @GetMapping("/Administrador/Inicio")
    public String principalAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";
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
        if (usuario == null)
            return "redirect:/Login";
        model.addAttribute("clases",
                claseService.listarMisClases(usuario.getIdUsuario()));
        return "Administrador/Clases/clasesadmin";
    }

    @GetMapping("/Administrador/Nueva/Clase")
    public String nuevaClaseVista(HttpSession session) {
        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";
        return "Administrador/Clases/nuevaClase";
    }

    @PostMapping("/Administrador/Nueva/Clase")
    public String crearClase(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

        try {
            claseService.crearClase(nombre, descripcion, usuario.getIdUsuario());
            return "redirect:/Administrador/Clases?ok";
        } catch (Exception e) {
            return "redirect:/Administrador/Nueva/Clase?error";
        }
    }

    

    @PostMapping("/Administrador/Clase/{id}/Editar")
    public String editarClase(
            @PathVariable int id,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

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
        if (usuario == null)
            return "redirect:/Login";

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

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            int idComp = competenciaService.crearCompetencia(
                    idClase, nombre, descripcion, lenguaje, fechaInicio, fechaFin);
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
        if (usuario == null)
            return "redirect:/Login";

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

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            competenciaService.editarCompetencia(
                    idComp, idClase, nombre, descripcion, lenguaje, fechaInicio, fechaFin);
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

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";
        competenciaService.publicar(idComp);
        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp;
    }

    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Eliminar")
    public String eliminarCompetencia(
            @PathVariable int idClase,
            @PathVariable int idComp,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";
        competenciaService.eliminar(idComp, idClase);
        return "redirect:/Administrador/Clase/" + idClase;
    }

    // Los niveles

    // ─── Crear nivel (actualizado) ────────────────────
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/Nuevo")
    public String crearNivel(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @RequestParam String titulo,
            @RequestParam(defaultValue = "10") int tiempoLimite,
            @RequestParam(defaultValue = "1") int problemasParaDesbloquear,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            nivelesService.crearNivel(idComp, titulo,
                    tiempoLimite, problemasParaDesbloquear);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                    "/Competencia/" + idComp + "?nivelError";
        }

        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp;
    }

    // ─── Ver nivel con problemas ──────────────────────
    @GetMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}")
    public String verNivel(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

        try {
            Clase clase = claseService.obtenerClase(idClase, usuario.getIdUsuario());
            Competencia comp = competenciaService.obtener(idComp);
            Niveles nivel = nivelesService.obtener(idNivel);
            List<Problema> problemas = problemaService.listarPorNivel(idNivel);

            model.addAttribute("clase", clase);
            model.addAttribute("competencia", comp);
            model.addAttribute("nivel", nivel);
            model.addAttribute("problemas", problemas);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                    "/Competencia/" + idComp;
        }

        return "Administrador/Competencias/nivel";
    }

    // ─── Crear problema ───────────────────────────────
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/Nuevo")
    public String crearProblema(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @RequestParam String titulo,
            @RequestParam(required = false) String enunciado,
            @RequestParam(defaultValue = "0") int puntaje,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            problemaService.crear(idNivel, titulo, enunciado, puntaje);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                    "/Competencia/" + idComp +
                    "/Nivel/" + idNivel + "?problemaError";
        }

        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp + "/Nivel/" + idNivel;
    }

    // ─── Editar problema ──────────────────────────────
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}/Editar")
    public String editarProblema(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @PathVariable int idProblema,
            @RequestParam String titulo,
            @RequestParam(required = false) String enunciado,
            @RequestParam(defaultValue = "0") int puntaje,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            problemaService.editar(idProblema, idNivel, titulo, enunciado, puntaje);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                    "/Competencia/" + idComp +
                    "/Nivel/" + idNivel + "?problemaError";
        }

        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp + "/Nivel/" + idNivel;
    }

    // ─── Eliminar problema ────────────────────────────
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}/Eliminar")
    public String eliminarProblema(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @PathVariable int idProblema,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        problemaService.eliminar(idProblema);

        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp + "/Nivel/" + idNivel;
    }

    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Eliminar")
    public String eliminarNivel(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";
        nivelesService.eliminar(idNivel, idComp);
        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp;
    }

    // Editar nivel
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Editar")
    public String editarNivel(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @RequestParam String titulo,
            @RequestParam(defaultValue = "10") int tiempoLimite,
            @RequestParam(defaultValue = "1") int problemasParaDesbloquear,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            nivelesService.editarNivel(idNivel, idComp, titulo,
                    tiempoLimite, problemasParaDesbloquear);
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

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

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

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

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

    // Ver todos los usuarios
    @GetMapping("/Administrador/Usuarios")
    public String verUsuarios(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

        model.addAttribute("usuarios",
                usuarioService.listarUsuariosConRol());

        return "Administrador/usuarios";
    }

    // Cambiar rol
    @PostMapping("/Administrador/Usuario/{id}/Rol")
    public String cambiarRol(
            @PathVariable int id,
            @RequestParam int nuevoRol,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            usuarioService.cambiarRol(id, nuevoRol);
        } catch (Exception e) {
            return "redirect:/Administrador/Usuarios?error";
        }

        return "redirect:/Administrador/Usuarios?ok";
    }

    // casos de prueba

    // Ver problema + sus casos de prueba
    @GetMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}")
    public String verProblema(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @PathVariable int idProblema,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

        try {
            Clase clase = claseService.obtenerClase(idClase, usuario.getIdUsuario());
            Competencia comp = competenciaService.obtener(idComp);
            Niveles nivel = nivelesService.obtener(idNivel);
            Problema problema = problemaService.obtener(idProblema);
            List<CasoPrueba> casos = casoPruebaService.listarPorProblema(idProblema);

            model.addAttribute("clase", clase);
            model.addAttribute("competencia", comp);
            model.addAttribute("nivel", nivel);
            model.addAttribute("problema", problema);
            model.addAttribute("casos", casos);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                    "/Competencia/" + idComp + "/Nivel/" + idNivel;
        }

        return "Administrador/Competencias/problema";
    }

    // Agregar caso de prueba
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}/Caso")
    public String agregarCaso(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @PathVariable int idProblema,
            @RequestParam String entrada,
            @RequestParam String salidaEsperada,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        try {
            casoPruebaService.agregar(idProblema, entrada, salidaEsperada);
        } catch (Exception e) {
            return "redirect:/Administrador/Clase/" + idClase +
                    "/Competencia/" + idComp +
                    "/Nivel/" + idNivel +
                    "/Problema/" + idProblema + "?casoError";
        }

        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp +
                "/Nivel/" + idNivel +
                "/Problema/" + idProblema;
    }

    // Eliminar caso de prueba
    @PostMapping("/Administrador/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}/Caso/{idCaso}/Eliminar")
    public String eliminarCaso(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @PathVariable int idProblema,
            @PathVariable int idCaso,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";

        casoPruebaService.eliminar(idCaso);

        return "redirect:/Administrador/Clase/" + idClase +
                "/Competencia/" + idComp +
                "/Nivel/" + idNivel +
                "/Problema/" + idProblema;
    }

    // Actualiza verClase para pasar solicitudes
    @GetMapping("/Administrador/Clase/{id}")
    public String verClase(
            @PathVariable int id,
            HttpSession session,
            Model model) {

                

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/Login";

        try {
            Clase clase = claseService.obtenerClase(id, usuario.getIdUsuario());
            model.addAttribute("clase", clase);
            model.addAttribute("urlClase", baseUrl + "/unirse/" + clase.getCodigo());
            model.addAttribute("competencias",
                    competenciaService.listarPorClase(id));
            model.addAttribute("solicitudes",
                    usuarioClaseService.listarPendientes(id)); // ← nuevo
                    model.addAttribute("alumnos",
    usuarioClaseService.listarAlumnos(id));
                    
        } catch (Exception e) {
            return "redirect:/Administrador/Clases";
        }

        return "Administrador/Clases/Enclase";
    }

    @PostMapping("/Administrador/Clase/{idClase}/Solicitud/{idUsuario}/Aprobar")
    public String aprobar(
            @PathVariable int idClase,
            @PathVariable int idUsuario,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";
        usuarioClaseService.aprobar(idUsuario, idClase);
        return "redirect:/Administrador/Clase/" + idClase + "?aprobado";
    }

    @PostMapping("/Administrador/Clase/{idClase}/Solicitud/{idUsuario}/Rechazar")
    public String rechazar(
            @PathVariable int idClase,
            @PathVariable int idUsuario,
            HttpSession session) {

        if (session.getAttribute("usuario") == null)
            return "redirect:/Login";
        usuarioClaseService.rechazar(idUsuario, idClase);
        return "redirect:/Administrador/Clase/" + idClase + "?rechazado";
    }

}