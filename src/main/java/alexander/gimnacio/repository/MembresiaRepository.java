package alexander.gimnacio.repository;
import alexander.gimnacio.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long> {
    Optional<Membresia> findByUsuarioId(Long usuarioId);
    List<Membresia> findByEstado(Membresia.Estado estado);
}
