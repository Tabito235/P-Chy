package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Clase;
import pchy.pchy.models.Usuario;
import pchy.pchy.repository.UsuarioClaseRepository;

import java.util.List;

@Service
public class UsuarioClaseService {

    @Autowired
    private UsuarioClaseRepository usuarioClaseRepository;

    // Solicitar unirse con código
    public String solicitarUnirse(String codigo, int idUsuario) {

        // Buscar la clase
        Clase clase = usuarioClaseRepository.buscarPorCodigo(codigo.trim().toUpperCase());

        if (clase == null)
            throw new RuntimeException("Código inválido o clase no encontrada");

        // Verificar si ya tiene relación con esta clase
        String estadoActual = usuarioClaseRepository
            .obtenerEstado(idUsuario, clase.getIdClase());

        if ("ACTIVO".equals(estadoActual))
            throw new RuntimeException("Ya estás inscrito en esta clase");

        if ("PENDIENTE".equals(estadoActual))
            throw new RuntimeException("Ya tienes una solicitud pendiente para esta clase");

        if ("BAJA".equals(estadoActual))
            throw new RuntimeException("Fuiste dado de baja de esta clase");

        usuarioClaseRepository.solicitar(idUsuario, clase.getIdClase());
        return clase.getNombre();
    }

    // Listar clases activas del alumno
    public List<Clase> listarMisClases(int idUsuario) {
        return usuarioClaseRepository.listarClasesActivas(idUsuario);
    }

    // Listar solicitudes pendientes (para el admin)
    public List<Usuario> listarPendientes(int idClase) {
        return usuarioClaseRepository.listarPendientes(idClase);
    }

    // Aprobar solicitud
    public void aprobar(int idUsuario, int idClase) {
        usuarioClaseRepository.actualizarEstado(idUsuario, idClase, "ACTIVO");
    }

    // Rechazar solicitud
    public void rechazar(int idUsuario, int idClase) {
        usuarioClaseRepository.actualizarEstado(idUsuario, idClase, "BAJA");
    }

    // Verificar acceso del alumno a una clase
    public void verificarAcceso(int idUsuario, int idClase) {
        String estado = usuarioClaseRepository.obtenerEstado(idUsuario, idClase);
        if (!"ACTIVO".equals(estado))
            throw new RuntimeException("No tienes acceso a esta clase");
    }

    public List<Usuario> listarAlumnos(int idClase) {
    return usuarioClaseRepository.listarAlumnosActivos(idClase);
}
}