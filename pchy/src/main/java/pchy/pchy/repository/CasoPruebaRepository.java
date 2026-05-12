package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.CasoPrueba;

import java.util.List;

@Repository
public class CasoPruebaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void crear(CasoPrueba caso) {
        String sql = """
            INSERT INTO casoPrueba (idNivel, entrada, salidaEsperada, posicion)
            VALUES (?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
            caso.getIdNivel(),
            caso.getEntrada(),
            caso.getSalidaEsperada(),
            caso.getPosicion()
        );
    }

    public List<CasoPrueba> listarPorNivel(int idNivel) {
        String sql = """
            SELECT * FROM casoPrueba
            WHERE idNivel = ?
            ORDER BY posicion ASC
            """;
        return jdbcTemplate.query(sql, (rs, row) -> {
            CasoPrueba c = new CasoPrueba();
            c.setIdCaso(rs.getInt("idCaso"));
            c.setIdNivel(rs.getInt("idNivel"));
            c.setEntrada(rs.getString("entrada"));
            c.setSalidaEsperada(rs.getString("salidaEsperada"));
            c.setPosicion(rs.getInt("posicion"));
            return c;
        }, idNivel);
    }

    public void eliminar(int idCaso) {
        jdbcTemplate.update("DELETE FROM casoPrueba WHERE idCaso = ?", idCaso);
    }

    public void eliminarPorNivel(int idNivel) {
        jdbcTemplate.update("DELETE FROM casoPrueba WHERE idNivel = ?", idNivel);
    }
}