package com.biblioteca_api.biblioteca.infra.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BookAlreadyExistsException.class)
    public ProblemDetail handleDuplicateBook(BookAlreadyExistsException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, // 409
                e.getMessage()
        );

        problemDetail.setTitle("Conflito de Dados");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleDuplicateBook(UserAlreadyExistsException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );

        problemDetail.setTitle("Conflito de Dados");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;

    }
}
