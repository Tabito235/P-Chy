package pchy.pchy.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pchy.pchy.models.*;
import pchy.pchy.repository.EntregaRepository;
import pchy.pchy.repository.PuntajeClaseRepository;
import pchy.pchy.service.*;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired private UsuarioService       usuarioService;
    @Autowired private UsuarioClaseService  usuarioClaseService;
    @Autowired private CompetenciaService   competenciaService;
    @Autowired private NivelesService       nivelesService;
    @Autowired private ProblemaService      problemaService;
    @Autowired private CasoPruebaService    casoPruebaService;
    @Autowired private EntregaService       entregaService;
    @Autowired private ProgresoService      progresoService;
    @Autowired private EntregaRepository    entregaRepository;
    @Autowired private RankingService rankingService;
 @Autowired private PuntajeClaseRepository puntajeClaseRepository;
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

            Clase clase = usuarioClaseService.listarMisClases(
                    usuario.getIdUsuario())
                .stream()
                .filter(c -> c.getIdClase() == idClase)
                .findFirst()
                .orElseThrow();

            // Solo competencias PUBLICADAS para el alumno
            List<Competencia> competencias =
                competenciaService.listarPublicadasPorClase(idClase);

            model.addAttribute("clase", clase);
            model.addAttribute("competencias", competencias);
            model.addAttribute("rol", 3);

        } catch (Exception e) {
            return "redirect:/Alumno/Mis/Clases";
        }

        return "Usuario/Enclase";
    }

    // ── Ver competencia (niveles) ─────────────────────
    @GetMapping("/Alumno/Clase/{idClase}/Competencia/{idComp}")
    public String verCompetencia(
            @PathVariable int idClase,
            @PathVariable int idComp,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        try {
            usuarioClaseService.verificarAcceso(
                usuario.getIdUsuario(), idClase
            );

            Clase clase = usuarioClaseService.listarMisClases(
                    usuario.getIdUsuario())
                .stream()
                .filter(c -> c.getIdClase() == idClase)
                .findFirst().orElseThrow();

            Competencia comp   = competenciaService.obtener(idComp);
            List<Niveles> niveles = nivelesService.listarPorCompetencia(idComp);

            java.util.Map<Integer, Boolean> nivelDesbloqueado =
                new java.util.HashMap<>();
            java.util.Map<Integer, Integer> progresoNivel =
                new java.util.HashMap<>();

            for (Niveles n : niveles) {
                boolean disponible = progresoService.nivelDisponible(
                    usuario.getIdUsuario(), n.getIdNivel(), n.getPosicion()
                );
                nivelDesbloqueado.put(n.getIdNivel(), disponible);
                progresoNivel.put(n.getIdNivel(),
                    progresoService.obtenerProblemasCompletados(
                        usuario.getIdUsuario(), n.getIdNivel()
                    )
                );
            }

            model.addAttribute("clase", clase);
            model.addAttribute("competencia", comp);
            model.addAttribute("niveles", niveles);
            model.addAttribute("nivelDesbloqueado", nivelDesbloqueado);
            model.addAttribute("progresoNivel", progresoNivel);
            model.addAttribute("rol", 3);

        } catch (Exception e) {
            return "redirect:/Alumno/Clase/" + idClase;
        }

        return "Usuario/competencias/compUsuario";
    }

    // ── Ver nivel ─────────────────────────────────────
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
            usuarioClaseService.verificarAcceso(
                usuario.getIdUsuario(), idClase
            );

            Clase clase = usuarioClaseService.listarMisClases(
                    usuario.getIdUsuario())
                .stream()
                .filter(c -> c.getIdClase() == idClase)
                .findFirst().orElseThrow();

            Competencia comp = competenciaService.obtener(idComp);
            Niveles nivel    = nivelesService.obtener(idNivel);

            // Verificar desbloqueo
            boolean disponible = progresoService.nivelDisponible(
                usuario.getIdUsuario(), idNivel, nivel.getPosicion()
            );

            if (!disponible) {
                return "redirect:/Alumno/Clase/" + idClase +
                       "/Competencia/" + idComp + "?bloqueado";
            }

            List<Problema> problemas = problemaService.listarPorNivel(idNivel);

            int completados = progresoService.obtenerProblemasCompletados(
                usuario.getIdUsuario(), idNivel
            );

            java.util.Map<Integer, Boolean> problemaCompletado =
                new java.util.HashMap<>();
            for (Problema p : problemas) {
                problemaCompletado.put(
                    p.getIdProblema(),
                    entregaRepository.problemaCompletado(
                        usuario.getIdUsuario(), p.getIdProblema()
                    )
                );
            }

            model.addAttribute("clase", clase);
            model.addAttribute("competencia", comp);
            model.addAttribute("nivel", nivel);
            model.addAttribute("problemas", problemas);
            model.addAttribute("completados", completados);
            model.addAttribute("problemaCompletado", problemaCompletado);
            model.addAttribute("rol", 3);

        } catch (Exception e) {
            return "redirect:/Alumno/Clase/" + idClase +
                   "/Competencia/" + idComp;
        }

        return "Usuario/competencias/lvlUsuario";
    }

    // ── Ver problema ──────────────────────────────────
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
            usuarioClaseService.verificarAcceso(
                usuario.getIdUsuario(), idClase
            );

            Clase clase = usuarioClaseService.listarMisClases(
                    usuario.getIdUsuario())
                .stream()
                .filter(c -> c.getIdClase() == idClase)
                .findFirst().orElseThrow();

            Competencia comp  = competenciaService.obtener(idComp);
            Niveles nivel     = nivelesService.obtener(idNivel);
            Problema problema = problemaService.obtener(idProblema);
            List<CasoPrueba> casos =
                casoPruebaService.listarPorProblema(idProblema);

            Entrega ultimaEntrega = entregaService.obtenerUltima(
                usuario.getIdUsuario(), idProblema
            );

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

    // ── Entregar solución ─────────────────────────────
    @PostMapping("/Alumno/Clase/{idClase}/Competencia/{idComp}/Nivel/{idNivel}/Problema/{idProblema}/Entregar")
    public String entregar(
            @PathVariable int idClase,
            @PathVariable int idComp,
            @PathVariable int idNivel,
            @PathVariable int idProblema,
            @RequestParam("archivoCodigo") MultipartFile archivoCodigo,
            @RequestParam(value = "archivoCaptura", required = false)
                MultipartFile archivoCaptura,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/Login";

        String base = "/Alumno/Clase/" + idClase +
                      "/Competencia/" + idComp +
                      "/Nivel/" + idNivel +
                      "/Problema/" + idProblema;

        try {
            Competencia comp = competenciaService.obtener(idComp);

Entrega entrega = entregaService.enviar(
    usuario.getIdUsuario(),
    idNivel,
    idProblema,
    comp.getLenguaje(),
    idClase,  // ← agregar
    archivoCodigo,
    archivoCaptura
);

            return "redirect:" + base +
                   "?resultado=" + entrega.getResultadoJudge() +
                   "&porcentaje=" + entrega.getPorcentajeCasos();

        } catch (Exception e) {
            return "redirect:" + base + "?judgeError";
        }
    }



    @GetMapping("/Alumno/Competencias/Resultado")
    public String resultado() {
        return "Usuario/competencias/ResultU";
    }


