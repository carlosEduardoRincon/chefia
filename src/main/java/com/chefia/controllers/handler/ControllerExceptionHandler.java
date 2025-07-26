package com.chefia.controllers.handler;

import com.chefia.dtos.UserNotFoundDTO;
import com.chefia.services.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundDTO> handlerUserNotFoundException(UserNotFoundException userNotFoundException) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new UserNotFoundDTO(userNotFoundException.getMessage(), status.value()));
    }
}
