package cl.duoc.api_suministros.service;

import cl.duoc.api_suministros.model.suministroModel;
import cl.duoc.api_suministros.repository.suministroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class suministroService {

    @Autowired
    suministroRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    public List<suministroModel> getAllSuministros() {
        return repository.findAll();
    }

    public Optional<suministroModel> getSuministroByIdFabricante(String idFabricante) {
        Optional<suministroModel> componente = repository.findByIdFabricante(idFabricante);

        if (componente.isPresent()) {
            suministroModel model = componente.get();

            // REGLA DE NEGOCIO: Consulta remota si stock < 3
            if (model.getUnidades() < 3) {
                try {
                    // Llamamos a la API 2 (Central de Proveedores)
                    String url = "http://localhost:8081/api/v1/ordenes/" + idFabricante;
                    String infoRemota = restTemplate.getForObject(url, String.class);

                    // Asignamos la info al campo @Transient
                    model.setInfoProveedorRemoto(infoRemota);
                } catch (Exception e) {
                    model.setInfoProveedorRemoto("Sin conexión con central de proveedores.");
                }
            }
            return Optional.of(model);
        }
        return Optional.empty();
    }

    public suministroModel createSuministro(suministroModel suministro) {
        return repository.save(suministro);
    }

    public suministroModel updateSuministro(Long id, suministroModel detalles) {
        Optional<suministroModel> existente = repository.findById(id);

        if (existente.isPresent()) {
            suministroModel suministro = existente.get();
            suministro.setUnidades(detalles.getUnidades());
            suministro.setValorComercial(detalles.getValorComercial());

            // Control de ciclo de vida automático
            if (detalles.getUnidades() == 0) {
                suministro.setEstado("AGOTADO");
            } else {
                suministro.setEstado(detalles.getEstado());
            }

            return repository.save(suministro);
        }
        return null;
    }

    public String deleteSuministro(Long id) {
        Optional<suministroModel> suministro = repository.findById(id);

        if (suministro.isPresent()) {
            // REGLA DE NEGOCIO: Restricción ISO
            if ("AGOTADO".equalsIgnoreCase(suministro.get().getEstado())) {
                return "ERROR: El registro contable está congelado para la revisión de fin de año (Normativa ISO).";
            }

            repository.deleteById(id);
            return "Eliminado correctamente.";
        }
        return "Componente no encontrado.";
    }
}