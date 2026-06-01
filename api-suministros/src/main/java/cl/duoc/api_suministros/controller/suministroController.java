package cl.duoc.api_suministros.Controller;

import cl.duoc.api_suministros.Service.suministroService;
import cl.duoc.api_suministros.model.SuministroModel;
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

    // READ ALL
    @GetMapping
    public ResponseEntity<List<SuministroModel>> obtenerTodos() {
        List<SuministroModel> suministros = service.getAllSuministros();
        return new ResponseEntity<>(suministros, HttpStatus.OK);
    }

    // READ BY ID FABRICANTE (Regla: Activa consulta remota si stock < 3)
    @GetMapping("/{idFabricante}")
    public ResponseEntity<SuministroModel> obtenerPorFabricante(@PathVariable("idFabricante") String idFab) {
        return service.getSuministroByIdFabricante(idFab)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping
    public ResponseEntity<SuministroModel> crearSuministro(@Valid @RequestBody SuministroModel suministro) {
        SuministroModel nuevo = service.createSuministro(suministro);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // UPDATE (PUT) - Para actualizaciones de técnicos
    @PutMapping("/{id}")
    public ResponseEntity<SuministroModel> actualizarSuministro(@PathVariable("id") Long id, @RequestBody SuministroModel detalles) {
        SuministroModel actualizado = service.updateSuministro(id, detalles);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE BY ID (Regla ISO: Bloqueo si está AGOTADO)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarSuministro(@PathVariable("id") Long id) {
        String resultado = service.deleteSuministro(id);

        if (resultado.equals("Eliminado correctamente.")) {
            return ResponseEntity.noContent().build(); // 204
        }

        if (resultado.startsWith("ERROR:")) {
            // Devolvemos 409 Conflict o 422 Unprocessable Entity con el mensaje semántico de la ISO
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
        }

        return ResponseEntity.notFound().build(); // 404
    }
}
