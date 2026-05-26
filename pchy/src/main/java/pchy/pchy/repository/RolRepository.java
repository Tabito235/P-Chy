package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Usuario;

import java.util.List;

@Repository
public class RolRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void asignarRol(int idUsuario, int idRol) {
        String sql = "INSERT INTO rolUsuario(idUsuario, idRol) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUsuario, idRol);
    }

    // Elimina todos los roles del usuario
    public void eliminarRoles(int idUsuario) {
        String sql = "DELETE FROM rolUsuario WHERE idUsuario = ?";
        jdbcTemplate.update(sql, idUsuario);
    }

    // Lista todos los usuarios con su rol actual
    public List<Usuario> listarUsuariosConRol() {

        String sql = """
            SELECT u.idUsuario, u.nombre, u.apellido, u.correo,
                   u.matricula, u.institucion, u.fotoPerfil,
                   u.activo, r.idRol, r.nombre AS nombreRol
            FROM usuario u
            JOIN rolUsuario ru ON u.idUsuario = ru.idUsuario
            JOIN rol r ON ru.idRol = r.idRol
            ORDER BY r.idRol ASC, u.nombre ASC
            """;

        return jdbcTemplate.query(sql, (rs, row) -> {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setCorreo(rs.getString("correo"));
            u.setMatricula(rs.getString("matricula"));
            u.setInstitucion(rs.getString("institucion"));
            u.setFotoPerfil(rs.getString("fotoPerfil"));
            u.setActivo(rs.getBoolean("activo"));
            u.setRolActual(rs.getString("nombreRol"));
            u.setIdRolActual(rs.getInt("idRol"));
            return u;
        });
    }
}