package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RolRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void asignarRol(int idUsuario, int idRol){

        String sql = """
        INSERT INTO rolUsuario(idUsuario,idRol)
        VALUES (?,?)
        """;

        jdbcTemplate.update(sql,idUsuario,idRol);
    }
}