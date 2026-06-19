package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Competencia;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CompetenciaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Crearla
    public int crearCompetencia(Competencia c) {

        String sql = """
            INSERT INTO competencia
            (idClase, nombre, descripcion, lenguaje, fechaInicio, fechaFin, estado)
            VALUES (?, ?, ?, ?, ?, ?, 'BORRADOR')
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, c.getIdClase());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDescripcion());
            ps.setString(4, c.getLenguaje());
            ps.setTimestamp(5, c.getFechaInicio());
            ps.setTimestamp(6, c.getFechaFin());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // En que clase esta
    public List<Competencia> listarPorClase(int idClase) {

        String sql = """
            SELECT * FROM competencia
            WHERE idClase = ?
            ORDER BY fechaInicio ASC
            """;

        return jdbcTemplate.query(sql, (rs, row) -> mapear(rs), idClase);
    }

    // ID
    public Competencia obtenerPorId(int idCompetencia) {

        String sql = "SELECT * FROM competencia WHERE idCompetencia = ?";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        }, idCompetencia);
    }

    //Editar
    public void editarCompetencia(Competencia c) {

        String sql = """
            UPDATE competencia
            SET nombre = ?, descripcion = ?, lenguaje = ?,
                fechaInicio = ?, fechaFin = ?
            WHERE idCompetencia = ? AND idClase = ?
            """;

        jdbcTemplate.update(sql,
            c.getNombre(),
            c.getDescripcion(),
            c.getLenguaje(),
            c.getFechaInicio(),
            c.getFechaFin(),
            c.getIdCompetencia(),
            c.getIdClase()
        );
    }

    // en que esta, si editado, eliminado, y eso
    public void cambiarEstado(int idCompetencia, String estado) {

        String sql = "UPDATE competencia SET estado = ? WHERE idCompetencia = ?";
        jdbcTemplate.update(sql, estado, idCompetencia);
    }

    //Borrar



  public void eliminarCompetencia(int idCompetencia, int idClase) {

    // 1. Eliminar resultadoCaso (FK hacia casoPrueba y entrega)
    String sqlResultados = """
        DELETE rc FROM resultadoCaso rc
        JOIN casoPrueba cp ON cp.idCaso = rc.idCaso
        JOIN problema p ON p.idProblema = cp.idProblema
        JOIN nivel n ON n.idNivel = p.idNivel
        WHERE n.idCompetencia = ?
        """;
    jdbcTemplate.update(sqlResultados, idCompetencia);

    // 2. Eliminar revisiones (FK hacia entrega)
    String sqlRevisiones = """
        DELETE rv FROM revision rv
        JOIN entrega e ON e.idEntrega = rv.idEntrega
        JOIN nivel n ON n.idNivel = e.idNivel
        WHERE n.idCompetencia = ?
        """;
    jdbcTemplate.update(sqlRevisiones, idCompetencia);

    // 3. Eliminar entregas (FK hacia nivel y problema)
    String sqlEntregas = """
        DELETE e FROM entrega e
        JOIN nivel n ON n.idNivel = e.idNivel
        WHERE n.idCompetencia = ?
        """;
    jdbcTemplate.update(sqlEntregas, idCompetencia);

    // 4. Eliminar casoPrueba (FK hacia problema)
    String sqlCasos = """
        DELETE cp FROM casoPrueba cp
        JOIN problema p ON p.idProblema = cp.idProblema
        JOIN nivel n ON n.idNivel = p.idNivel
        WHERE n.idCompetencia = ?
        """;
    jdbcTemplate.update(sqlCasos, idCompetencia);

    // 5. Eliminar progreso de niveles
    String sqlProgreso = """
        DELETE pn FROM progresoNivel pn
        JOIN nivel n ON n.idNivel = pn.idNivel
        WHERE n.idCompetencia = ?
        """;
    jdbcTemplate.update(sqlProgreso, idCompetencia);

    // 6. Eliminar problemas
    String sqlProblemas = """
        DELETE p FROM problema p
        JOIN nivel n ON n.idNivel = p.idNivel
        WHERE n.idCompetencia = ?
        """;
    jdbcTemplate.update(sqlProblemas, idCompetencia);

    // 7. Eliminar niveles
    jdbcTemplate.update(
        "DELETE FROM nivel WHERE idCompetencia = ?",
        idCompetencia
    );

    // 8. Eliminar la competencia
    jdbcTemplate.update(
        "DELETE FROM competencia WHERE idCompetencia = ? AND idClase = ?",
        idCompetencia, idClase
    );
}

    public List<Competencia> listarPublicadasPorClase(int idClase) {
    String sql = """
        SELECT * FROM competencia
        WHERE idClase = ? AND estado = 'PUBLICADA'
        ORDER BY fechaInicio ASC
        """;
    return jdbcTemplate.query(sql, (rs, row) -> mapear(rs), idClase);
}

    // Mapeado
    private Competencia mapear(java.sql.ResultSet rs) throws java.sql.SQLException {
        Competencia c = new Competencia();
        c.setIdCompetencia(rs.getInt("idCompetencia"));
        c.setIdClase(rs.getInt("idClase"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setLenguaje(rs.getString("lenguaje"));
        c.setFechaInicio(rs.getTimestamp("fechaInicio"));
        c.setFechaFin(rs.getTimestamp("fechaFin"));
        c.setEstado(rs.getString("estado"));
        return c;
    }

    public List<Competencia> listarProximasPorProfesor(int idProfesor) {
    String sql = """
        SELECT c.* FROM competencia c
        JOIN clase cl ON cl.idClase = c.idClase
        WHERE cl.idProfesorCreador = ?
        AND c.estado = 'PUBLICADA'
        AND c.fechaInicio >= NOW()
        ORDER BY c.fechaInicio ASC
        LIMIT 3
        """;
    return jdbcTemplate.query(sql, (rs, row) -> mapear(rs), idProfesor);
}

public List<Competencia> listarActivasPorAlumno(int idUsuario) {
    String sql = """
        SELECT c.* FROM competencia c
        JOIN clase cl ON cl.idClase = c.idClase
        JOIN usuarioClase uc ON uc.idClase = cl.idClase
        WHERE uc.idUsuario = ? AND uc.estado = 'ACTIVO'
        AND c.estado = 'PUBLICADA'
        AND c.fechaFin >= NOW()
        ORDER BY c.fechaInicio ASC
        LIMIT 3
        """;
    return jdbcTemplate.query(sql, (rs, row) -> mapear(rs), idUsuario);
}
}