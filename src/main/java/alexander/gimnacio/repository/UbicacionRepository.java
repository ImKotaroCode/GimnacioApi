package alexander.gimnacio.repository;
import alexander.gimnacio.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByEstaActivoTrue();
    List<Ubicacion> findByDistrito(String distrito);
}
