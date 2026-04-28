package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pchy.pchy.models.Usuario;
import pchy.pchy.repository.UsuarioRepository;
import pchy.pchy.repository.RolRepository;

import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    private final int ADMIN = 1;
    private final int PROFESOR = 2;
    private final int ALUMNO = 3;

    public void registrarAlumno(Usuario usuario){

        usuario.setContrasena(
            encoder.encode(
                usuario.getContrasena()
            )
        );

        usuarioRepository.guardarUsuario(usuario);

        int idUsuario =
            usuarioRepository.obtenerIdPorCorreo(
                usuario.getCorreo()
            );

        rolRepository.asignarRol(idUsuario, ALUMNO);
    }


    public void registrarAdmin(Usuario usuario){

        usuario.setContrasena(
            encoder.encode(
                usuario.getContrasena()
            )
        );

        usuarioRepository.guardarUsuario(usuario);

        int idUsuario =
            usuarioRepository.obtenerIdPorCorreo(
                usuario.getCorreo()
            );

        rolRepository.asignarRol(idUsuario, ADMIN);
    }


    public void registrarProfesor(Usuario usuario){

        String passwordPlano =
                generarPassword();

        usuario.setContrasena(
            encoder.encode(passwordPlano)
        );

        usuarioRepository.guardarUsuario(usuario);

        int idUsuario =
            usuarioRepository.obtenerIdPorCorreo(
                usuario.getCorreo()
            );

        rolRepository.asignarRol(
            idUsuario,
            PROFESOR
        );

        enviarCorreoProfesor(
            usuario.getCorreo(),
            passwordPlano
        );
    }


    private String generarPassword(){

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,10);
    }


    private void enviarCorreoProfesor(
            String correo,
            String password){

        try{

            SimpleMailMessage mensaje =
                    new SimpleMailMessage();

            mensaje.setFrom(
                "pichyowo9@gmail.com"
            );

            mensaje.setTo(correo);

            mensaje.setSubject(
                "Bienvenido a P-Chy"
            );

            mensaje.setText(
                "Hola y bienvenido al sistema P-Chy.\n\n" +

                "Tu cuenta de profesor fue creada correctamente.\n\n" +

                "Datos de acceso:\n" +
                "Correo: " + correo + "\n" +
                "Contraseña: " + password + "\n\n" +

                "Recuerda cambiar tu contraseña al iniciar sesión.\n\n" +

                "Equipo P-Chy"
            );

            mailSender.send(mensaje);

            System.out.println(
                "Correo enviado correctamente a: "
                + correo
            );

        }catch(Exception e){

            System.out.println(
                "Error al enviar correo:"
            );

            e.printStackTrace();
        }
    }
}