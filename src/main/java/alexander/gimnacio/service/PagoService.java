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
public class PagoService {
    private final PagoRepository repositorioPago;
    private final MembresiaRepository repositorioMembresia;
    private final UsuarioRepository repositorioUsuario;
    private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public RespuestaPago procesarPago(SolicitudPago solicitud) {
        Usuario usuario = repositorioUsuario.findById(solicitud.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Membresia membresia = repositorioMembresia.findById(solicitud.getMembresiaId())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));

        String idTransaccion = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String ultimosCuatroDigitos = solicitud.getNumeroTarjeta().substring(
                solicitud.getNumeroTarjeta().length() - 4
        );

        Pago pago = Pago.builder()
                .usuario(usuario)
                .membresia(membresia)
                .monto(solicitud.getMonto())
                .metodoPago(Pago.MetodoPago.TARJETA_CREDITO)
                .estadoPago(Pago.EstadoPago.COMPLETADO)
                .idTransaccion(idTransaccion)
                .ultimosCuatroDigitos(ultimosCuatroDigitos)
                .build();

        Pago pagoGuardado = repositorioPago.save(pago);

        membresia.setEstado(Membresia.Estado.ACTIVO);
        repositorioMembresia.save(membresia);

        return RespuestaPago.builder()
                .pagoId(pagoGuardado.getId())
                .estado(pagoGuardado.getEstadoPago().name())
                .idTransaccion(pagoGuardado.getIdTransaccion())
                .fechaPago(pagoGuardado.getFechaPago().format(formateador))
                .mensaje("Pago procesado exitosamente")
                .build();
    }

    public List<Pago> obtenerPagosPorUsuario(Long usuarioId) {
        return repositorioPago.findByUsuarioId(usuarioId);
    }
}

