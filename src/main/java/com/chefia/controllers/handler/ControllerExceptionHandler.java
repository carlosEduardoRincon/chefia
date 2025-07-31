package com.chefia.controllers.handler;

import com.chefia.dtos.DefaultExceptionDTO;
import com.chefia.dtos.ValidationErrorDTO;
import com.chefia.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "users_email_key", "E-mail already exists",
            "users_login_key", "Login already exists"
    );

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DefaultExceptionDTO> handlerUserNotFoundException(UserNotFoundException userNotFoundException) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new DefaultExceptionDTO(userNotFoundException.getMessage(), status.value()));
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<DefaultExceptionDTO> handlerUserNotFoundException(AddressNotFoundException addressNotFoundException) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new DefaultExceptionDTO(addressNotFoundException.getMessage(), status.value()));
    }

    @ExceptionHandler(UserEmailAlreadyExist.class)
    public ResponseEntity<DefaultExceptionDTO> handlerUserEmailAlreadyExist(UserEmailAlreadyExist userEmailAlreadyExist) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new DefaultExceptionDTO(userEmailAlreadyExist.getMessage(), status.value()));
    }

    @ExceptionHandler(UserNotStrongPassword.class)
    public ResponseEntity<DefaultExceptionDTO> handlerUserEmailAlreadyExist(UserNotStrongPassword userNotStrongPassword) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new DefaultExceptionDTO(userNotStrongPassword.getMessage(), status.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handlerMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (var error:  methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return ResponseEntity.status(status.value()).body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (var error:  ex.getConstraintViolations()) {
            errors.add(error.getPropertyPath().toString() + ": " + error.getMessage());
        }
        return ResponseEntity.status(status.value()).body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ValidationErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException hibernateEx) {
            var constraintName = hibernateEx.getConstraintName();

            if (constraintName != null) {
                var message = CONSTRAINT_MESSAGES.entrySet().stream()
                        .filter(entry -> constraintName.toLowerCase().contains(entry.getKey().toLowerCase()))
                        .map(Map.Entry::getValue)
                        .findFirst().get();
                errors.add(message);
            }
        }

        return ResponseEntity.status(status.value()).body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(PasswordNotMatch.class)
    public ResponseEntity<DefaultExceptionDTO> handlerPasswordNotMatch(PasswordNotMatch passwordNotMatch) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new DefaultExceptionDTO(passwordNotMatch.getMessage(), status.value()));
    }

    @ExceptionHandler(PasswordAlreadyUsed.class)
    public ResponseEntity<DefaultExceptionDTO> handlerPasswordAlreadyUsed(PasswordAlreadyUsed passwordAlreadyUsed) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new DefaultExceptionDTO(passwordAlreadyUsed.getMessage(), status.value()));
    }
}
