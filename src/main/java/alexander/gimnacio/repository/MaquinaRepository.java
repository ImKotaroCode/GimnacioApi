package alexander.gimnacio.repository;

import alexander.gimnacio.entities.Maquina;
import alexander.gimnacio.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    List<Maquina> findByUbicacion(Ubicacion ubicacion);
    List<Maquina> findByEstado(Maquina.Estado estado);
}

