package cl.duoc.api_suministros.Model;

import cl.duoc.api_suministros.model.suministroModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuministroModelValidationTest {

    private static Validator validator;

   @BeforeAll
    static void initValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // Metodo auxiliar para no repetir código en cada test
    private suministroModel crearSuministroValido() {
        suministroModel s = new suministroModel();
        s.setIdFabricante("FAB-VALIDO-01");
        s.setNombre("Tarjeta de Video RTX");
        s.setUnidades(10);
        s.setValorComercial(250000.0);
        s.setEstado("EN_STOCK"); // Estado permitido por tu Regex
        s.setBodegaId(1L);
        return s;
    }

    @Test
    @DisplayName("Un suministro con todos los datos correctos pasa la validación")
    void suministroValido_ok() {
        suministroModel s = crearSuministroValido();
        
        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertTrue(violaciones.isEmpty(), "No debería generar violaciones");
    }

    @Test
    @DisplayName("Falla si idFabricante está en blanco (@NotBlank)")
    void idFabricanteEnBlanco_falla() {
        suministroModel s = crearSuministroValido();
        s.setIdFabricante(""); 

        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertFalse(violaciones.isEmpty(), "Debe detectar que el ID Fabricante está vacío");
    }

    @Test
    @DisplayName("Falla si el nombre está nulo (@NotBlank)")
    void nombreNulo_falla() {
        suministroModel s = crearSuministroValido();
        s.setNombre(null);

        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertFalse(violaciones.isEmpty(), "Debe detectar que el nombre es nulo");
    }

    @Test
    @DisplayName("Falla si las unidades son negativas (@Min)")
    void unidadesNegativas_falla() {
        suministroModel s = crearSuministroValido();
        s.setUnidades(-5); 

        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertFalse(violaciones.isEmpty(), "Debe detectar que las unidades son menores a 0");
    }

    @Test
    @DisplayName("Falla si el valor comercial es negativo (@Min)")
    void valorNegativo_falla() {
        suministroModel s = crearSuministroValido();
        s.setValorComercial(-1000.0); 

        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertFalse(violaciones.isEmpty(), "Debe detectar que el valor comercial es menor a 0");
    }

    @Test
    @DisplayName("Falla si el estado no respeta el patrón permitido (@Pattern)")
    void estadoInvalido_falla() {
        suministroModel s = crearSuministroValido();
        s.setEstado("VENDIDO"); // No está en la lista EN_STOCK|ASIGNADO|AGOTADO

        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertFalse(violaciones.isEmpty(), "Debe detectar que el estado no cumple el Pattern");
    }

    @Test
    @DisplayName("Falla si la bodegaId es nula (@NotNull)")
    void bodegaIdNula_falla() {
        suministroModel s = crearSuministroValido();
        s.setBodegaId(null); 

        Set<ConstraintViolation<suministroModel>> violaciones = validator.validate(s);
        assertFalse(violaciones.isEmpty(), "Debe detectar que la bodega ID no existe");
    }
}
