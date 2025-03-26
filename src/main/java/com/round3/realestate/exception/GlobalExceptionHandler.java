package com.round3.realestate.exception;

import com.round3.realestate.payload.*;
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

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<String> handleCustomBadRequestException(CustomBadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MortgageException.class)
    public ResponseEntity<MortgageExceptionResponse> handleMortgageException(MortgageException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MortgageExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(MortgageRejectedException.class)
    public ResponseEntity<MortgageRejectedResponse> handleMortgageRejected(MortgageRejectedException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MortgageRejectedResponse(false,
                        ex.getNetMonthly(),
                        ex.getMonthlyPayment(),
                        ex.getAllowedPercentage(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(AuctionException.class)
    public ResponseEntity<AuctionErrorResponse> handleAuctionException(AuctionException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuctionErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(BadUrlException.class)
    public ResponseEntity<AuctionErrorResponse> handleBadUrlException(BadUrlException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuctionErrorResponse(ex.getMessage()));
    }
}
