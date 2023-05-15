package com.ficr.edu.bancoapi.exceptions;

import com.ficr.edu.bancoapi.exceptions.errors.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.NonUniqueResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String REQUISICAO_INVALIDA = "Requisição inválida";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String CONFLICT = "Conflict";
    private static final String NOT_FOUND = "Not Found";
    private static final String FORBIDDEN = "Forbidden";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> catchResourceNotFound(ResourceNotFoundException e,
                                                               HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError(NOT_FOUND);
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> catchConflict(ConflictException e, HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setError(CONFLICT);
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> catchBusinessRule(BusinessRuleException e, HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError(BAD_REQUEST);
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> catchIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError(BAD_REQUEST);
        err.setMessage(REQUISICAO_INVALIDA);
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> catchOthers(RuntimeException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError(BAD_REQUEST);
        err.setMessage(REQUISICAO_INVALIDA);
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<StandardError> catchNonUniqueResult(NonUniqueResultException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError(BAD_REQUEST);
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

}