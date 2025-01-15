package com.mx.bbase.orderpayment.exceptions;

import com.mx.bbase.orderpayment.model.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e, HttpServletRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO();
        apiErrorDTO.setBackendMessage(e.getLocalizedMessage());
        apiErrorDTO.setUrl(request.getRequestURL().toString());
        apiErrorDTO.setMethod(request.getMethod());
        apiErrorDTO.setMessage("Error interno en el servidor, vuelva a intentarlo.");
        apiErrorDTO.setTimestamp(LocalDateTime.now());

        log.error("Error generico: ", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorDTO);
    }

    @ExceptionHandler(InvalidObjectException.class)
    public ResponseEntity<?> handlerException(InvalidObjectException e, HttpServletRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO();
        apiErrorDTO.setBackendMessage(e.getLocalizedMessage());
        apiErrorDTO.setUrl(request.getRequestURL().toString());
        apiErrorDTO.setMethod(request.getMethod());
        apiErrorDTO.setTimestamp(LocalDateTime.now());
        apiErrorDTO.setMessage("Error en la peticion enviada.");
        apiErrorDTO.setTimestamp(LocalDateTime.now());

        log.error("Error controlado de invalidos ", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorDTO);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handlerException(ObjectNotFoundException e, HttpServletRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO();
        apiErrorDTO.setBackendMessage(e.getLocalizedMessage());
        apiErrorDTO.setUrl(request.getRequestURL().toString());
        apiErrorDTO.setMethod(request.getMethod());
        apiErrorDTO.setTimestamp(LocalDateTime.now());
        apiErrorDTO.setMessage("Valor de campo no reconocido.");
        apiErrorDTO.setTimestamp(LocalDateTime.now());

        log.error("Error controlado de no encontrados ", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorDTO);
    }

}
