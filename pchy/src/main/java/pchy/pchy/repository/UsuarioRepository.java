package pchy.pchy.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pchy.pchy.models.Usuario;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void guardarUsuario(Usuario u) {

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
                u.getContrasena());
    }

    public int obtenerIdPorCorreo(String correo) {

        String sql = """
                SELECT idUsuario
                FROM usuario
                WHERE correo = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                correo);
    }

    public Usuario buscarPorCorreo(String correo) {

        String sql = """
                SELECT *
                FROM usuario
                WHERE correo = ?
                """;

        return jdbcTemplate.query(
                sql,
                rs -> {

                    if (rs.next()) {

                        Usuario u = new Usuario();

                        u.setIdUsuario(
                                rs.getInt("idUsuario"));

                        u.setMatricula(
                                rs.getString("matricula"));

                        u.setCorreo(
                                rs.getString("correo"));

                        u.setNombre(
                                rs.getString("nombre"));

                        u.setApellido(
                                rs.getString("apellido"));

                        u.setInstitucion(
                                rs.getString("institucion"));

                        u.setContrasena(
                                rs.getString("contrasena"));

                        return u;
                    }

                    return null;
                },
                correo);
    }

    public int obtenerRol(int idUsuario) {

        String sql = """
                SELECT idRol
                FROM rolUsuario
                WHERE idUsuario = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                idUsuario);
    }

    public boolean existeCorreo(String correo) {

        String sql = """
                SELECT COUNT(*)
                FROM usuario
                WHERE correo = ?
                """;

        Integer total = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                correo);

        return total > 0;
    }

    public boolean existeMatricula(String matricula) {

        String sql = """
                SELECT COUNT(*)
                FROM usuario
                WHERE matricula = ?
                """;

        Integer total = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                matricula);

        return total > 0;
    }

    public Usuario obtenerPerfilCompleto(String correo) {

        String sql = """
                SELECT *
                FROM usuario
                WHERE correo = ?
                """;

        return jdbcTemplate.query(
                sql,
                rs -> {

                    if (rs.next()) {

                        Usuario u = new Usuario();

                        u.setIdUsuario(rs.getInt("idUsuario"));
                        u.setMatricula(rs.getString("matricula"));
                        u.setCorreo(rs.getString("correo"));
                        u.setNombre(rs.getString("nombre"));
                        u.setApellido(rs.getString("apellido"));
                        u.setInstitucion(rs.getString("institucion"));
                        u.setFotoPerfil(rs.getString("fotoPerfil"));
                        u.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                        u.setAsistente(rs.getBoolean("asistente"));
                        u.setPuntaje(rs.getInt("puntaje"));
                        u.setActivo(rs.getBoolean("activo"));
                        u.setFechaRegistro(rs.getTimestamp("fechaRegistro"));

                        return u;
                    }

                    return null;
                },
                correo);
    }

    public void actualizarPerfil(Usuario u) {

        String sql = """
                UPDATE usuario
                SET nombre = ?, apellido = ?, fechaNacimiento = ?
                WHERE correo = ?
                """;

        jdbcTemplate.update(
                sql,
                u.getNombre(),
                u.getApellido(),
                u.getFechaNacimiento(),
                u.getCorreo());
    }

    public void actualizarPassword(String correo, String nueva) {

        String sql = """
                UPDATE usuario
                SET contrasena = ?
                WHERE correo = ?
                """;

        jdbcTemplate.update(
                sql,
                nueva,
                correo);
    }

    public void actualizarFoto(String correo, String ruta) {

        String sql = """
                UPDATE usuario
                SET fotoPerfil = ?
                WHERE correo = ?
                """;

        jdbcTemplate.update(
                sql,
                ruta,
                correo);
    }

    public void sumarPuntos(int idUsuario, int puntos) {
        String sql = """
                UPDATE usuario
                SET puntaje = puntaje + ?
                WHERE idUsuario = ?
                """;
        jdbcTemplate.update(sql, puntos, idUsuario);
    }

    public Usuario obtenerPerfilPorId(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setMatricula(rs.getString("matricula"));
                u.setInstitucion(rs.getString("institucion"));
                u.setFotoPerfil(rs.getString("fotoPerfil"));
                u.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                u.setPuntaje(rs.getInt("puntaje"));
                u.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                return u;
            }
            return null;
        }, idUsuario);
    }

    public int obtenerRolPorId(int idUsuario) {
        String sql = """
                SELECT idRol FROM rolUsuario
                WHERE idUsuario = ?
                LIMIT 1
                """;
        try {
            Integer rol = jdbcTemplate.queryForObject(sql, Integer.class, idUsuario);
            return rol != null ? rol : 3;
        } catch (Exception e) {
            return 3;
        }
    }

}