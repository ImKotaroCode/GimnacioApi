package alexander.gimnacio.service;
import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service
@RequiredArgsConstructor
public class ContactoService {
    private final MensajeContactoRepository repositorioMensaje;

    @Transactional
    public String guardarMensajeContacto(SolicitudContacto solicitud) {
        MensajeContacto mensaje = MensajeContacto.builder()
                .nombreRemitente(solicitud.getNombreRemitente())
                .correoRemitente(solicitud.getCorreoRemitente())
                .asunto(solicitud.getAsunto())
                .mensaje(solicitud.getMensaje())
                .estaLeido(false)
                .build();

        repositorioMensaje.save(mensaje);
        return "Mensaje enviado exitosamente. Te contactaremos pronto.";
    }

    public List<MensajeContacto> obtenerTodosLosMensajes() {
        return repositorioMensaje.findAll();
    }

    public List<MensajeContacto> obtenerMensajesNoLeidos() {
        return repositorioMensaje.findByEstaLeidoFalse();
    }
}
