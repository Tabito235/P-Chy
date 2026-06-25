package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PuntajeClaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Sumar puntos a la clase 
    public void sumarPuntos(int idUsuario, int idClase, int puntos) {
        String sql = """
                INSERT INTO puntajeClase (idUsuario, idClase, puntaje)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE puntaje = puntaje + VALUES(puntaje)
                """;
        jdbcTemplate.update(sql, idUsuario, idClase, puntos);
    }

    public java.util.List<java.util.Map<String, Object>> rankingPorClase(
            int idClase) {

        String sql = """
                SELECT u.idUsuario, u.nombre, u.apellido,
                       u.fotoPerfil, u.puntaje AS puntajeGlobal,
                       pc.puntaje AS puntajeClase
                FROM puntajeClase pc
                JOIN usuario u ON u.idUsuario = pc.idUsuario
                JOIN rolUsuario ru ON ru.idUsuario = u.idUsuario
                WHERE pc.idClase = ? AND ru.idRol = 3
                ORDER BY pc.puntaje DESC
                """;

        return jdbcTemplate.queryForList(sql, idClase);
    }

    // Puntaje de un alumno en una clase
    public int obtenerPuntaje(int idUsuario, int idClase) {
        String sql = """
                SELECT puntaje FROM puntajeClase
                WHERE idUsuario = ? AND idClase = ?
                """;
        try {
            Integer p = jdbcTemplate.queryForObject(
                    sql, Integer.class, idUsuario, idClase);
            return p != null ? p : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}