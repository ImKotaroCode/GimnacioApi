package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.ActualizarUsuarioRequest;
import alexander.gimnacio.dto.request.CrearClaseRequest;
import alexander.gimnacio.dto.request.CrearUbicacionRequest;

import alexander.gimnacio.dto.request.CrearUsuarioAdminRequest;
import alexander.gimnacio.dto.response.EstadisticasDashboard;
import alexander.gimnacio.dto.response.ReporteIngresos;
import alexander.gimnacio.dto.response.ReporteMembresias;
import alexander.gimnacio.dto.response.ReporteClases;

import java.util.LinkedHashMap;

import alexander.gimnacio.dto.response.*;

import alexander.gimnacio.entities.Clase;
import alexander.gimnacio.entities.Membresia;
import alexander.gimnacio.entities.Pago;
import alexander.gimnacio.entities.Ubicacion;
import alexander.gimnacio.entities.Usuario;

import alexander.gimnacio.repository.ClaseRepository;
import alexander.gimnacio.repository.MembresiaRepository;
import alexander.gimnacio.repository.PagoRepository;
import alexander.gimnacio.repository.UbicacionRepository;
import alexander.gimnacio.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final UsuarioRepository rU;
    private final MembresiaRepository rM;
    private final ClaseRepository rC;
    private final UbicacionRepository rUb;
    private final PagoRepository rP;
    private final PasswordEncoder codificadorContrasena;


    public EstadisticasDashboard obtenerEstadisticasDashboard() {
        long totalUsuarios      = rU.count();
        long usuariosActivos    = rU.findAll().stream().filter(Usuario::isEstaActivo).count();
        long totalMembresias    = rM.count();
        long membresiasActivas  = rM.findByEstado(Membresia.Estado.ACTIVO).size();
        long totalClases        = rC.count();
        long totalUbicaciones   = rUb.count();

        LocalDateTime inicioMes  = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime inicioAnio = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        var pagosMes = rP.findAll().stream()
                .filter(p -> p.getFechaPago() != null && p.getFechaPago().isAfter(inicioMes))
                .toList();

        double ingresosMensuales = pagosMes.stream()
                .filter(p -> p.getEstadoPago() == Pago.EstadoPago.COMPLETADO)
                .mapToDouble(Pago::getMonto)
                .sum();

        double ingresosAnuales = rP.findAll().stream()
                .filter(p -> p.getFechaPago() != null && p.getFechaPago().isAfter(inicioAnio))
                .filter(p -> p.getEstadoPago() == Pago.EstadoPago.COMPLETADO)
                .mapToDouble(Pago::getMonto)
                .sum();

        long nuevosMiembrosMes = rU.findAll().stream()
                .filter(u -> u.getFechaCreacion() != null && u.getFechaCreacion().isAfter(inicioMes))
                .count();

        return EstadisticasDashboard.builder()
                .totalUsuarios(totalUsuarios)
                .usuariosActivos(usuariosActivos)
                .totalMembresias(totalMembresias)
                .membresiasActivas(membresiasActivas)
                .totalClases(totalClases)
                .totalUbicaciones(totalUbicaciones)
                .ingresosMensuales(ingresosMensuales)
                .ingresosAnuales(ingresosAnuales)
                .nuevosMiembrosMes(nuevosMiembrosMes)
                .build();
    }

    public ReporteMembresias obtenerReporteMembresias() {
        var ms = rM.findAll();

        long basico    = ms.stream().filter(m -> m.getTipoPlan() == Membresia.TipoPlan.BASICO).count();
        long premium   = ms.stream().filter(m -> m.getTipoPlan() == Membresia.TipoPlan.PREMIUM).count();
        long anual     = ms.stream().filter(m -> m.getTipoPlan() == Membresia.TipoPlan.ANUAL).count();

        long activas   = ms.stream().filter(m -> m.getEstado() == Membresia.Estado.ACTIVO).count();
        long pausadas  = ms.stream().filter(m -> m.getEstado() == Membresia.Estado.PAUSADO).count();
        long canceladas= ms.stream().filter(m -> m.getEstado() == Membresia.Estado.CANCELADO).count();
        long expiradas = ms.stream().filter(m -> m.getEstado() == Membresia.Estado.EXPIRADO).count();

        return ReporteMembresias.builder()
                .totalMembresias((long) ms.size())
                .membresiaBasico(basico)
                .membresiaPremium(premium)
                .membresiaAnual(anual)
                .membresiaActivas(activas)
                .membresiasPausadas(pausadas)
                .membresiasCanceladas(canceladas)
                .membresiasExpiradas(expiradas)
                .build();
    }

    public List<ReporteClases> obtenerReporteClases(){
        return rC.findAll().stream().map(c -> {
            int inscritos = (c.getUsuariosInscritos() == null) ? 0 : c.getUsuariosInscritos().size();
            Integer capObj = c.getCapacidadMaxima();
            int cap = (capObj == null) ? 0 : capObj;

            double tasa = (cap > 0) ? ( (double) inscritos / cap ) * 100.0 : 0.0;

            return ReporteClases.builder()
                    .nombreClase(c.getNombreClase())
                    .capacidadMaxima(cap)
                    .usuariosInscritos(inscritos)
                    .tasaOcupacion(tasa)
                    .nombreInstructor(c.getNombreInstructor())
                    .build();
        }).toList();
    }


    public ReporteIngresos obtenerReporteIngresos(String periodo) {
        LocalDateTime inicio = "anual".equalsIgnoreCase(periodo)
                ? LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
                : LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        var pagos = rP.findAll().stream()
                .filter(p -> p.getFechaPago() != null && p.getFechaPago().isAfter(inicio))
                .filter(p -> p.getEstadoPago() == Pago.EstadoPago.COMPLETADO)
                .toList();

        double total = pagos.stream().mapToDouble(Pago::getMonto).sum();
        double promedio = pagos.isEmpty() ? 0 : total / pagos.size();

        Map<Pago.MetodoPago, Long> metodos = pagos.stream()
                .collect(Collectors.groupingBy(Pago::getMetodoPago, Collectors.counting()));

        String top = metodos.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse("N/A");

        return ReporteIngresos.builder()
                .periodo(periodo)
                .totalIngresos(total)
                .cantidadPagos((long) pagos.size())
                .promedioIngreso(promedio)
                .metodoPagoMasUsado(top)
                .build();
    }


    public List<UsuarioAdminDTO> obtenerUsuariosAdmin() {
        return rU.findAll().stream()
                .map(this::toUsuarioAdminDTO)
                .sorted(Comparator.comparing(UsuarioAdminDTO::getId))
                .toList();
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, ActualizarUsuarioRequest r) {
        var u = rU.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (r.getNombreCompleto() != null) u.setNombreCompleto(r.getNombreCompleto());
        if (r.getNumeroTelefono() != null) u.setNumeroTelefono(r.getNumeroTelefono());
        if (r.getRol() != null) u.setRol(Usuario.Rol.valueOf(r.getRol()));
        if (r.getEstaActivo() != null) u.setEstaActivo(r.getEstaActivo());
        return rU.save(u);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        rU.deleteById(id);
    }


    @Transactional
    public Clase crearClase(CrearClaseRequest r) {
        boolean activo = (r.getEstaActivo() == null) ? true : r.getEstaActivo();

        var c = Clase.builder()
                .nombreClase(r.getNombreClase())
                .descripcion(r.getDescripcion())
                .horario(r.getHorario())
                .nombreInstructor(r.getNombreInstructor())
                .capacidadMaxima(r.getCapacidadMaxima())
                .duracionMinutos(r.getDuracionMinutos())
                .estaActivo(activo)
                .build();
        return rC.save(c);
    }

    @Transactional
    public Clase actualizarClase(Long id, CrearClaseRequest r) {
        var c = rC.findById(id).orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        c.setNombreClase(r.getNombreClase());
        c.setDescripcion(r.getDescripcion());
        c.setHorario(r.getHorario());
        c.setNombreInstructor(r.getNombreInstructor());
        c.setCapacidadMaxima(r.getCapacidadMaxima());
        c.setDuracionMinutos(r.getDuracionMinutos());

        if (r.getEstaActivo() != null) {
            c.setEstaActivo(r.getEstaActivo());
        }

        return rC.save(c);
    }

    @Transactional
    public void eliminarClase(Long id) {
        rC.deleteById(id);
    }


    @Transactional
    public Ubicacion crearUbicacion(CrearUbicacionRequest r) {
        var u = Ubicacion.builder()
                .nombreUbicacion(r.getNombreUbicacion())
                .direccion(r.getDireccion())
                .distrito(r.getDistrito())
                .ciudad(r.getCiudad())
                .numeroTelefono(r.getNumeroTelefono())
                .horario(r.getHorario())
                .latitud(r.getLatitud())
                .longitud(r.getLongitud())
                .estaActivo(true)
                .build();
        return rUb.save(u);
    }

    @Transactional
    public Ubicacion actualizarUbicacion(Long id, CrearUbicacionRequest r) {
        var u = rUb.findById(id).orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
        u.setNombreUbicacion(r.getNombreUbicacion());
        u.setDireccion(r.getDireccion());
        u.setDistrito(r.getDistrito());
        u.setCiudad(r.getCiudad());
        u.setNumeroTelefono(r.getNumeroTelefono());
        u.setHorario(r.getHorario());
        u.setLatitud(r.getLatitud());
        u.setLongitud(r.getLongitud());
        return rUb.save(u);
    }

    @Transactional
    public void eliminarUbicacion(Long id) {
        rUb.deleteById(id);
    }


    public List<MembresiaAdminDTO> obtenerMembresiasAdmin() {
        return rM.findAll().stream()
                .map(this::toMembresiaAdminDTO)
                .sorted(Comparator.comparing(MembresiaAdminDTO::getId))
                .toList();
    }

    public List<PagoAdminDTO> obtenerPagosAdmin() {
        return rP.findAll().stream()
                .map(this::toPagoAdminDTO)
                .sorted(Comparator.comparing(PagoAdminDTO::getId))
                .toList();
    }


    private double calcTasaOcupacion(Clase c) {
        int cap = c.getCapacidadMaxima() != null ? c.getCapacidadMaxima() : 0;
        int ins = (c.getUsuariosInscritos() != null) ? c.getUsuariosInscritos().size() : 0;
        if (cap <= 0) return 0.0;
        return (ins * 100.0) / cap;
    }

    private UsuarioAdminDTO toUsuarioAdminDTO(Usuario u) {

        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setId(u.getId());
        dto.setNombreCompleto(u.getNombreCompleto());
        dto.setCorreoElectronico(u.getCorreoElectronico());
        dto.setNumeroTelefono(u.getNumeroTelefono());
        dto.setRol(u.getRol() != null ? u.getRol().name() : "USUARIO");
        dto.setEstaActivo(u.isEstaActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        return dto;
    }

    private MembresiaAdminDTO toMembresiaAdminDTO(Membresia m) {
        Long usuarioId      = (m.getUsuario() != null) ? m.getUsuario().getId() : null;
        String usuarioNombre= (m.getUsuario() != null) ? m.getUsuario().getNombreCompleto() : null;

        MembresiaAdminDTO dto = new MembresiaAdminDTO();
        dto.setId(m.getId());
        dto.setUsuarioId(usuarioId);
        dto.setUsuarioNombre(usuarioNombre);
        dto.setTipoPlan(m.getTipoPlan() != null ? m.getTipoPlan().name() : null);
        dto.setPrecio(m.getPrecio());
        dto.setFechaInicio(m.getFechaInicio());
        dto.setFechaFin(m.getFechaFin());
        dto.setEstado(m.getEstado() != null ? m.getEstado().name() : null);
        dto.setRenovacionAutomatica(m.isRenovacionAutomatica());
        return dto;
    }

    private PagoAdminDTO toPagoAdminDTO(Pago p) {
        Long usuarioId       = (p.getUsuario() != null) ? p.getUsuario().getId() : null;
        String usuarioNombre = (p.getUsuario() != null) ? p.getUsuario().getNombreCompleto() : null;
        Long membresiaId     = (p.getMembresia() != null) ? p.getMembresia().getId() : null;

        PagoAdminDTO dto = new PagoAdminDTO();
        dto.setId(p.getId());
        dto.setUsuarioId(usuarioId);
        dto.setUsuarioNombre(usuarioNombre);
        dto.setMembresiaId(membresiaId);
        dto.setMonto(p.getMonto());
        dto.setFechaPago(p.getFechaPago());
        dto.setIdTransaccion(p.getIdTransaccion());
        dto.setMetodoPago(p.getMetodoPago() != null ? p.getMetodoPago().name() : null);
        dto.setEstadoPago(p.getEstadoPago() != null ? p.getEstadoPago().name() : null);
        return dto;
    }

    @Transactional
    public UsuarioAdminDTO crearUsuarioAdmin(CrearUsuarioAdminRequest r){
        rU.findByCorreoElectronico(r.getCorreoElectronico())
                .ifPresent(u -> { throw new IllegalArgumentException("El correo ya está registrado"); });

        var u = new Usuario();
        u.setNombreCompleto(r.getNombreCompleto());
        u.setCorreoElectronico(r.getCorreoElectronico());
        u.setNumeroTelefono(r.getNumeroTelefono());
        u.setRol(Usuario.Rol.valueOf(r.getRol()));
        u.setEstaActivo(r.getEstaActivo() == null ? true : r.getEstaActivo());

        String raw = (r.getContrasena() == null || r.getContrasena().isBlank()) ? "123456" : r.getContrasena();
        u.setContrasena(codificadorContrasena.encode(raw));

        var guardado = rU.save(u);

        return toUsuarioAdminDTO(guardado);
    }
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerSuscripcionesMensuales() {
        var todas = rM.findAll();
        var formatoMes = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, Long> porMes = todas.stream()
                .filter(m -> m.getFechaInicio() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getFechaInicio().format(formatoMes),
                        Collectors.counting()
                ));

        return porMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

}
