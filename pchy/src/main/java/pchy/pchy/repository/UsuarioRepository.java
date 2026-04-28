package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Usuario;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void guardarUsuario(Usuario u){

        String sql = """
        INSERT INTO usuario
        (matricula,correo,nombre,apellido,institucion,contrasena)
        VALUES (?,?,?,?,?,?)
        """;

        jdbcTemplate.update(sql,
            u.getMatricula(),
            u.getCorreo(),
            u.getNombre(),
            u.getApellido(),
            u.getInstitucion(),
            u.getContrasena()
        );
    }


    public int obtenerIdPorCorreo(String correo){

        String sql = """
        SELECT idUsuario
        FROM usuario
        WHERE correo = ?
        """;

        return jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            correo
        );
    }


    public Usuario buscarPorCorreo(String correo){

        String sql = """
        SELECT *
        FROM usuario
        WHERE correo = ?
        """;

        return jdbcTemplate.query(
            sql,
            rs -> {

                if(rs.next()){

                    Usuario u = new Usuario();

                    u.setIdUsuario(
                        rs.getInt("idUsuario")
                    );

                    u.setMatricula(
                        rs.getString("matricula")
                    );

                    u.setCorreo(
                        rs.getString("correo")
                    );

                    u.setNombre(
                        rs.getString("nombre")
                    );

                    u.setApellido(
                        rs.getString("apellido")
                    );

                    u.setInstitucion(
                        rs.getString("institucion")
                    );

                    u.setContrasena(
                        rs.getString("contrasena")
                    );

                    return u;
                }

                return null;
            },
            correo
        );
    }


    public int obtenerRol(int idUsuario){

        String sql = """
        SELECT idRol
        FROM rolUsuario
        WHERE idUsuario = ?
        """;

        return jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            idUsuario
        );
    }
}