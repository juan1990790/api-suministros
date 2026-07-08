package cl.duoc.api_suministros.Controller;

import cl.duoc.api_suministros.model.suministroModel;
import cl.duoc.api_suministros.service.suministroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suministros")
public class suministroController {

    @Autowired
    private suministroService service;

    @GetMapping
    public ResponseEntity<List<suministroModel>> obtenerTodos() {
        return new ResponseEntity<>(service.getAllSuministros(), HttpStatus.OK);
    }

    @GetMapping("/{idFabricante}")
    public ResponseEntity<suministroModel> obtenerPorFabricante(@PathVariable("idFabricante") String idFab) {
        return service.getSuministroByIdFabricante(idFab)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<suministroModel> crearSuministro(@Valid @RequestBody suministroModel suministro) {
        suministroModel nuevo = service.createSuministro(suministro);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<suministroModel> actualizarSuministro(@PathVariable("id") Long id, @Valid @RequestBody suministroModel detalles) {
        suministroModel actualizado = service.updateSuministro(id, detalles);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSuministro(@PathVariable("id") Long id) {
        service.deleteSuministro(id);
        return ResponseEntity.noContent().build();
    }
}
