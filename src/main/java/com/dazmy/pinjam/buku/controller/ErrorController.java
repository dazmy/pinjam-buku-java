package com.dazmy.pinjam.buku.controller;

import com.dazmy.pinjam.buku.model.response.CoreResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CoreResponse<String>> constraint(ConstraintViolationException exception) {
        return ResponseEntity.status(400).body(CoreResponse.<String>builder().status(400).errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CoreResponse<String>> response(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(CoreResponse.<String>builder().status(exception.getStatusCode().value()).errors(exception.getReason()).build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CoreResponse<String>> authenticationException(AuthenticationException exception) {
        return ResponseEntity.status(401).body(CoreResponse.<String>builder().status(401).errors(exception.getMessage()).build());
    }

}
