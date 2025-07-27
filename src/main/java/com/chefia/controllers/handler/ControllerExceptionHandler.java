package com.chefia.controllers.handler;

import com.chefia.dtos.DefaultExceptionDTO;
import com.chefia.dtos.ValidationErrorDTO;
import com.chefia.services.exceptions.AddressNotFoundException;
import com.chefia.services.exceptions.UserEmailAlreadyExist;
import com.chefia.services.exceptions.UserNotFoundException;
import com.chefia.services.exceptions.UserNotStrongPassword;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    // todo: evolução trocar por uma enum para conseguir definir o campo e assim padronizar a resposta
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
}
