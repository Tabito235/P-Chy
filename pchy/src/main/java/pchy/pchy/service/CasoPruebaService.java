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

    public void agregar(int idNivel, String entrada, String salidaEsperada) {

        if (entrada == null || entrada.isBlank())
            throw new RuntimeException("La entrada es obligatoria");
        if (salidaEsperada == null || salidaEsperada.isBlank())
            throw new RuntimeException("La salida esperada es obligatoria");

        int posicion = casoPruebaRepository
            .listarPorNivel(idNivel).size() + 1;

        CasoPrueba caso = new CasoPrueba();
        caso.setIdNivel(idNivel);
        caso.setEntrada(entrada);
        caso.setSalidaEsperada(salidaEsperada);
        caso.setPosicion(posicion);

        casoPruebaRepository.crear(caso);
    }

    public List<CasoPrueba> listarPorNivel(int idNivel) {
        return casoPruebaRepository.listarPorNivel(idNivel);
    }

    public void eliminar(int idCaso) {
        casoPruebaRepository.eliminar(idCaso);
    }
}