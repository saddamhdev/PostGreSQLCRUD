package com.PostGreSQL.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- Your custom exceptions ----
    @ExceptionHandler(BadRequestException2.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(BadRequestException2 ex, HttpServletRequest req) {
        System.out.println("checking");
        return problem(HttpStatus.BAD_REQUEST, ex.getMessage(), req, null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return problem(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflict(ConflictException ex, HttpServletRequest req) {
        return problem(HttpStatus.CONFLICT, ex.getMessage(), req, null);
    }

    // ---- Validation & binding ----
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleBodyValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList();
        return problem(HttpStatus.BAD_REQUEST, "Validation failed", req, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleParamValidation(ConstraintViolationException ex, HttpServletRequest req) {
        var errors = ex.getConstraintViolations().stream()
                .map(cv -> Map.of("field", cv.getPropertyPath().toString(), "message", cv.getMessage()))
                .toList();
        return problem(HttpStatus.BAD_REQUEST, "Validation failed", req, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Malformed JSON request body", req, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid parameter value", req,
                List.of(Map.of("parameter", ex.getName(), "expectedType", Objects.toString(ex.getRequiredType(), "unknown"))));
    }

    // ---- Mongo / DB specifics ----
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateKeyException ex, HttpServletRequest req) {
        // Adjust field according to your unique index (studentId/email/etc.)
        return problem(HttpStatus.CONFLICT, "Duplicate key", req,
                List.of(Map.of("field", "studentId", "message", "already exists")));
    }

    // ---- 404 for missing handler/static resource (optional) ----
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResource(NoResourceFoundException ex, HttpServletRequest req) {
        return problem(HttpStatus.NOT_FOUND, "Endpoint not found", req, null);
    }

    // ---- Fallback (last resort) ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleOthers(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, null);
    }

    // ---- Helper to build RFC 7807 ProblemDetail ----
    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String message,
                                                  HttpServletRequest req, List<?> errors) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", req.getRequestURI());
        if (errors != null && !errors.isEmpty())
            pd.setProperty("errors", errors);
        System.out.println(pd.toString());
        System.out.println(ResponseEntity.status(status).body(pd));
        return ResponseEntity.status(status).body(pd);
    }
}
