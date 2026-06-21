package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Entrega;
import pchy.pchy.models.ResultadoCaso;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class EntregaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Crear entrega
    public int crear(Entrega e) {

        String sql = """
            INSERT INTO entrega
            (idUsuario, idNivel, idProblema, archivoCodigo,
             archivoCaptura, puntaje, estado, resultadoJudge,
             porcentajeCasos, mensajeError)
            VALUES (?, ?, ?, ?, ?, 0, 'PENDIENTE', 'PENDIENTE', 0, NULL)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, e.getIdUsuario());
            ps.setInt(2, e.getIdNivel());
            ps.setInt(3, e.getIdProblema());
            ps.setString(4, e.getArchivoCodigo());
            ps.setString(5, e.getArchivoCaptura());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // Actualizar resultado del Judge
    public void actualizarResultadoJudge(int idEntrega, String resultado,
                                          int porcentaje, String mensajeError) {
        String sql = """
            UPDATE entrega
            SET resultadoJudge = ?, porcentajeCasos = ?,
                mensajeError = ?, estado = ?
            WHERE idEntrega = ?
            """;

         String estado = "REVISION";

        jdbcTemplate.update(sql, resultado, porcentaje,
            mensajeError, estado, idEntrega);
    }

    // Guardar resultado por caso
    public void guardarResultadoCaso(ResultadoCaso rc) {

        String sql = """
            INSERT INTO resultadoCaso
            (idEntrega, idCaso, correcto, salidaObtenida, tiempoEjecucion)
            VALUES (?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
            rc.getIdEntrega(),
            rc.getIdCaso(),
            rc.isCorrecto(),
            rc.getSalidaObtenida(),
            rc.getTiempoEjecucion()
        );
    }

    // Obtener última entrega del alumno para un problema
    public Entrega obtenerUltima(int idUsuario, int idProblema) {

        String sql = """
            SELECT * FROM entrega
            WHERE idUsuario = ? AND idProblema = ?
            ORDER BY fechaEntrega DESC
            LIMIT 1
            """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        }, idUsuario, idProblema);
    }

    // Historial de entregas de un alumno para un problema
    public List<Entrega> listarPorProblema(int idUsuario, int idProblema) {

        String sql = """
            SELECT * FROM entrega
            WHERE idUsuario = ? AND idProblema = ?
            ORDER BY fechaEntrega DESC
            """;

        return jdbcTemplate.query(sql,
            (rs, row) -> mapear(rs), idUsuario, idProblema);
    }

    // Resultados por caso de una entrega
    public List<ResultadoCaso> listarResultadosCasos(int idEntrega) {

        String sql = """
            SELECT rc.*, cp.entrada, cp.salidaEsperada
            FROM resultadoCaso rc
            JOIN casoPrueba cp ON cp.idCaso = rc.idCaso
            WHERE rc.idEntrega = ?
            ORDER BY cp.posicion ASC
            """;

        return jdbcTemplate.query(sql, (rs, row) -> {
            ResultadoCaso rc = new ResultadoCaso();
            rc.setIdResultado(rs.getInt("idResultado"));
            rc.setIdEntrega(rs.getInt("idEntrega"));
            rc.setIdCaso(rs.getInt("idCaso"));
            rc.setCorrecto(rs.getBoolean("correcto"));
            rc.setSalidaObtenida(rs.getString("salidaObtenida"));
            rc.setTiempoEjecucion(rs.getFloat("tiempoEjecucion"));
            rc.setEntrada(rs.getString("entrada"));
            rc.setSalidaEsperada(rs.getString("salidaEsperada"));
            return rc;
        }, idEntrega);
    }

    // Verificar si el alumno ya completó el problema (APROBADA)
    public boolean estaAprobado(int idUsuario, int idProblema) {

        String sql = """
            SELECT COUNT(*) FROM entrega
            WHERE idUsuario = ? AND idProblema = ?
            AND estado = 'APROBADA'
            """;

        Integer total = jdbcTemplate.queryForObject(
            sql, Integer.class, idUsuario, idProblema
        );
        return total != null && total > 0;
    }

    // Contar problemas aprobados en un nivel
    public int contarAprobadosEnNivel(int idUsuario, int idNivel) {

        String sql = """
            SELECT COUNT(DISTINCT e.idProblema)
            FROM entrega e
            WHERE e.idUsuario = ? AND e.idNivel = ?
            AND e.estado = 'APROBADA'
            """;

        Integer total = jdbcTemplate.queryForObject(
            sql, Integer.class, idUsuario, idNivel
        );
        return total != null ? total : 0;
    }

    private Entrega mapear(java.sql.ResultSet rs) throws java.sql.SQLException {
        Entrega e = new Entrega();
        e.setIdEntrega(rs.getInt("idEntrega"));
        e.setIdUsuario(rs.getInt("idUsuario"));
        e.setIdNivel(rs.getInt("idNivel"));
        e.setIdProblema(rs.getInt("idProblema"));
        e.setArchivoCodigo(rs.getString("archivoCodigo"));
        e.setArchivoCaptura(rs.getString("archivoCaptura"));
        e.setPuntaje(rs.getInt("puntaje"));
        e.setEstado(rs.getString("estado"));
        e.setResultadoJudge(rs.getString("resultadoJudge"));
        e.setPorcentajeCasos(rs.getInt("porcentajeCasos"));
        e.setMensajeError(rs.getString("mensajeError"));
        e.setFechaEntrega(rs.getTimestamp("fechaEntrega"));
        return e;
    }

    // Verificar si el alumno ya completó un problema (Judge ACEPTADO)
public boolean problemaCompletado(int idUsuario, int idProblema) {
    String sql = """
        SELECT COUNT(*) FROM entrega
        WHERE idUsuario = ? AND idProblema = ?
        AND resultadoJudge = 'ACEPTADO'
        """;
    Integer total = jdbcTemplate.queryForObject(sql, Integer.class,
        idUsuario, idProblema);
    return total != null && total > 0;
}

// Contar problemas distintos completados (Judge ACEPTADO) en un nivel
public int contarProblemasCompletadosEnNivel(int idUsuario, int idNivel) {
    String sql = """
        SELECT COUNT(DISTINCT e.idProblema)
        FROM entrega e
        WHERE e.idUsuario = ? AND e.idNivel = ?
        AND e.resultadoJudge = 'ACEPTADO'
        """;
    Integer total = jdbcTemplate.queryForObject(sql, Integer.class,
        idUsuario, idNivel);
    return total != null ? total : 0;
}

// Contar cuántas veces el alumno ha completado (ACEPTADO) un problema
public long contarEntregasAceptadasParaProblema(int idUsuario, int idProblema) {
    String sql = """
        SELECT COUNT(*) FROM entrega
        WHERE idUsuario = ? AND idProblema = ?
        AND resultadoJudge = 'ACEPTADO'
        """;
    Long total = jdbcTemplate.queryForObject(sql, Long.class,
        idUsuario, idProblema);
    return total != null ? total : 0;
}

}