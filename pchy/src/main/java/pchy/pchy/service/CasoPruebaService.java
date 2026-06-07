package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.CasoPrueba;
import pchy.pchy.repository.CasoPruebaRepository;

import java.util.List;

@Service
public class CasoPruebaService {

    @Autowired
    private CasoPruebaRepository casoPruebaRepository;

    public void agregar(int idProblema, String entrada, String salidaEsperada) {

        if (entrada == null || entrada.isBlank())
            throw new RuntimeException("La entrada es obligatoria");
        if (salidaEsperada == null || salidaEsperada.isBlank())
            throw new RuntimeException("La salida esperada es obligatoria");

        int posicion = casoPruebaRepository
            .listarPorProblema(idProblema).size() + 1;

        CasoPrueba caso = new CasoPrueba();
        caso.setIdProblema(idProblema);
        caso.setEntrada(entrada);
        caso.setSalidaEsperada(salidaEsperada);
        caso.setPosicion(posicion);

        casoPruebaRepository.crear(caso);
    }

    public List<CasoPrueba> listarPorNivel(int idProblema) {
        return casoPruebaRepository.listarPorProblema(idProblema);
    }

    public void eliminar(int idCaso) {
        casoPruebaRepository.eliminar(idCaso);
    }
}