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

        String sql = "DELETE FROM competencia WHERE idCompetencia = ? AND idClase = ?";
        jdbcTemplate.update(sql, idCompetencia, idClase);
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
}