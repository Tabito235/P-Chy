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

        validarDuplicados(usuario);

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

        rolRepository.asignarRol(
            idUsuario,
            ALUMNO
        );
    }

    public void registrarAdmin(Usuario usuario){

        validarDuplicados(usuario);

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

        rolRepository.asignarRol(
            idUsuario,
            ADMIN
        );
    }

    public void registrarProfesor(Usuario usuario){

        validarDuplicados(usuario);

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

    private void validarDuplicados(
            Usuario usuario){

        if(usuarioRepository.existeCorreo(
                usuario.getCorreo())){

            throw new RuntimeException(
                "Correo ya registrado"
            );
        }

        if(usuarioRepository.existeMatricula(
                usuario.getMatricula())){

            throw new RuntimeException(
                "Matrícula ya registrada"
            );
        }
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

                "Tu cuenta de profesor a sido creada de forma exitosamente correcta.\n\n" +

                "Datos de acceso:\n" +
                "Correo: " + correo + "\n" +
                "Contraseña: " + password + "\n\n" +

                "Recuerda cambiar tu contraseña al iniciar sesión... ¡Oye! ¿Sabias que el primer correo electronico fue: QWERTYUIOP? ¡Yo tampoco lo sabia! Lo acabo de ver en google" 
            );

            mailSender.send(mensaje);

        }catch(Exception e){
    throw new RuntimeException("No se pudo enviar correo");
}
    }
}