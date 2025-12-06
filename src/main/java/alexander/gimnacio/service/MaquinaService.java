package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.CrearMaquinaRequest;
import alexander.gimnacio.dto.response.MaquinaAdminDTO;
import alexander.gimnacio.entities.Maquina;
import alexander.gimnacio.entities.Ubicacion;
import alexander.gimnacio.repository.MaquinaRepository;
import alexander.gimnacio.repository.UbicacionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;
    private final UbicacionRepository ubicacionRepository;

    /* =============== LISTAR =============== */

    @Transactional(readOnly = true)
    public List<MaquinaAdminDTO> obtenerMaquinas() {
        return maquinaRepository.findAll().stream()
                .map(this::toMaquinaAdminDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MaquinaAdminDTO> obtenerMaquinasPorUbicacion(Long ubicacionId) {
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        return maquinaRepository.findByUbicacion(ubicacion).stream()
                .map(this::toMaquinaAdminDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MaquinaAdminDTO> obtenerMaquinasPorEstado(Maquina.Estado estado) {
        return maquinaRepository.findByEstado(estado).stream()
                .map(this::toMaquinaAdminDTO)
                .toList();
    }

    /* =============== CREAR =============== */

    @Transactional
    public MaquinaAdminDTO crearMaquina(CrearMaquinaRequest r) {
        Ubicacion ubicacion = ubicacionRepository.findById(r.getUbicacionId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        Maquina.Estado estadoInicial =
                (r.getEstado() != null) ? r.getEstado() : Maquina.Estado.OPERATIVA;

        Maquina maquina = Maquina.builder()
                .nombre(r.getNombre())
                .descripcion(r.getDescripcion())
                .categoria(r.getCategoria())
                .estado(estadoInicial)
                .fechaUltimoMantenimiento(LocalDate.now())
                .ubicacion(ubicacion)
                .build();

        Maquina guardada = maquinaRepository.save(maquina);
        return toMaquinaAdminDTO(guardada);
    }

    /* =============== ACTUALIZAR =============== */

    @Transactional
    public MaquinaAdminDTO actualizarMaquina(Long id, CrearMaquinaRequest r) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Máquina no encontrada"));

        if (r.getNombre() != null) {
            maquina.setNombre(r.getNombre());
        }
        if (r.getDescripcion() != null) {
            maquina.setDescripcion(r.getDescripcion());
        }
        if (r.getCategoria() != null) {
            maquina.setCategoria(r.getCategoria());
        }
        if (r.getEstado() != null) {
            maquina.setEstado(r.getEstado());
        }
        if (r.getUbicacionId() != null) {
            Ubicacion ubicacion = ubicacionRepository.findById(r.getUbicacionId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
            maquina.setUbicacion(ubicacion);
        }

        Maquina actualizada = maquinaRepository.save(maquina);
        return toMaquinaAdminDTO(actualizada);
    }

    /* =============== CAMBIAR ESTADO =============== */

    @Transactional
    public MaquinaAdminDTO cambiarEstadoMaquina(Long id, Maquina.Estado nuevoEstado) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Máquina no encontrada"));

        maquina.setEstado(nuevoEstado);

        if (nuevoEstado == Maquina.Estado.MANTENIMIENTO) {
            maquina.setFechaUltimoMantenimiento(LocalDate.now());
        }

        Maquina guardada = maquinaRepository.save(maquina);
        return toMaquinaAdminDTO(guardada);
    }

    /* =============== ELIMINAR =============== */

    @Transactional
    public void eliminarMaquina(Long id) {
        if (!maquinaRepository.existsById(id)) {
            throw new RuntimeException("Máquina no encontrada");
        }
        maquinaRepository.deleteById(id);
    }

    /* =============== MAPEADOR DTO =============== */

    private MaquinaAdminDTO toMaquinaAdminDTO(Maquina m) {
        MaquinaAdminDTO dto = new MaquinaAdminDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setDescripcion(m.getDescripcion());
        dto.setCategoria(m.getCategoria() != null ? m.getCategoria().name() : null);
        dto.setEstado(m.getEstado() != null ? m.getEstado().name() : null);
        dto.setFechaUltimoMantenimiento(String.valueOf(m.getFechaUltimoMantenimiento()));

        if (m.getUbicacion() != null) {
            dto.setUbicacionId(m.getUbicacion().getId());
            dto.setNombreUbicacion(m.getUbicacion().getNombreUbicacion());
            dto.setDistrito(m.getUbicacion().getDistrito());
            dto.setCiudad(m.getUbicacion().getCiudad());
        }

        return dto;
    }
}
