package com.arpitasaha.offers.exception;

import com.arpitasaha.offers.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(OfferService.NotFoundException.class)
    ResponseEntity<?> notFound(RuntimeException ex) { return response(HttpStatus.NOT_FOUND, ex.getMessage()); }
    @ExceptionHandler(OfferService.BadRequestException.class)
    ResponseEntity<?> badRequest(RuntimeException ex) { return response(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining("; "));
        return response(HttpStatus.BAD_REQUEST, message);
    }
    private ResponseEntity<Map<String,Object>> response(HttpStatus status, String message) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now()); body.put("status", status.value());
        body.put("error", status.getReasonPhrase()); body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
