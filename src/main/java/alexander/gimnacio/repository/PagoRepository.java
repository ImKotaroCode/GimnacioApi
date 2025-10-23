package alexander.gimnacio.repository;
import alexander.gimnacio.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long usuarioId);
    List<Pago> findByEstadoPago(Pago.EstadoPago estadoPago);
}
