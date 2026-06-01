package cl.duoc.api_suministros.controller;

import cl.duoc.api_suministros.model.suministroModel;
import cl.duoc.api_suministros.service.suministroService;
import cl.duoc.api_suministros.model.suministroModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/suministros")
public class suministroController {

    @Autowired
    suministroService service;

    @GetMapping
    public ResponseEntity<List<suministroModel>> obtenerTodos() {
        List<suministroModel> suministros = service.getAllSuministros();
        return new ResponseEntity<>(suministros, HttpStatus.OK);
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
    public ResponseEntity<List<suministroModel>> actualizarSuministro(@PathVariable("id") Long id, @RequestBody suministroModel detalles) {
        suministroModel actualizado = service.updateSuministro(id, detalles);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarSuministro(@PathVariable("id") Long id) {
        String resultado = service.deleteSuministro(id);

        if (resultado.equals("Eliminado correctamente.")) {
            return ResponseEntity.noContent().build(); // 204
        }

        if (resultado.startsWith("ERROR:")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
        }

        return ResponseEntity.notFound().build(); // 404
    }
}
