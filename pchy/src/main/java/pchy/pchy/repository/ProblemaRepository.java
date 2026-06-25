package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Problema;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProblemaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int crear(Problema p) {

        String sql = """
                INSERT INTO problema
                (idNivel, titulo, enunciado, puntaje, posicion, activo)
                VALUES (?, ?, ?, ?, ?, TRUE)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getIdNivel());
            ps.setString(2, p.getTitulo());
            ps.setString(3, p.getEnunciado());
            ps.setInt(4, p.getPuntaje());
            ps.setInt(5, p.getPosicion());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<Problema> listarPorNivel(int idNivel) {

        String sql = """
                SELECT * FROM problema
                WHERE idNivel = ? AND activo = TRUE
                ORDER BY posicion ASC
                """;

        return jdbcTemplate.query(sql, (rs, row) -> mapear(rs), idNivel);
    }

    public Problema obtenerPorId(int idProblema) {

        String sql = "SELECT * FROM problema WHERE idProblema = ?";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next())
                return mapear(rs);
            return null;
        }, idProblema);
    }

    public void editar(Problema p) {

        String sql = """
                UPDATE problema
                SET titulo = ?, enunciado = ?, puntaje = ?
                WHERE idProblema = ? AND idNivel = ?
                """;

        jdbcTemplate.update(sql,
                p.getTitulo(),
                p.getEnunciado(),
                p.getPuntaje(),
                p.getIdProblema(),
                p.getIdNivel());
    }

    public void desactivar(int idProblema) {
        jdbcTemplate.update(
                "UPDATE problema SET activo = FALSE WHERE idProblema = ?",
                idProblema);
    }

    public int contarPorNivel(int idNivel) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM problema WHERE idNivel = ? AND activo = TRUE",
                Integer.class, idNivel);
        return total != null ? total : 0;
    }

    private Problema mapear(java.sql.ResultSet rs) throws java.sql.SQLException {
        Problema p = new Problema();
        p.setIdProblema(rs.getInt("idProblema"));
        p.setIdNivel(rs.getInt("idNivel"));
        p.setTitulo(rs.getString("titulo"));
        p.setEnunciado(rs.getString("enunciado"));
        p.setPuntaje(rs.getInt("puntaje"));
        p.setPosicion(rs.getInt("posicion"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}