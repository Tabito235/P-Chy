package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Niveles;
import pchy.pchy.repository.NivelesRepository;

import java.util.List;

@Service
public class NivelesService {

    @Autowired
    private NivelesRepository nivelesRepository;

    public int crearNivel(int idCompetencia, String titulo,
                      int tiempoLimite, int problemasParaDesbloquear) {

    if (titulo == null || titulo.isBlank())
        throw new RuntimeException("El título es obligatorio");

    int posicion = nivelesRepository.contarPorCompetencia(idCompetencia) + 1;

    Niveles n = new Niveles();
    n.setIdCompetencia(idCompetencia);
    n.setNumeroNivel(posicion);
    n.setTitulo(titulo);
    n.setTiempoLimite(tiempoLimite);
    n.setPosicion(posicion);
    n.setProblemasParaDesbloquear(problemasParaDesbloquear);
    n.setActivo(true);

    return nivelesRepository.crearNivel(n);
}

public void editarNivel(int idNivel, int idCompetencia,
                        String titulo, int tiempoLimite,
                        int problemasParaDesbloquear) {

    if (titulo == null || titulo.isBlank())
        throw new RuntimeException("El título es obligatorio");

    Niveles n = nivelesRepository.obtenerPorId(idNivel);
    if (n == null) throw new RuntimeException("Nivel no encontrado");

    n.setTitulo(titulo);
    n.setTiempoLimite(tiempoLimite);
    n.setProblemasParaDesbloquear(problemasParaDesbloquear);
    n.setIdCompetencia(idCompetencia);

    nivelesRepository.editarNivel(n);
}



    public List<Niveles> listarPorCompetencia(int idCompetencia) {
        return nivelesRepository.listarPorCompetencia(idCompetencia);
    }

    public Niveles obtener(int idNivel) {
        Niveles n = nivelesRepository.obtenerPorId(idNivel);
        if (n == null) throw new RuntimeException("Nivel no encontrado");
        return n;
    }

    public void eliminar(int idNivel, int idCompetencia) {
        nivelesRepository.eliminarNivel(idNivel, idCompetencia);
    }
}