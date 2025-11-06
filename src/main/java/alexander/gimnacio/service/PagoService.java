package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.SolicitudPago;
import alexander.gimnacio.dto.response.RespuestaPago;
import alexander.gimnacio.entities.Membresia;
import alexander.gimnacio.entities.Pago;
import alexander.gimnacio.entities.Usuario;
import alexander.gimnacio.repository.MembresiaRepository;
import alexander.gimnacio.repository.PagoRepository;
import alexander.gimnacio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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

    // ====== NUEVO: registrar pago proveniente de Stripe ======
    @Transactional
    public void registrarPagoStripe(
            Usuario usuario,
            Membresia membresia,
            String idTransaccionStripe,
            double monto,
            String metodo
    ) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario es requerido para registrar el pago de Stripe");
        }

        // Si no vino la membresía, intenta buscar la del usuario
        if (membresia == null) {
            membresia = repositorioMembresia.findByUsuarioId(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene membresía para asociar al pago"));
        }

        Pago pago = Pago.builder()
                .usuario(usuario)
                .membresia(membresia)
                .monto(monto)
                .metodoPago(Pago.MetodoPago.TARJETA_CREDITO) // o podrías crear TARJETA_STRIPE si quieres
                .estadoPago(Pago.EstadoPago.COMPLETADO)
                .idTransaccion(idTransaccionStripe)
                .build();

        repositorioPago.save(pago);
    }

}