// Ranking global
@GetMapping("/Alumno/Rankings")
public String rankingGlobal(HttpSession session, Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    List<RankingEntry> ranking = rankingService.rankingGlobal();
    int miPosicion = rankingService.posicionGlobal(usuario.getIdUsuario());
    String miMedalla = rankingService.calcularMedalla(usuario.getPuntaje());

    model.addAttribute("ranking", ranking);
    model.addAttribute("miPosicion", miPosicion);
    model.addAttribute("miPuntaje", usuario.getPuntaje());
    model.addAttribute("miMedalla", miMedalla);
    model.addAttribute("usuario", usuario);
    model.addAttribute("rol", 3);

    return "Usuario/competencias/RanksU";
}

// Ranking por clase
@GetMapping("/Alumno/Clase/{idClase}/Ranking")
public String rankingPorClase(
        @PathVariable int idClase,
        HttpSession session,
        Model model) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/Login";

    try {
        usuarioClaseService.verificarAcceso(
            usuario.getIdUsuario(), idClase
        );

        Clase clase = usuarioClaseService.listarMisClases(
                usuario.getIdUsuario())
            .stream()
            .filter(c -> c.getIdClase() == idClase)
            .findFirst().orElseThrow();

        List<RankingEntry> ranking =
            rankingService.rankingPorClase(idClase);

        int miPosicion = rankingService.posicionEnClase(
            usuario.getIdUsuario(), idClase
        );

        List<Clase> misClases = usuarioClaseService.listarMisClases(
    usuario.getIdUsuario()
);

        int miPuntajeClase = puntajeClaseRepository.obtenerPuntaje(
            usuario.getIdUsuario(), idClase
        );

        model.addAttribute("clase", clase);
        model.addAttribute("ranking", ranking);
        model.addAttribute("miPosicion", miPosicion);
        model.addAttribute("miPuntajeClase", miPuntajeClase);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", 3);
        model.addAttribute("misClases", misClases);

    } catch (Exception e) {
        return "redirect:/Alumno/Mis/Clases";
    }

    return "Usuario/competencias/RankingClase";
}

}