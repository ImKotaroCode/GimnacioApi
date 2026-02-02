package alexander.gimnacio.controller;

import alexander.gimnacio.dto.response.PagoAdminDTO;
import alexander.gimnacio.entities.Membresia;
import alexander.gimnacio.entities.Pago;
import alexander.gimnacio.entities.Usuario;
import alexander.gimnacio.repository.UsuarioRepository;
import alexander.gimnacio.service.MembresiaService;
import alexander.gimnacio.service.PagoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    private final PagoService servicioPago;
    private final MembresiaService membresiaService;
    private final UsuarioRepository usuarioRepository;

    @Value("${stripe.secret.key}")
    private String STRIPE_SECRET_KEY;

    @Value("${stripe.webhook.secret}")
    private String STRIPE_WEBHOOK_SECRET;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<alexander.gimnacio.dto.response.RespuestaPago> procesarPago(
            @Valid @RequestBody alexander.gimnacio.dto.request.SolicitudPago solicitud) {
        return ResponseEntity.ok(servicioPago.procesarPago(solicitud));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PagoAdminDTO>> obtenerPagosPorUsuario(@PathVariable Long usuarioId) {
        List<Pago> pagos = servicioPago.obtenerPagosPorUsuario(usuarioId);
        List<PagoAdminDTO> dto = pagos.stream().map(p -> {
            Long usuarioIdOut = (p.getUsuario() != null) ? p.getUsuario().getId() : null;
            String usuarioNombreOut = (p.getUsuario() != null) ? p.getUsuario().getNombreCompleto() : null;
            Long membresiaIdOut = (p.getMembresia() != null) ? p.getMembresia().getId() : null;

            PagoAdminDTO d = new PagoAdminDTO();
            d.setId(p.getId());
            d.setUsuarioId(usuarioIdOut);
            d.setUsuarioNombre(usuarioNombreOut);
            d.setMembresiaId(membresiaIdOut);
            d.setMonto(p.getMonto());
            d.setFechaPago(p.getFechaPago());
            d.setIdTransaccion(p.getIdTransaccion());
            d.setMetodoPago(p.getMetodoPago() != null ? p.getMetodoPago().name() : null);
            d.setEstadoPago(p.getEstadoPago() != null ? p.getEstadoPago().name() : null);
            return d;
        }).toList();

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/stripe/checkout")
    public ResponseEntity<Map<String, Object>> crearCheckout(@RequestBody Map<String, Object> body) {
        String priceId = (String) body.get("priceId");
        String plan = (String) body.get("plan");
        Long usuarioId = body.get("usuarioId") != null ? Long.valueOf(body.get("usuarioId").toString()) : null;

        if (priceId == null || plan == null || usuarioId == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "priceId, plan y usuarioId son requeridos"));
        }

        try {
            com.stripe.Stripe.apiKey = STRIPE_SECRET_KEY;

            var params = new com.stripe.param.checkout.SessionCreateParams.Builder()
                    .setMode(com.stripe.param.checkout.SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl("https://sistema-gimnacio.vercel.app/index.html?checkout=success")
                    .setCancelUrl("https://sistema-gimnacio.vercel.app/index.html?checkout=cancel")
                    .addLineItem(
                            com.stripe.param.checkout.SessionCreateParams.LineItem.builder()
                                    .setPrice(priceId)
                                    .setQuantity(1L)
                                    .build()
                    )
                    .putMetadata("usuarioId", usuarioId.toString())
                    .putMetadata("plan", plan)
                    .build();

            var session = com.stripe.model.checkout.Session.create(params);

            return ResponseEntity.ok(Map.of("sessionId", session.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload,
                                          @RequestHeader("Stripe-Signature") String sigHeader) {
        log.info("====== Stripe webhook recibido ======");

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (Exception e) {
            log.error("Firma de Stripe inválida o payload malo", e);
            return ResponseEntity.badRequest().build();
        }

        log.info("Evento recibido de Stripe: {}", event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            manejarCheckoutSessionCompleted(event);
        } else {
            log.info("Evento {} ignorado", event.getType());
        }

        return ResponseEntity.ok("ok");
    }

    private void manejarCheckoutSessionCompleted(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        Session session = null;

        if (deserializer.getObject().isPresent()) {
            session = (Session) deserializer.getObject().get();
        } else {
            try {
                String rawJson = deserializer.getRawJson();
                JsonNode node = objectMapper.readTree(rawJson);
                String sessionId = node.get("id").asText();

                com.stripe.Stripe.apiKey = STRIPE_SECRET_KEY;
                session = Session.retrieve(sessionId);
            } catch (Exception ex) {
                log.error("No se pudo deserializar la sesión de checkout (pero el evento sí era checkout.session.completed)");
                return;
            }
        }

        if (session == null) {
            log.error("La sesión vino nula");
            return;
        }

        String usuarioIdStr = session.getMetadata().get("usuarioId");
        String planStr = session.getMetadata().get("plan");

        if (usuarioIdStr == null || planStr == null) {
            log.error("La sesión de Stripe no tenía metadata usuarioId/plan");
            return;
        }

        Long usuarioId = Long.valueOf(usuarioIdStr);

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            log.error("Usuario {} no encontrado en la BD", usuarioId);
            return;
        }

        Membresia.TipoPlan planEnum;
        try {
            planEnum = Membresia.TipoPlan.valueOf(planStr.toUpperCase());
        } catch (Exception e) {
            log.error("Plan {} no coincide con el enum Membresia.TipoPlan", planStr);
            return;
        }

        membresiaService.activarMembresiaDesdeStripe(usuario, planEnum);

        long amountTotal = session.getAmountTotal() != null ? session.getAmountTotal() : 0L;
        double monto = amountTotal / 100.0;

        servicioPago.registrarPagoStripe(
                usuario,
                null,                
                session.getId(),
                monto,
                "STRIPE"
        );

        log.info("Pago y membresía registrados para usuario {}", usuarioId);
    }
}
