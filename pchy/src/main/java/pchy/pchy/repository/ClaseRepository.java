package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Clase;

import java.util.List;
import java.util.UUID;

@Repository
public class ClaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
// Eto es para crear clase
    public void crearClase(Clase clase) {

        String sql = """
                INSERT INTO clase (nombre, descripcion, codigo, idProfesorCreador)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                clase.getNombre(),
                clase.getDescripcion(),
                clase.getCodigo(),
                clase.getIdProfesorCreador());
    }

    // Eto es para ver las clases del profe
    public List<Clase> listarPorProfesor(int idProfesor) {

        String sql = """
                SELECT
                    c.idClase, c.nombre, c.descripcion, c.codigo,
                    c.idProfesorCreador, c.activa, c.fechaCreacion,
                    COUNT(DISTINCT uc.idUsuario) AS totalAlumnos,
                    COUNT(DISTINCT co.idCompetencia) AS totalCompetencias
                FROM clase c
                LEFT JOIN usuarioClase uc
                    ON uc.idClase = c.idClase AND uc.estado = 'ACTIVO'
                LEFT JOIN competencia co
                    ON co.idClase = c.idClase
                WHERE c.idProfesorCreador = ? AND c.activa = TRUE
                GROUP BY c.idClase
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
            return c;
        }, idProfesor);
    }

    // Esto es para el id de la clase al buscarla
    public Clase obtenerPorId(int idClase) {

        String sql = """
                SELECT
                    c.idClase, c.nombre, c.descripcion, c.codigo,
                    c.idProfesorCreador, c.activa, c.fechaCreacion,
                    COUNT(DISTINCT uc.idUsuario) AS totalAlumnos,
                    COUNT(DISTINCT co.idCompetencia) AS totalCompetencias
                FROM clase c
                LEFT JOIN usuarioClase uc
                    ON uc.idClase = c.idClase AND uc.estado = 'ACTIVO'
                LEFT JOIN competencia co
                    ON co.idClase = c.idClase
                WHERE c.idClase = ?
                GROUP BY c.idClase
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
                c.setTotalAlumnos(rs.getInt("totalAlumnos"));
                c.setTotalCompetencias(rs.getInt("totalCompetencias"));
                return c;
            }
            return null;
        }, idClase);
    }

    //Eto edita la vaina
    public void editarClase(Clase clase) {

        String sql = """
                UPDATE clase
                SET nombre = ?, descripcion = ?
                WHERE idClase = ? AND idProfesorCreador = ?
                """;

        jdbcTemplate.update(sql,
                clase.getNombre(),
                clase.getDescripcion(),
                clase.getIdClase(),
                clase.getIdProfesorCreador());
    }

    // ─Pa borrarla
    public void desactivarClase(int idClase, int idProfesor) {

        String sql = """
                UPDATE clase
                SET activa = FALSE
                WHERE idClase = ? AND idProfesorCreador = ?
                """;

        jdbcTemplate.update(sql, idClase, idProfesor);
    }

    // Pa que se peuda crear y no ecista el codigo para no repetir
    public boolean existeCodigo(String codigo) {

        String sql = "SELECT COUNT(*) FROM clase WHERE codigo = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, codigo);
        return total != null && total > 0;
    }
}