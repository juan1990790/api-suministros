package cl.duoc.api_suministros.Controller;

import cl.duoc.api_suministros.model.suministroModel;
import cl.duoc.api_suministros.service.suministroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuministroControllerTest {

    @Mock
    private suministroService service;

    @InjectMocks
    private suministroController controller;

    private suministroModel suministroPrueba;

    @BeforeEach
    void setup() {
        suministroPrueba = new suministroModel();
        suministroPrueba.setId(1L);
        suministroPrueba.setIdFabricante("FAB-123");
        suministroPrueba.setNombre("Componente Test");
        suministroPrueba.setEstado("EN_STOCK");
        suministroPrueba.setValorComercial(1500.0);
        suministroPrueba.setUnidades(10);
    }

    // Test para GET (Todos)
    @Test
    void obtenerTodos_responde200() {
        when(service.getAllSuministros()).thenReturn(List.of(suministroPrueba));

        ResponseEntity<List<suministroModel>> resp = controller.obtenerTodos();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        verify(service, times(1)).getAllSuministros();
    }

    // Test para GET /Por Fabricante - Existe
    @Test
    void obtenerPorFabricante_existe_responde200() {
        when(service.getSuministroByIdFabricante("FAB-123")).thenReturn(Optional.of(suministroPrueba));

        ResponseEntity<suministroModel> resp = controller.obtenerPorFabricante("FAB-123");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Componente Test", resp.getBody().getNombre());
    }

    // Test para GET / Por Fabricante - No Existe
    @Test
    void obtenerPorFabricante_noExiste_responde404() {
        when(service.getSuministroByIdFabricante("NADA")).thenReturn(Optional.empty());

        ResponseEntity<suministroModel> resp = controller.obtenerPorFabricante("NADA");

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    // Test para POST (Crear)
    @Test
    void crearSuministro_responde201() {
        when(service.createSuministro(any(suministroModel.class))).thenReturn(suministroPrueba);

        ResponseEntity<suministroModel> resp = controller.crearSuministro(suministroPrueba);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("FAB-123", resp.getBody().getIdFabricante());
        verify(service, times(1)).createSuministro(any(suministroModel.class));
    }

    // Test para PUT (Actualizar - Existe)
    @Test
    void actualizarSuministro_existe_responde200() {
        when(service.updateSuministro(eq(1L), any(suministroModel.class))).thenReturn(suministroPrueba);

        ResponseEntity<suministroModel> resp = controller.actualizarSuministro(1L, suministroPrueba);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1500.0, resp.getBody().getValorComercial());
    }

    // Test para DELETE (Eliminar)
    @Test
    void eliminarSuministro_responde204() {
        doNothing().when(service).deleteSuministro(1L);

        ResponseEntity<Void> resp = controller.eliminarSuministro(1L);

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        verify(service, times(1)).deleteSuministro(1L);
    }
}
