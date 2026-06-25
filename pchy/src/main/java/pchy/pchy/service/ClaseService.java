package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Clase;
import pchy.pchy.repository.ClaseRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    public void crearClase(String nombre, String descripcion, int idProfesor) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        Clase clase = new Clase();
        clase.setNombre(nombre);
        clase.setDescripcion(descripcion);
        clase.setCodigo(generarCodigo());
        clase.setIdProfesorCreador(idProfesor);

        claseRepository.crearClase(clase);
    }

    public List<Clase> listarMisClases(int idProfesor) {
        return claseRepository.listarPorProfesor(idProfesor);
    }

    public Clase obtenerClase(int idClase, int idProfesor) {

        Clase clase = claseRepository.obtenerPorId(idClase);

        if (clase == null) {
            throw new RuntimeException("Clase no encontrada");
        }

        if (clase.getIdProfesorCreador() != idProfesor) {
            throw new RuntimeException("No tienes permiso para ver esta clase");
        }

        return clase;
    }

  
    public void editarClase(int idClase, String nombre,
            String descripcion, int idProfesor) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        Clase clase = new Clase();
        clase.setIdClase(idClase);
        clase.setNombre(nombre);
        clase.setDescripcion(descripcion);
        clase.setIdProfesorCreador(idProfesor);

        claseRepository.editarClase(clase);
    }

    public void eliminarClase(int idClase, int idProfesor) {
        claseRepository.desactivarClase(idClase, idProfesor);
    }

    private String generarCodigo() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String codigo;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(
                        (int) (Math.random() * chars.length())));
            }
            codigo = sb.toString();
        } while (claseRepository.existeCodigo(codigo));

        return codigo;
    }

    public int contarClasesActivas(int idProfesor) {
        return claseRepository.contarClasesActivas(idProfesor);
    }
}