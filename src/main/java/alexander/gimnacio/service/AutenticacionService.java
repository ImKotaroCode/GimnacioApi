package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class AutenticacionService {
    private final UsuarioRepository repositorioUsuario;
    private final PasswordEncoder codificadorContrasena;
    private final JwtService servicioJwt;

    @Transactional
    public RespuestaAutenticacion registrar(SolicitudRegistro solicitud) {
        if (repositorioUsuario.existsByCorreoElectronico(solicitud.getCorreoElectronico())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombreCompleto(solicitud.getNombreCompleto())
                .correoElectronico(solicitud.getCorreoElectronico())
                .contrasena(codificadorContrasena.encode(solicitud.getContrasena()))
                .numeroTelefono(solicitud.getNumeroTelefono())
                .rol(Usuario.Rol.USUARIO)
                .estaActivo(true)
                .build();

        Usuario usuarioGuardado = repositorioUsuario.save(usuario);
        String token = servicioJwt.generarToken(usuarioGuardado.getCorreoElectronico());

        return RespuestaAutenticacion.builder()
                .token(token)
                .usuarioId(usuarioGuardado.getId())
                .correoElectronico(usuarioGuardado.getCorreoElectronico())
                .nombreCompleto(usuarioGuardado.getNombreCompleto())
                .rol(usuarioGuardado.getRol().name())
                .build();
    }

    public RespuestaAutenticacion iniciarSesion(SolicitudInicioSesion solicitud) {
        Usuario usuario = repositorioUsuario.findByCorreoElectronico(solicitud.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!codificadorContrasena.matches(solicitud.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!usuario.isEstaActivo()) {
            throw new RuntimeException("Cuenta inactiva");
        }

        String token = servicioJwt.generarToken(usuario.getCorreoElectronico());

        return RespuestaAutenticacion.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .correoElectronico(usuario.getCorreoElectronico())
                .nombreCompleto(usuario.getNombreCompleto())
                .rol(usuario.getRol().name())
                .build();
    }
}

