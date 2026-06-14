package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Niveles;
import pchy.pchy.repository.EntregaRepository;
import pchy.pchy.repository.NivelesRepository;
import pchy.pchy.repository.ProgresoRepository;
import pchy.pchy.repository.UsuarioRepository;

import java.util.List;

@Service
public class ProgresoService {

    @Autowired private ProgresoRepository  progresoRepository;
    @Autowired private EntregaRepository   entregaRepository;
    @Autowired private NivelesRepository   nivelesRepository;
    @Autowired private UsuarioRepository   usuarioRepository;

    public void procesarEntregaAceptada(int idUsuario, int idNivel,
                                         int idProblema, int puntaje) {

        // 1. Contar cuántas veces fue aceptado ANTES de esta entrega
        //    Si ya había sido aceptado, no sumar puntos de nuevo
        long totalAceptadas = entregaRepository
            .contarEntregasAceptadasParaProblema(idUsuario, idProblema);

        boolean primerAceptado = (totalAceptadas == 1);
        // totalAceptadas == 1 porque esta entrega YA fue guardada como ACEPTADO
        // antes de llamar a este método

        if (!primerAceptado) return; // Ya lo completó antes, no hacer nada

        // 2. Sumar puntos al usuario solo la primera vez
        if (puntaje > 0) {
            usuarioRepository.sumarPuntos(idUsuario, puntaje);
        }

        // 3. Recalcular problemas completados en el nivel
        int completados = entregaRepository
            .contarProblemasCompletadosEnNivel(idUsuario, idNivel);

        Niveles nivel = nivelesRepository.obtenerPorId(idNivel);
        if (nivel == null) return;

        // 4. Actualizar progreso
        progresoRepository.actualizarProgreso(
            idUsuario, idNivel, completados, true
        );

        // 5. Si cumple el mínimo, desbloquear siguiente nivel
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