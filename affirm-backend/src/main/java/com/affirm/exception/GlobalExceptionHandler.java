package com.affirm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "参数校验失败");
        pd.setType(URI.create("https://api.example.com/errors/validation"));
        pd.setTitle("Validation error");
        return pd;
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ProblemDetail handleConflict(ResourceConflictException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setType(URI.create("https://api.example.com/errors/conflict"));
        pd.setTitle("Resource conflict");
        return pd;
    }
}


