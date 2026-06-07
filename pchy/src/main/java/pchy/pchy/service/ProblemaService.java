package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Problema;
import pchy.pchy.repository.ProblemaRepository;

import java.util.List;

@Service
public class ProblemaService {

    @Autowired
    private ProblemaRepository problemaRepository;

    public int crear(int idNivel, String titulo,
            String enunciado, int puntaje) {

        if (titulo == null || titulo.isBlank())
            throw new RuntimeException("El título es obligatorio");

        int posicion = problemaRepository.contarPorNivel(idNivel) + 1;

        Problema p = new Problema();
        p.setIdNivel(idNivel);
        p.setTitulo(titulo);
        p.setEnunciado(enunciado != null ? enunciado : "");
        p.setPuntaje(puntaje);
        p.setPosicion(posicion);

        return problemaRepository.crear(p);
    }

    public List<Problema> listarPorNivel(int idNivel) {
        return problemaRepository.listarPorNivel(idNivel);
    }

    public Problema obtener(int idProblema) {
        Problema p = problemaRepository.obtenerPorId(idProblema);
        if (p == null)
            throw new RuntimeException("Problema no encontrado");
        return p;
    }

    public void editar(int idProblema, int idNivel,
            String titulo, String enunciado, int puntaje) {

        if (titulo == null || titulo.isBlank())
            throw new RuntimeException("El título es obligatorio");

        Problema p = new Problema();
        p.setIdProblema(idProblema);
        p.setIdNivel(idNivel);
        p.setTitulo(titulo);
        p.setEnunciado(enunciado != null ? enunciado : "");
        p.setPuntaje(puntaje);

        problemaRepository.editar(p);
    }

    public void eliminar(int idProblema) {
        problemaRepository.desactivar(idProblema);
    }
}