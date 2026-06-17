package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pchy.pchy.models.RankingEntry;
import pchy.pchy.repository.PuntajeClaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RankingService {

    @Autowired
    private PuntajeClaseRepository puntajeClaseRepository;

    // Umbrales de medallas
    private static final int BRONCE   = 100;
    private static final int PLATA    = 500;
    private static final int ORO      = 1000;
    private static final int DIAMANTE = 5000;

    public String calcularMedalla(int puntaje) {
        if (puntaje >= DIAMANTE) return "DIAMANTE";
        if (puntaje >= ORO)      return "ORO";
        if (puntaje >= PLATA)    return "PLATA";
        if (puntaje >= BRONCE)   return "BRONCE";
        return null;
    }

    // Ranking por clase
    public List<RankingEntry> rankingPorClase(int idClase) {

        List<Map<String, Object>> rows =
            puntajeClaseRepository.rankingPorClase(idClase);

        List<RankingEntry> lista = new ArrayList<>();
        int pos = 1;

        for (Map<String, Object> row : rows) {
            RankingEntry e = new RankingEntry();
            e.setPosicion(pos++);
            e.setIdUsuario(((Number) row.get("idUsuario")).intValue());
            e.setNombre((String) row.get("nombre"));
            e.setApellido((String) row.get("apellido"));
            e.setFotoPerfil((String) row.get("fotoPerfil"));
            e.setPuntaje(((Number) row.get("puntajeClase")).intValue());

            // Medalla basada en puntaje GLOBAL
            int globalPts = ((Number) row.get("puntajeGlobal")).intValue();
            e.setMedalla(calcularMedalla(globalPts));

            lista.add(e);
        }

        return lista;
    }

    // Ranking global — solo alumnos
    public List<RankingEntry> rankingGlobal() {

        // Reutilizamos UsuarioRepository directamente via SQL
        return rankingGlobalQuery();
    }

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    private List<RankingEntry> rankingGlobalQuery() {

        String sql = """
            SELECT u.idUsuario, u.nombre, u.apellido,
                   u.fotoPerfil, u.puntaje
            FROM usuario u
            JOIN rolUsuario ru ON ru.idUsuario = u.idUsuario
            WHERE ru.idRol = 3 AND u.activo = TRUE
            ORDER BY u.puntaje DESC
            """;

        List<RankingEntry> lista = new ArrayList<>();
        int[] pos = {1};

        jdbcTemplate.query(sql, rs -> {
            RankingEntry e = new RankingEntry();
            e.setPosicion(pos[0]++);
            e.setIdUsuario(rs.getInt("idUsuario"));
            e.setNombre(rs.getString("nombre"));
            e.setApellido(rs.getString("apellido"));
            e.setFotoPerfil(rs.getString("fotoPerfil"));
            e.setPuntaje(rs.getInt("puntaje"));
            e.setMedalla(calcularMedalla(rs.getInt("puntaje")));
            lista.add(e);
        });

        return lista;
    }

    // Posición del usuario en el ranking global
    public int posicionGlobal(int idUsuario) {
        String sql = """
            SELECT COUNT(*) + 1
            FROM usuario u
            JOIN rolUsuario ru ON ru.idUsuario = u.idUsuario
            WHERE ru.idRol = 3
            AND u.puntaje > (
                SELECT puntaje FROM usuario WHERE idUsuario = ?
            )
            """;
        Integer pos = jdbcTemplate.queryForObject(sql, Integer.class, idUsuario);
        return pos != null ? pos : 1;
    }

    // Posición del usuario en el ranking de una clase
    public int posicionEnClase(int idUsuario, int idClase) {
        String sql = """
            SELECT COUNT(*) + 1
            FROM puntajeClase pc
            JOIN rolUsuario ru ON ru.idUsuario = pc.idUsuario
            WHERE pc.idClase = ? AND ru.idRol = 3
            AND pc.puntaje > (
                SELECT COALESCE(puntaje, 0)
                FROM puntajeClase
                WHERE idUsuario = ? AND idClase = ?
            )
            """;
        try {
            Integer pos = jdbcTemplate.queryForObject(
                sql, Integer.class, idClase, idUsuario, idClase
            );
            return pos != null ? pos : 1;
        } catch (Exception e) {
            return 0;
        }
    }
}