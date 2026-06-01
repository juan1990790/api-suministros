package cl.duoc.api_suministros.repository;

import cl.duoc.api_suministros.model.suministroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface suministroRepository extends JpaRepository<suministroModel, Long> {

    Optional<suministroModel> findByIdFabricante(String idFabricante);
}