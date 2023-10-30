package com.example.sportstore06.exception;

import com.example.sportstore06.dao.response.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> list = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            list.add(new FieldErrorResponse(error.getField().toString(), error.getDefaultMessage()));
        });
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }
}
