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
public class UbicacionService {
    private final UbicacionRepository repositorioUbicacion;

    public List<Ubicacion> obtenerTodasLasUbicacionesActivas() {
        return repositorioUbicacion.findByEstaActivoTrue();
    }

    public List<Ubicacion> obtenerUbicacionesPorDistrito(String distrito) {
        return repositorioUbicacion.findByDistrito(distrito);
    }

    public Ubicacion obtenerUbicacionPorId(Long id) {
        return repositorioUbicacion.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
    }
}
