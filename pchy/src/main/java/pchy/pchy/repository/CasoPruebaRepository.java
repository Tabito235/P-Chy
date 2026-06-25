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
                INSERT INTO casoPrueba (idProblema, entrada, salidaEsperada, posicion)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql,
                caso.getIdProblema(),
                caso.getEntrada(),
                caso.getSalidaEsperada(),
                caso.getPosicion());
    }

    public List<CasoPrueba> listarPorProblema(int idProblema) {
        String sql = """
                SELECT * FROM casoPrueba
                WHERE idProblema = ?
                ORDER BY posicion ASC
                """;
        return jdbcTemplate.query(sql, (rs, row) -> {
            CasoPrueba c = new CasoPrueba();
            c.setIdCaso(rs.getInt("idCaso"));
            c.setIdProblema(rs.getInt("idProblema"));
            c.setEntrada(rs.getString("entrada"));
            c.setSalidaEsperada(rs.getString("salidaEsperada"));
            c.setPosicion(rs.getInt("posicion"));
            return c;
        }, idProblema);
    }

    public void eliminar(int idCaso) {
        jdbcTemplate.update("DELETE FROM casoPrueba WHERE idCaso = ?", idCaso);
    }
}