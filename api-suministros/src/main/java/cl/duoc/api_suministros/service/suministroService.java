package cl.duoc.api_suministros.Service;

import cl.duoc.api_suministros.Repository.suministroRepository;
import cl.duoc.api_suministros.model.SuministroModel;
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
    RestTemplate restTemplate; // Para conectar con la API 2 (Central de Proveedores)

    // READ ALL
    public List<SuministroModel> getAllSuministros() {
        return repository.findAll();
    }

    // READ BY ID FABRICANTE (Aquí aplicamos la lógica de Stock < 3)
    public Optional<SuministroModel> getSuministroByIdFabricante(String idFabricante) {
        Optional<SuministroModel> componente = repository.findByIdFabricante(idFabricante);

        if (componente.isPresent()) {
            SuministroModel model = componente.get();

            // REGLA DE NEGOCIO: Si stock < 3, consultar a la API 2
            if (model.getUnidades() < 3) {
                try {
                    // Llamamos a la API 2 (Central de Compras) puerto 8082
                    String url = "http://localhost:8082/api/proveedores/verificar/" + idFabricante;
                    String infoRemota = restTemplate.getForObject(url, String.class);

                    // Adjuntamos la info dinámicamente al SKU o un campo transitorio
                    model.setSku("INFO PROVEEDOR: " + infoRemota);
                } catch (Exception e) {
                    model.setSku("INFO PROVEEDOR: Sin conexión con central.");
                }
            }
            return Optional.of(model);
        }
        return Optional.empty();
    }

    // CREATE
    public SuministroModel createSuministro(SuministroModel suministro) {
        // La validación de no negativos ya la hace el Model con @Min(0)
        return repository.save(suministro);
    }

    // UPDATE (PUT) - Actualización de técnicos
    public SuministroModel updateSuministro(Long id, SuministroModel detalles) {
        Optional<SuministroModel> existente = repository.findById(id);

        if (existente.isPresent()) {
            SuministroModel suministro = existente.get();
            suministro.setUnidades(detalles.getUnidades());
            suministro.setValorComercial(detalles.getValorComercial());

            // Lógica de integridad: Si llega a 0, cambiar estado a AGOTADO
            if (detalles.getUnidades() == 0) {
                suministro.setEstado("AGOTADO");
            } else {
                suministro.setEstado(detalles.getEstado());
            }

            return repository.save(suministro);
        }
        return null;
    }

    // DELETE BY ID (REGLA DE NEGOCIO ISO)
    public String deleteSuministro(Long id) {
        Optional<SuministroModel> suministro = repository.findById(id);

        if (suministro.isPresent()) {
            // REGLA: Si está AGOTADO, el registro está congelado
            if ("AGOTADO".equalsIgnoreCase(suministro.get().getEstado())) {
                return "ERROR: El registro contable está congelado para la revisión de fin de año (Normativa ISO).";
            }

            repository.deleteById(id);
            return "Eliminado correctamente.";
        }
        return "Componente no encontrado.";
    }
}
