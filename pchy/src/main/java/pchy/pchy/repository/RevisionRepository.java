package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Entrega;
import pchy.pchy.models.ResultadoCaso;
import pchy.pchy.models.Usuario;

import java.util.List;

@Repository
public class RevisionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Alumnos con entregas en una competencia
    public List<Usuario> listarAlumnosConEntregas(int idCompetencia) {

        String sql = """
            SELECT DISTINCT u.idUsuario, u.nombre, u.apellido,
                   u.correo, u.matricula, u.fotoPerfil,
                   COUNT(DISTINCT e.idProblema) AS problemasEntregados
            FROM usuario u
            JOIN entrega e ON e.idUsuario = u.idUsuario
            JOIN problema p ON p.idProblema = e.idProblema
            JOIN nivel n ON n.idNivel = p.idNivel
            WHERE n.idCompetencia = ?
            GROUP BY u.idUsuario
            ORDER BY u.nombre ASC
            """;

        return jdbcTemplate.query(sql, (rs, row) -> {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setCorreo(rs.getString("correo"));
            u.setMatricula(rs.getString("matricula"));
            u.setFotoPerfil(rs.getString("fotoPerfil"));
            u.setRolActual(rs.getInt("problemasEntregados") + " entregas");
            return u;
        }, idCompetencia);
    }

    // Última entrega de un alumno por problema
    public List<Entrega> listarUltimasEntregasAlumno(int idUsuario,
                                                       int idCompetencia) {
        String sql = """
            SELECT e.*
            FROM entrega e
            JOIN (
                SELECT idProblema, MAX(fechaEntrega) AS ultima
                FROM entrega
                WHERE idUsuario = ?
                GROUP BY idProblema
            ) ult ON ult.idProblema = e.idProblema
                 AND ult.ultima = e.fechaEntrega
            JOIN problema p ON p.idProblema = e.idProblema
            JOIN nivel n ON n.idNivel = p.idNivel
            WHERE e.idUsuario = ? AND n.idCompetencia = ?
            ORDER BY p.posicion ASC
            """;

        return jdbcTemplate.query(sql, (rs, row) -> {
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
        }, idUsuario, idUsuario, idCompetencia);
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

    // Validar entrega manualmente (aprobar o rechazar)
    public void validarEntrega(int idEntrega, String estado,
                                int puntaje, String comentario,
                                int idRevisor) {

        // Actualizar entrega
        String sqlEntrega = """
            UPDATE entrega
            SET estado = ?, puntaje = ?
            WHERE idEntrega = ?
            """;
        jdbcTemplate.update(sqlEntrega, estado, puntaje, idEntrega);

        // Guardar revisión
        String sqlRevision = """
            INSERT INTO revision
            (idEntrega, idRevisor, comentario, aprobado)
            VALUES (?, ?, ?, ?)
            """;
        jdbcTemplate.update(sqlRevision,
            idEntrega, idRevisor,
            comentario,
            "APROBADA".equals(estado)
        );
    }
}