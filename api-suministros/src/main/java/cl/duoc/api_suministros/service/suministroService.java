package cl.duoc.api_suministros.Service;

import cl.duoc.api_suministros.config.AuditoriaException; 
import cl.duoc.api_suministros.model.*;
import cl.duoc.api_suministros.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class suministroService {

    @Autowired
    private suministroRepository repository;

    @Autowired
    private MovimientoRepository logRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<suministroModel> getAllSuministros() {
        return repository.findAll();
    }

    public Optional<suministroModel> getSuministroByIdFabricante(String idFabricante) {
        Optional<suministroModel> componente = repository.findByIdFabricante(idFabricante);
        if (componente.isPresent()) {
            suministroModel model = componente.get();
            if (model.getUnidades() < 3) {
                try {
                    String url = "http://localhost:21300/api/v1/ordenes/" + idFabricante;
                    String infoRemota = restTemplate.getForObject(url, String.class);
                    model.setInfoProveedorRemoto(infoRemota);
                } catch (Exception e) {
                    model.setInfoProveedorRemoto("Sin conexion con central de proveedores");
                }
            }
            return Optional.of(model);
        }
        return Optional.empty();
    }

    public suministroModel createSuministro(suministroModel suministro) {
        suministroModel guardado = repository.save(suministro);
        
        registrarMovimiento(guardado.getSku(), "CREACION", guardado.getUnidades());
        
        return guardado;
    }

    public suministroModel updateSuministro(Long id, suministroModel detalles) {
        Optional<suministroModel> existente = repository.findById(id);
        if (existente.isPresent()) {
            suministroModel suministro = existente.get();
            suministro.setUnidades(detalles.getUnidades());
            suministro.setValorComercial(detalles.getValorComercial());
            suministro.setEstado(detalles.getUnidades() == 0 ? "AGOTADO" : detalles.getEstado());

            suministroModel actualizado = repository.save(suministro);
            
            registrarMovimiento(actualizado.getSku(), "ACTUALIZACION", actualizado.getUnidades());
            
            return actualizado;
        }
        return null;
    }

    public void deleteSuministro(Long id) {
        Optional<suministroModel> suministro = repository.findById(id);
        if (suministro.isPresent()) {
            
            if ("AGOTADO".equalsIgnoreCase(suministro.get().getEstado().trim())) {
                throw new AuditoriaException("ERROR: El registro contable está congelado para revisión (Estado AGOTADO).");
            }
            repository.deleteById(id);
        } else {
            throw new AuditoriaException("Componente no encontrado");
        }
    }

    private void registrarMovimiento(String sku, String accion, Integer cantidad) {
        MovimientoModel log = new MovimientoModel();
        log.setSkuComponente(sku);
        log.setAccion(accion);
        log.setFechaMovimiento(LocalDateTime.now());
        log.setCantidad(cantidad);
        logRepository.save(log);
    }
}
