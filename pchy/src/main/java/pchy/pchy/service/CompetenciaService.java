package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Competencia;
import pchy.pchy.repository.CompetenciaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CompetenciaService {

    @Autowired
    private CompetenciaRepository competenciaRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public int crearCompetencia(int idClase, String nombre, String descripcion,
            String lenguaje, String fechaInicio, String fechaFin) {

        validar(nombre, lenguaje, fechaInicio, fechaFin);

        Competencia c = new Competencia();
        c.setIdClase(idClase);
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setLenguaje(lenguaje);
        c.setFechaInicio(parsear(fechaInicio));
        c.setFechaFin(parsear(fechaFin));

        return competenciaRepository.crearCompetencia(c);
    }

    public List<Competencia> listarPorClase(int idClase) {
        return competenciaRepository.listarPorClase(idClase);
    }

    public Competencia obtener(int idCompetencia) {
        Competencia c = competenciaRepository.obtenerPorId(idCompetencia);
        if (c == null)
            throw new RuntimeException("Competencia no encontrada");
        return c;
    }

    public void editarCompetencia(int idCompetencia, int idClase, String nombre,
            String descripcion, String lenguaje,
            String fechaInicio, String fechaFin) {

        validar(nombre, lenguaje, fechaInicio, fechaFin);

        Competencia c = new Competencia();
        c.setIdCompetencia(idCompetencia);
        c.setIdClase(idClase);
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setLenguaje(lenguaje);
        c.setFechaInicio(parsear(fechaInicio));
        c.setFechaFin(parsear(fechaFin));

        competenciaRepository.editarCompetencia(c);
    }

    public void publicar(int idCompetencia) {
        competenciaRepository.cambiarEstado(idCompetencia, "PUBLICADA");
    }

    public void cerrar(int idCompetencia) {
        competenciaRepository.cambiarEstado(idCompetencia, "CERRADA");
    }

    public void eliminar(int idCompetencia, int idClase) {
        competenciaRepository.eliminarCompetencia(idCompetencia, idClase);
    }

    // Por si me falta texto
    private void validar(String nombre, String lenguaje,
            String fechaInicio, String fechaFin) {
        if (nombre == null || nombre.isBlank())
            throw new RuntimeException("El nombre es obligatorio");
        if (lenguaje == null || lenguaje.isBlank())
            throw new RuntimeException("El lenguaje es obligatorio");
        if (fechaInicio == null || fechaInicio.isBlank())
            throw new RuntimeException("La fecha de inicio es obligatoria");
        if (fechaFin == null || fechaFin.isBlank())
            throw new RuntimeException("La fecha de fin es obligatoria");
    }

    private Timestamp parsear(String fechaHora) {
        return Timestamp.valueOf(LocalDateTime.parse(fechaHora, FMT));
    }
}