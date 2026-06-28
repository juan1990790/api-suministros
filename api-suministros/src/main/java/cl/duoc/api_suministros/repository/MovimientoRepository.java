package cl.duoc.api_suministros.repository;

import cl.duoc.api_suministros.model.MovimientoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoModel, Long> {
}