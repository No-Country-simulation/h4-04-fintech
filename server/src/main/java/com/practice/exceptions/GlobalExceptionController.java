package com.practice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_ERROR",
                "Errores de validación en los campos enviados",
                details
        );

        log.warn("Validation error: {}", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_REQUEST_BODY",
                "El formato del cuerpo de la solicitud es inválido",
                Collections.singletonList("Verifica la sintaxis JSON y los tipos de datos enviados")
        );

        log.warn("Invalid request body: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_QUERY_PARAMETER",
                "Error en los parámetros de consulta",
                Collections.singletonList(ex.getMostSpecificCause().getMessage())
        );

        log.warn("Invalid data access: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                "CONSTRAINT_VIOLATION",
                "Violación de restricciones en los datos",
                details
        );

        log.warn("Constraint violation: {}", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        List<String> supportedMethods = ex.getSupportedHttpMethods() != null ?
                ex.getSupportedHttpMethods().stream().map(Object::toString).collect(Collectors.toList()) :
                Collections.emptyList();

        ErrorResponse errorResponse = new ErrorResponse(
                "METHOD_NOT_ALLOWED",
                "Método HTTP no soportado: " + ex.getMethod(),
                Collections.singletonList("Métodos soportados: " + String.join(", ", supportedMethods))
        );

        log.warn("Method not allowed: {} for path {}", ex.getMethod(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                "El recurso solicitado no existe",
                Collections.singletonList("Path: " + ex.getRequestURL())
        );

        log.warn("Resource not found: {}", ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "ACCESS_DENIED",
                "Acceso denegado",
                Collections.singletonList("No tienes los permisos necesarios para realizar esta acción")
        );

        log.warn("Access denied for user");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(InsufficientInstrumentsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientInstrumentsException(InsufficientInstrumentsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INSUFFICIENT_INSTRUMENTS",
                "Ha ocurrido un error con el portafolio",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("Insufficient instruments: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileException(InvalidFileException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_FILE",
                "Ha ocurrido un error con el archivo",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("Invalid file: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePortfolioNotFoundException(PortfolioNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "PORTFOLIO_ERROR",
                "Ha ocurrido un error con el portafolio",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("Portfolio not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PortfolioHasTransactionsException.class)
    public ResponseEntity<ErrorResponse> handlePortfolioHasTransactionsException(PortfolioHasTransactionsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "PORTFOLIO_ERROR",
                "El portafolio tiene transacciones",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("Portfolio has transactions: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "TRANSACTION_ERROR",
                "Ha ocurrido un error con la transacción",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("Transaction not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FinancingProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFinancingProfileNotFoundException(FinancingProfileNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "FINANCING_PROFILE_ERROR",
                "Ha ocurrido un error con el perfil financiero",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("Financing profile not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "USER_ERROR",
                "Ha ocurrido un error con el usuario",
                Collections.singletonList(ex.getMessage())
        );

        log.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String exceptionType = ex.getClass().getSimpleName();

        ErrorResponse errorResponse = new ErrorResponse(
                "SERVER_ERROR",
                "Ha ocurrido un error en el servidor",
                List.of("Error: " + ex.getMessage(), "Ruta: " + path, "Tipo: " + exceptionType)
        );

        log.error("🚨 Error en la ruta [{}]: {} - {}", path, exceptionType, ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }




}
