package cl.duoc.api_suministros;


import cl.duoc.api_suministros.config.AuditoriaException;
import cl.duoc.api_suministros.model.MovimientoModel;
import cl.duoc.api_suministros.service.suministroService;
import cl.duoc.api_suministros.model.suministroModel;
import cl.duoc.api_suministros.repository.suministroRepository;
import cl.duoc.api_suministros.repository.MovimientoRepository;


import cl.duoc.api_suministros.service.suministroService;
import cl.duoc.api_suministros.model.suministroModel;
import cl.duoc.api_suministros.repository.suministroRepository;
import cl.duoc.api_suministros.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuministroServiceTest {

    @Mock
    private suministroRepository repository;

    @Mock
    private MovimientoRepository logRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private suministroService service;

    private suministroModel suministroPrueba;

    @BeforeEach
    void setUp() {
        suministroPrueba = new suministroModel();
        suministroPrueba.setId(1L);
        suministroPrueba.setIdFabricante("FAB-123");
        suministroPrueba.setNombre("Componente Test");
        suministroPrueba.setSku("SKU-TEST");
        suministroPrueba.setValorComercial(1000.0);
        suministroPrueba.setEstado("EN_STOCK");
        suministroPrueba.setBodegaId(1L);
    }

    @Test
    void getSuministroByIdFabricante_StockBajo_LlamaApiExterna() {
        // Arrange: Preparamos el escenario con 2 unidades (stock bajo)
        suministroPrueba.setUnidades(2);
        when(repository.findByIdFabricante("FAB-123")).thenReturn(Optional.of(suministroPrueba));

        String respuestaMock = "Cargamento en transito: 50 unidades";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(respuestaMock);

        // Act: Ejecutamos
        Optional<suministroModel> resultado = service.getSuministroByIdFabricante("FAB-123");

        // Assert: Verificamos los resultados
        assertTrue(resultado.isPresent());
        assertEquals(2, resultado.get().getUnidades());
        assertEquals(respuestaMock, resultado.get().getInfoProveedorRemoto());

        // Verificamos que efectivamente se llamó al RestTemplate una vez
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void getSuministroByIdFabricante_StockAlto_NoLlamaApiExterna() {
        // Arrange: Preparamos el escenario con 10 unidades (stock normal)
        suministroPrueba.setUnidades(10);
        when(repository.findByIdFabricante("FAB-123")).thenReturn(Optional.of(suministroPrueba));

        // Act
        Optional<suministroModel> resultado = service.getSuministroByIdFabricante("FAB-123");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(10, resultado.get().getUnidades());
        assertNull(resultado.get().getInfoProveedorRemoto());

        // Verificamos que NO se llamó al RestTemplate
        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void createSuministro_Exitoso() {
        // Arrange
        suministroPrueba.setUnidades(10);
        when(repository.save(any(suministroModel.class))).thenReturn(suministroPrueba);
        when(logRepository.save(any(MovimientoModel.class))).thenReturn(new MovimientoModel());

        // Act
        suministroModel resultado = service.createSuministro(suministroPrueba);

        // Assert
        assertNotNull(resultado);
        assertEquals("SKU-TEST", resultado.getSku());
        assertEquals(10, resultado.getUnidades());

        // Verificaciones de comportamiento de los repositorios
        verify(repository, times(1)).save(any(suministroModel.class));
        verify(logRepository, times(1)).save(any(MovimientoModel.class));
    }


    @Test
    void updateSuministro_Exitoso() {
        // Arrange
        suministroModel detallesNuevos = new suministroModel();
        detallesNuevos.setUnidades(5);
        detallesNuevos.setValorComercial(1200.0);
        detallesNuevos.setEstado("EN_STOCK");

        // Simulamos la secuencia completa: buscar -> guardar -> auditar
        when(repository.findById(1L)).thenReturn(Optional.of(suministroPrueba));
        when(repository.save(any(suministroModel.class))).thenReturn(suministroPrueba);
        when(logRepository.save(any(MovimientoModel.class))).thenReturn(new MovimientoModel());

        // Act
        suministroModel resultado = service.updateSuministro(1L, detallesNuevos);

        // Assert
        assertNotNull(resultado);
        assertEquals(5, resultado.getUnidades());
        assertEquals(1200.0, resultado.getValorComercial());
        
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(suministroModel.class));
        verify(logRepository, times(1)).save(any(MovimientoModel.class));
    }

    //siguiente
}
