package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Clase;
import pchy.pchy.models.Usuario;

import java.util.List;

@Repository
public class UsuarioClaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Solicitar unirse 
    public void solicitar(int idUsuario, int idClase) {
        String sql = """
                INSERT INTO usuarioClase (idUsuario, idClase, estado)
                VALUES (?, ?, 'PENDIENTE')
                """;
        jdbcTemplate.update(sql, idUsuario, idClase);
    }

    // Verificar estado de inscripción 
    public String obtenerEstado(int idUsuario, int idClase) {
        String sql = """
                SELECT estado FROM usuarioClase
                WHERE idUsuario = ? AND idClase = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, String.class,
                    idUsuario, idClase);
        } catch (Exception e) {
            return null;
        }
    }

    // Listdo de clases
    public List<Clase> listarClasesActivas(int idUsuario) {
        String sql = """
                SELECT c.idClase, c.nombre, c.descripcion, c.codigo,
                       c.idProfesorCreador, c.activa, c.fechaCreacion,
                       COUNT(DISTINCT uc2.idUsuario) AS totalAlumnos,
                       COUNT(DISTINCT co.idCompetencia) AS totalCompetencias,
                       u.nombre AS nombreProfesor,
                       u.apellido AS apellidoProfesor
                FROM clase c
                JOIN usuarioClase uc ON uc.idClase = c.idClase
                LEFT JOIN usuarioClase uc2
                    ON uc2.idClase = c.idClase AND uc2.estado = 'ACTIVO'
                LEFT JOIN competencia co
                    ON co.idClase = c.idClase AND co.estado = 'PUBLICADA'
                JOIN usuario u ON u.idUsuario = c.idProfesorCreador
                WHERE uc.idUsuario = ? AND uc.estado = 'ACTIVO'
                  AND c.activa = TRUE
                GROUP BY c.idClase, u.nombre, u.apellido
                ORDER BY c.fechaCreacion DESC
                """;

        return jdbcTemplate.query(sql, (rs, row) -> {
            Clase c = new Clase();
            c.setIdClase(rs.getInt("idClase"));
            c.setNombre(rs.getString("nombre"));
            c.setDescripcion(rs.getString("descripcion"));
            c.setCodigo(rs.getString("codigo"));
            c.setIdProfesorCreador(rs.getInt("idProfesorCreador"));
            c.setActiva(rs.getBoolean("activa"));
            c.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
            c.setTotalAlumnos(rs.getInt("totalAlumnos"));
            c.setTotalCompetencias(rs.getInt("totalCompetencias"));
            c.setNombreProfesor(rs.getString("nombreProfesor") + " " +
                    rs.getString("apellidoProfesor"));
            return c;
        }, idUsuario);
    }

    // Buscar clase por código
    public Clase buscarPorCodigo(String codigo) {
        String sql = """
                SELECT idClase, nombre, descripcion, codigo,
                       idProfesorCreador, activa, fechaCreacion
                FROM clase
                WHERE codigo = ? AND activa = TRUE
                """;
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Clase c = new Clase();
                c.setIdClase(rs.getInt("idClase"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setCodigo(rs.getString("codigo"));
                c.setIdProfesorCreador(rs.getInt("idProfesorCreador"));
                c.setActiva(rs.getBoolean("activa"));
                c.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                return c;
            }
            return null;
        }, codigo);
    }

    // Listar solicitudes pendientes de una clase
    public List<Usuario> listarPendientes(int idClase) {
        String sql = """
                SELECT u.idUsuario, u.nombre, u.apellido,
                       u.correo, u.matricula, u.fotoPerfil
                FROM usuario u
                JOIN usuarioClase uc ON uc.idUsuario = u.idUsuario
                WHERE uc.idClase = ? AND uc.estado = 'PENDIENTE'
                """;
        return jdbcTemplate.query(sql, (rs, row) -> {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setCorreo(rs.getString("correo"));
            u.setMatricula(rs.getString("matricula"));
            u.setFotoPerfil(rs.getString("fotoPerfil"));
            return u;
        }, idClase);
    }

    // Aprobar o rechazar solicitud
    public void actualizarEstado(int idUsuario, int idClase, String estado) {
        String sql = """
                UPDATE usuarioClase SET estado = ?
                WHERE idUsuario = ? AND idClase = ?
                """;
        jdbcTemplate.update(sql, estado, idUsuario, idClase);
    }

    public List<Usuario> listarAlumnosActivos(int idClase) {
        String sql = """
                SELECT u.idUsuario, u.nombre, u.apellido,
                       u.correo, u.matricula, u.fotoPerfil
                FROM usuario u
                JOIN usuarioClase uc ON uc.idUsuario = u.idUsuario
                WHERE uc.idClase = ? AND uc.estado = 'ACTIVO'
                ORDER BY u.nombre ASC
                """;
        return jdbcTemplate.query(sql, (rs, row) -> {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setCorreo(rs.getString("correo"));
            u.setMatricula(rs.getString("matricula"));
            u.setFotoPerfil(rs.getString("fotoPerfil"));
            return u;
        }, idClase);
    }

    public int contarSolicitudesPendientes(int idProfesor) {
        String sql = """
                SELECT COUNT(*) FROM usuarioClase uc
                JOIN clase c ON c.idClase = uc.idClase
                WHERE c.idProfesorCreador = ? AND uc.estado = 'PENDIENTE'
                """;
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, idProfesor);
        return total != null ? total : 0;
    }

}