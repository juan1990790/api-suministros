package cl.duoc.api_suministros.config;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> construirRespuesta(HttpStatus estado, String mensaje, WebRequest req) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("mensaje", mensaje);
        cuerpo.put("status", estado.value());
        return new ResponseEntity<>(cuerpo, estado);
    }

    @ExceptionHandler(AuditoriaException.class)
    public ResponseEntity<Object> manejarAuditoria(AuditoriaException ex, WebRequest req) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> manejarValidacion(MethodArgumentNotValidException ex, WebRequest req) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarGeneral(Exception ex, WebRequest req) {
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", req);
    }
}
