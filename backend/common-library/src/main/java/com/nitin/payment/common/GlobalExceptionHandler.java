/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.ConstraintViolationException
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.MethodArgumentNotValidException
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.RestControllerAdvice
 *  org.springframework.web.servlet.resource.NoResourceFoundException
 */
package com.nitin.payment.common;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.common.exception.DuplicateResourceException;
import com.nitin.payment.common.exception.FileProcessingException;
import com.nitin.payment.common.exception.InvalidRequestException;
import com.nitin.payment.common.exception.KafkaProcessingException;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.common.exception.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={ResourceNotFoundException.class})
    ResponseEntity<ApiResponse<Void>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(ApiResponse.error(404, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(value={NoResourceFoundException.class})
    ResponseEntity<ApiResponse<Void>> noResource(NoResourceFoundException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "Resource not found", List.of(ex.getMessage())));
    }

    @ExceptionHandler(value={DuplicateResourceException.class})
    ResponseEntity<ApiResponse<Void>> conflict(DuplicateResourceException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(ApiResponse.error(409, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(value={UnauthorizedException.class})
    ResponseEntity<ApiResponse<Void>> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(value={FileProcessingException.class})
    ResponseEntity<ApiResponse<Void>> fileProcessing(FileProcessingException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(400, ex.getMessage(), List.of(this.rootMessage(ex))));
    }

    @ExceptionHandler(value={KafkaProcessingException.class})
    ResponseEntity<ApiResponse<Void>> kafka(KafkaProcessingException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_GATEWAY).body(ApiResponse.error(502, ex.getMessage(), List.of(this.rootMessage(ex))));
    }

    @ExceptionHandler(value={InvalidRequestException.class, ConstraintViolationException.class})
    ResponseEntity<ApiResponse<Void>> badRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(400, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    ResponseEntity<ApiResponse<Void>> validation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(ApiResponse.error(400, "Validation failed", errors));
    }

    @ExceptionHandler(value={Exception.class})
    ResponseEntity<ApiResponse<Void>> generic(Exception ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Unexpected server error", List.of(ex.getMessage())));
    }

    private String rootMessage(Throwable throwable) {
        Throwable cursor = throwable;
        while (cursor.getCause() != null) {
            cursor = cursor.getCause();
        }
        return cursor.getMessage() == null ? throwable.getMessage() : cursor.getMessage();
    }
}
