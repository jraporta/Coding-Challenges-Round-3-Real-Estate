package com.round3.realestate.exception;

import com.round3.realestate.payload.RegistrationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<RegistrationResponse> handleRegistrationException(RegistrationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(false, ex.getMessage()));
    }

}
