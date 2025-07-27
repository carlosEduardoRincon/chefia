package com.chefia.controllers.handler;

import com.chefia.dtos.DefaultExceptionDTO;
import com.chefia.dtos.ValidationErrorDTO;
import com.chefia.services.exceptions.AddressNotFoundException;
import com.chefia.services.exceptions.UserEmailAlreadyExist;
import com.chefia.services.exceptions.UserNotFoundException;
import com.chefia.services.exceptions.UserNotStrongPassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

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
}
