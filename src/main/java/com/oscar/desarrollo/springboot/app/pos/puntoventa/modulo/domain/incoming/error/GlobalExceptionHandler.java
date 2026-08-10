package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.BussinessException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.ResourceNotFoundException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.model.CampoErrorResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.model.Error422Model;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.model.ErrorModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorModelResponse> manejarNotFounf(ResourceNotFoundException ex) {

        ErrorModelResponse response = ErrorModelResponse.builder()
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<ErrorModelResponse> manejarNotFounf(BussinessException ex) {

        ErrorModelResponse response = ErrorModelResponse.builder()
                .error("Error_ReglaNegocio")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error422Model> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        List<CampoErrorResponse> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new CampoErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        Error422Model response = Error422Model.builder()
                .error("VALIDATION_ERROR")
                .message("Existen errores de validación")
                .timestamp(LocalDateTime.now())
                .errores(errores)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
