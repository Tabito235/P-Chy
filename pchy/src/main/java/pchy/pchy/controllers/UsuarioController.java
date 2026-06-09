package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pchy.pchy.models.CasoPrueba;
import pchy.pchy.models.Clase;
import pchy.pchy.models.Competencia;
import pchy.pchy.models.Entrega;
import pchy.pchy.models.Niveles;
import pchy.pchy.models.Problema;
import pchy.pchy.models.ResultadoCaso;
import pchy.pchy.models.Usuario;
import pchy.pchy.service.CasoPruebaService;
import pchy.pchy.service.CompetenciaService;
import pchy.pchy.service.EntregaService;
import pchy.pchy.service.NivelesService;
import pchy.pchy.service.ProblemaService;
import pchy.pchy.service.UsuarioClaseService;
import pchy.pchy.service.UsuarioService;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired private UsuarioService      usuarioService;
    @Autowired private UsuarioClaseService usuarioClaseService;
    @Autowired private CompetenciaService  competenciaService;

    // ── Registro ──────────────────────────────────────
    @GetMapping("/Usuario/Registro")
    public String vistaRegistro() {
        return "Usuario/registroUsuario";
    }

    @PostMapping("/Usuario/Registro")
    public String registrar(Usuario usuario) {
        try {
            usuarioService.registrarAlumno(usuario);
            return "redirect:/Login";
        } catch (Exception e) {
            return "redirect:/Usuario/Registro?error";
        }
    }

    // ── Inicio ────────────────────────────────────────
    @GetMapping("/Alumno/Inicio")
    public String inicio(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        List<Clase> clases = usuarioClaseService.listarMisClases(
            usuario.getIdUsuario()
        );

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", 3);
        model.addAttribute("totalClases", clases.size());

        return "Usuario/principalAlumno";
    }

    @GetMapping("/Alumno/Perfil")
    public String perfil() { return "redirect:/Perfil"; }

    // ── Mis clases ────────────────────────────────────
    @GetMapping("/Alumno/Mis/Clases")
    public String misClases(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        List<Clase> clases = usuarioClaseService.listarMisClases(
            usuario.getIdUsuario()
        );
        model.addAttribute("clases", clases);
        model.addAttribute("rol", 3);

        return "Usuario/misClases";
    }

    // ── Unirse a clase con código ─────────────────────
    @PostMapping("/Alumno/Unirse")
    public String unirse(
            @RequestParam String codigo,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            String nombre = usuarioClaseService.solicitarUnirse(
                codigo, usuario.getIdUsuario()
            );
            return "redirect:/Alumno/Mis/Clases?solicitudOk=" +
                java.net.URLEncoder.encode(nombre,
                    java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "redirect:/Alumno/Mis/Clases?error=" +
                java.net.URLEncoder.encode(e.getMessage(),
                    java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    // ── En clase (ver competencias) ───────────────────
    @GetMapping("/Alumno/Clase/{idClase}")
    public String enClase(
            @PathVariable int idClase,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            usuarioClaseService.verificarAcceso(
                usuario.getIdUsuario(), idClase
            );

            // Reutilizamos listarMisClases y filtramos la clase actual
            Clase clase = usuarioClaseService.listarMisClases(
                    usuario.getIdUsuario())
                .stream()
                .filter(c -> c.getIdClase() == idClase)
                .findFirst()
                .orElseThrow(() ->
                    new RuntimeException("Clase no encontrada"));

            List<Competencia> competencias =
                competenciaService.listarPorClase(idClase);

            model.addAttribute("clase", clase);
            model.addAttribute("competencias", competencias);
            model.addAttribute("rol", 3);

        } catch (Exception e) {
            return "redirect:/Alumno/Mis/Clases";
        }

        return "Usuario/enClase";
    }

    // ── Rutas estáticas por ahora ─────────────────────
    @GetMapping("/Alumno/Competencias/Problema")
    public String problema() {
        return "Usuario/competencias/problemaU";
    }

    @GetMapping("/Alumno/Rankings")
    public String ranks() {
        return "Usuario/competencias/RanksU";
    }

    @GetMapping("/Alumno/Competencias/Resultado")
    public String resultado() {
        return "Usuario/competencias/ResultU";
    }

    // ── Ver competencia (niveles) ─────────────────────
@Autowired private NivelesService nivelesService;

@GetMapping("/Alumno/Clase/{idClase}/Competencia/{idComp}")
public String verCompetencia(
        @PathVariable int idClase,
        @PathVariable int idComp,
        HttpSession session,
        Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    try {
        usuarioClaseService.verificarAcceso(usuario.getIdUsuario(), idClase);

        Clase clase = usuarioClaseService.listarMisClases(usuario.getIdUsuario())
            .stream()
            .filter(c -> c.getIdClase() == idClase)
            .findFirst()
            .orElseThrow();

        Competencia comp = competenciaService.obtener(idComp);
        List<Niveles> niveles = nivelesService.listarPorCompetencia(idComp);

        model.addAttribute("clase", clase);
        model.addAttribute("competencia", comp);
        model.addAttribute("niveles", niveles);
        model.addAttribute("rol", 3);

    } catch (Exception e) {
        return "redirect:/Alumno/Clase/" + idClase;
    }

    return "Usuario/competencias/compUsuario";
}

// ── Ver nivel (problemas + casos) ────────────────
@Autowired private ProblemaService problemaService;
@Autowired private CasoPruebaService casoPruebaService;

@GetMapping("/Alumno/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}")
public String verNivel(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        HttpSession session,
        Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    try {
        usuarioClaseService.verificarAcceso(usuario.getIdUsuario(), idClase);

        Clase clase = usuarioClaseService.listarMisClases(usuario.getIdUsuario())
            .stream()
            .filter(c -> c.getIdClase() == idClase)
            .findFirst()
            .orElseThrow();

        Competencia comp   = competenciaService.obtener(idComp);
        Niveles nivel      = nivelesService.obtener(idNivel);
        List<Problema> problemas = problemaService.listarPorNivel(idNivel);

        model.addAttribute("clase", clase);
        model.addAttribute("competencia", comp);
        model.addAttribute("nivel", nivel);
        model.addAttribute("problemas", problemas);
        model.addAttribute("rol", 3);

    } catch (Exception e) {
        return "redirect:/Alumno/Clase/" + idClase + "/Competencia/" + idComp;
    }

    return "Usuario/competencias/lvlUsuario";
}

// ── Ver problema ──────────────────────────────────



@Autowired private EntregaService entregaService;

@PostMapping("/Alumno/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}/Entregar")
public String entregar(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        @PathVariable int idProblema,
        @RequestParam("archivoCodigo") MultipartFile archivoCodigo,
        @RequestParam(value = "archivoCaptura", required = false)
            MultipartFile archivoCaptura,
        HttpSession session,
        Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    String base = "/Alumno/Clase/" + idClase +
                  "/Competencia/" + idComp +
                  "/Nivel/" + idNivel +
                  "/Problema/" + idProblema;

    try {
        // Obtener lenguaje de la competencia
        Competencia comp = competenciaService.obtener(idComp);

        Entrega entrega = entregaService.enviar(
            usuario.getIdUsuario(),
            idNivel,
            idProblema,
            comp.getLenguaje(),
            archivoCodigo,
            archivoCaptura
        );

        return "redirect:" + base + "?resultado=" + entrega.getResultadoJudge()
               + "&porcentaje=" + entrega.getPorcentajeCasos();

    } catch (Exception e) {
        return "redirect:" + base + "?judgeError";
    }
}

// Actualizar verProblema para incluir última entrega
@GetMapping("/Alumno/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}")
public String verProblema(
        @PathVariable int idClase,
        @PathVariable int idComp,
        @PathVariable int idNivel,
        @PathVariable int idProblema,
        HttpSession session,
        Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    try {
        usuarioClaseService.verificarAcceso(usuario.getIdUsuario(), idClase);

        Clase clase = usuarioClaseService.listarMisClases(
                usuario.getIdUsuario())
            .stream()
            .filter(c -> c.getIdClase() == idClase)
            .findFirst().orElseThrow();

        Competencia comp  = competenciaService.obtener(idComp);
        Niveles nivel     = nivelesService.obtener(idNivel);
        Problema problema = problemaService.obtener(idProblema);
        List<CasoPrueba> casos = casoPruebaService.listarPorProblema(idProblema);

        // Última entrega del alumno para este problema
        Entrega ultimaEntrega = entregaService.obtenerUltima(
            usuario.getIdUsuario(), idProblema
        );

        // Resultados por caso de la última entrega
        List<ResultadoCaso> resultadosCasos = null;
        if (ultimaEntrega != null) {
            resultadosCasos = entregaService.resultadosCasos(
                ultimaEntrega.getIdEntrega()
            );
        }

        model.addAttribute("clase", clase);
        model.addAttribute("competencia", comp);
        model.addAttribute("nivel", nivel);
        model.addAttribute("problema", problema);
        model.addAttribute("casos", casos);
        model.addAttribute("ultimaEntrega", ultimaEntrega);
        model.addAttribute("resultadosCasos", resultadosCasos);
        model.addAttribute("rol", 3);

    } catch (Exception e) {
        return "redirect:/Alumno/Clase/" + idClase +
               "/Competencia/" + idComp + "/Nivel/" + idNivel;
    }

    return "Usuario/competencias/problemaU";
}


}
