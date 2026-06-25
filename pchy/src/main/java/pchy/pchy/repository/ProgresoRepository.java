package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProgresoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Verificar si un nivel está desbloqueado para el alumno
    public boolean nivelDesbloqueado(int idUsuario, int idNivel) {
        String sql = """
                SELECT COUNT(*) FROM progresoNivel
                WHERE idUsuario = ? AND idNivel = ?
                AND desbloqueado = TRUE
                """;
        Integer total = jdbcTemplate.queryForObject(
                sql, Integer.class, idUsuario, idNivel);
        return total != null && total > 0;
    }

    // Obtener problemas completados en un nivel
    public int obtenerProblemasCompletados(int idUsuario, int idNivel) {
        String sql = """
                SELECT problemasCompletados FROM progresoNivel
                WHERE idUsuario = ? AND idNivel = ?
                """;
        try {
            Integer total = jdbcTemplate.queryForObject(
                    sql, Integer.class, idUsuario, idNivel);
            return total != null ? total : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    // Crear o actualizar progreso del nivel
    public void actualizarProgreso(int idUsuario, int idNivel,
            int problemasCompletados,
            boolean desbloqueado) {
        String sql = """
                INSERT INTO progresoNivel
                    (idUsuario, idNivel, problemasCompletados, desbloqueado, fechaDesbloqueo)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    problemasCompletados = VALUES(problemasCompletados),
                    desbloqueado = VALUES(desbloqueado),
                    fechaDesbloqueo = VALUES(fechaDesbloqueo)
                """;

        java.sql.Timestamp ahora = desbloqueado ? new java.sql.Timestamp(System.currentTimeMillis()) : null;

        jdbcTemplate.update(sql,
                idUsuario, idNivel, problemasCompletados,
                desbloqueado, ahora);
    }

    // Desbloquear el primer nivel de una competencia para el alumno
    public void desbloquearPrimerNivel(int idUsuario, int idNivel) {
        String sql = """
                INSERT IGNORE INTO progresoNivel
                    (idUsuario, idNivel, problemasCompletados, desbloqueado, fechaDesbloqueo)
                VALUES (?, ?, 0, TRUE, NOW())
                """;
        jdbcTemplate.update(sql, idUsuario, idNivel);
    }
}