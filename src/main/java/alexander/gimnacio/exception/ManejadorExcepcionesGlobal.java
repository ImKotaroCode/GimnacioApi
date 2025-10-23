package alexander.gimnacio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorExcepcionesGlobal {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RespuestaError> manejarExcepcionRuntime(RuntimeException ex) {
        RespuestaError error = RespuestaError.builder()
                .marcaTiempo(LocalDateTime.now())
                .estado(HttpStatus.BAD_REQUEST.value())
                .error("Error")
                .mensaje(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionesValidacion(
            MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });
        return ResponseEntity.badRequest().body(errores);
    }
}



