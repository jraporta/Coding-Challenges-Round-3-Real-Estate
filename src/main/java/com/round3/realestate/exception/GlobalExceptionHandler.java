package com.round3.realestate.exception;

import com.round3.realestate.payload.LoginFailedResponse;
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

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<LoginFailedResponse> handleLoginException(LoginException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginFailedResponse(ex.getMessage()));
    }

}
