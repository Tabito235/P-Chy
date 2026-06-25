package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Entrega;
import pchy.pchy.models.ResultadoCaso;
import pchy.pchy.models.Usuario;
import pchy.pchy.repository.RevisionRepository;

import java.util.List;

@Service
public class RevisionService {

    @Autowired
    private RevisionRepository revisionRepository;

    public List<Usuario> listarAlumnosConEntregas(int idCompetencia) {
        return revisionRepository.listarAlumnosConEntregas(idCompetencia);
    }

    public List<Entrega> listarEntregasAlumno(int idUsuario, int idCompetencia) {
        return revisionRepository.listarUltimasEntregasAlumno(
                idUsuario, idCompetencia);
    }

    public List<ResultadoCaso> listarResultadosCasos(int idEntrega) {
        return revisionRepository.listarResultadosCasos(idEntrega);
    }

    public void validar(int idEntrega, String estado,
            int puntaje, String comentario, int idRevisor) {

        if (!"APROBADA".equals(estado) && !"RECHAZADA".equals(estado))
            throw new RuntimeException("Estado inválido");

        revisionRepository.validarEntrega(
                idEntrega, estado, puntaje, comentario, idRevisor);
    }

    public java.util.Map<String, Object> obtenerRevision(int idEntrega) {
        return revisionRepository.obtenerRevision(idEntrega);
    }

}