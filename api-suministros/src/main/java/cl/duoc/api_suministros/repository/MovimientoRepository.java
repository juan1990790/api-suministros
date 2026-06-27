package cl.duoc.api_suministros.repository; // Nota: Usar minúsculas para paquetes

import cl.duoc.api_suministros.model.MovimientoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoModel, Long> {
}