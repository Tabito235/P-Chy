package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Niveles;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class NivelesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int crearNivel(Niveles n) {

        String sql = """
                INSERT INTO nivel
                (idCompetencia, numeroNivel, titulo, tiempoLimite,
                 posicion, problemasParaDesbloquear, activo)
                VALUES (?, ?, ?, ?, ?, ?, TRUE)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, n.getIdCompetencia());
            ps.setInt(2, n.getNumeroNivel());
            ps.setString(3, n.getTitulo());
            ps.setInt(4, n.getTiempoLimite());
            ps.setInt(5, n.getPosicion());
            ps.setInt(6, n.getProblemasParaDesbloquear());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void editarNivel(Niveles n) {

        String sql = """
                UPDATE nivel
                SET titulo = ?, tiempoLimite = ?, problemasParaDesbloquear = ?
                WHERE idNivel = ? AND idCompetencia = ?
                """;

        jdbcTemplate.update(sql,
                n.getTitulo(),
                n.getTiempoLimite(),
                n.getProblemasParaDesbloquear(),
                n.getIdNivel(),
                n.getIdCompetencia());
    }

    private Niveles mapear(java.sql.ResultSet rs) throws java.sql.SQLException {
        Niveles n = new Niveles();
        n.setIdNivel(rs.getInt("idNivel"));
        n.setIdCompetencia(rs.getInt("idCompetencia"));
        n.setNumeroNivel(rs.getInt("numeroNivel"));
        n.setTitulo(rs.getString("titulo"));
        n.setTiempoLimite(rs.getInt("tiempoLimite"));
        n.setPosicion(rs.getInt("posicion"));
        n.setActivo(rs.getBoolean("activo"));
        n.setProblemasParaDesbloquear(rs.getInt("problemasParaDesbloquear"));
        return n;
    }

    public List<Niveles> listarPorCompetencia(int idCompetencia) {

        String sql = """
                SELECT * FROM nivel
                WHERE idCompetencia = ? AND activo = TRUE
                ORDER BY posicion ASC
                """;

        return jdbcTemplate.query(sql, (rs, row) -> mapear(rs), idCompetencia);
    }

    public Niveles obtenerPorId(int idNivel) {

        String sql = "SELECT * FROM nivel WHERE idNivel = ?";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next())
                return mapear(rs);
            return null;
        }, idNivel);
    }

  
    public void desactivarNivel(int idNivel) {

        String sql = "UPDATE nivel SET activo = FALSE WHERE idNivel = ?";
        jdbcTemplate.update(sql, idNivel);
    }

    public void eliminarNivel(int idNivel, int idCompetencia) {

        String sql = "DELETE FROM nivel WHERE idNivel = ? AND idCompetencia = ?";
        jdbcTemplate.update(sql, idNivel, idCompetencia);
    }

    public int contarPorCompetencia(int idCompetencia) {

        String sql = "SELECT COUNT(*) FROM nivel WHERE idCompetencia = ? AND activo = TRUE";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, idCompetencia);
        return total != null ? total : 0;
    }

}