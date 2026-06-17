package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Niveles;
import pchy.pchy.repository.EntregaRepository;
import pchy.pchy.repository.NivelesRepository;
import pchy.pchy.repository.ProgresoRepository;
import pchy.pchy.repository.PuntajeClaseRepository;
import pchy.pchy.repository.UsuarioRepository;

import java.util.List;

@Service
public class ProgresoService {

    @Autowired private ProgresoRepository  progresoRepository;
    @Autowired private EntregaRepository   entregaRepository;
    @Autowired private NivelesRepository   nivelesRepository;
    @Autowired private UsuarioRepository   usuarioRepository;
    @Autowired private PuntajeClaseRepository puntajeClaseRepository;

   public void procesarEntregaAceptada(int idUsuario, int idNivel,
                                     int idProblema, int puntaje,
                                     int idClase) { // ← agregar idClase

    long totalAceptadas = entregaRepository
        .contarEntregasAceptadasParaProblema(idUsuario, idProblema);

    boolean primerAceptado = (totalAceptadas == 1);
    if (!primerAceptado) return;

    // Sumar al puntaje global
    if (puntaje > 0) {
        usuarioRepository.sumarPuntos(idUsuario, puntaje);
        // Sumar también al ranking de la clase
        puntajeClaseRepository.sumarPuntos(idUsuario, idClase, puntaje);
    }

    int completados = entregaRepository
        .contarProblemasCompletadosEnNivel(idUsuario, idNivel);

    Niveles nivel = nivelesRepository.obtenerPorId(idNivel);
    if (nivel == null) return;

    progresoRepository.actualizarProgreso(
        idUsuario, idNivel, completados, true
    );

    if (completados >= nivel.getProblemasParaDesbloquear()) {
        desbloquearSiguienteNivel(idUsuario, idNivel,
            nivel.getIdCompetencia());
    }
}

    private void desbloquearSiguienteNivel(int idUsuario,
                                            int idNivelActual,
                                            int idCompetencia) {
        List<Niveles> niveles = nivelesRepository
            .listarPorCompetencia(idCompetencia);

        for (int i = 0; i < niveles.size() - 1; i++) {
            if (niveles.get(i).getIdNivel() == idNivelActual) {
                Niveles siguiente = niveles.get(i + 1);
                progresoRepository.desbloquearPrimerNivel(
                    idUsuario, siguiente.getIdNivel()
                );
                break;
            }
        }
    }

    public boolean nivelDisponible(int idUsuario, int idNivel,
                                    int posicion) {
        if (posicion == 1) {
            progresoRepository.desbloquearPrimerNivel(idUsuario, idNivel);
            return true;
        }
        return progresoRepository.nivelDesbloqueado(idUsuario, idNivel);
    }

    public int obtenerProblemasCompletados(int idUsuario, int idNivel) {
        return entregaRepository
            .contarProblemasCompletadosEnNivel(idUsuario, idNivel);
    }
}