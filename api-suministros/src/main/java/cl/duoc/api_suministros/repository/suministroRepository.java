package cl.duoc.api_suministros.repository;

import cl.duoc.api_suministros.model.suministroModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface suministroRepository extends JpaRepository<suministroModel, Long> {

    // Buscar por ID de fabricante para la validación externa y stock
    @Transactional
    Optional<suministroModel> findByIdFabricante(String idFabricante);

    // Eliminar por ID de fabricante (opcional, por si no quieres usar el ID numérico)
    @Transactional
    void deleteByIdFabricante(String idFabricante);
}
