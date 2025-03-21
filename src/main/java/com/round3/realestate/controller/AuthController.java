package com.round3.realestate.controller;

import com.round3.realestate.payload.LoginRequest;
import com.round3.realestate.payload.LoginSuccessfulResponse;
import com.round3.realestate.payload.RegistrationRequest;
import com.round3.realestate.payload.RegistrationResponse;
import com.round3.realestate.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest req) {
        authService.registerUser(req.getEmail(), req.getUsername(), req.getPassword());
        return ResponseEntity.ok(new RegistrationResponse(true, "User successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessfulResponse> login(@RequestBody LoginRequest req) {
        String token = authService.loginUser(req.getUsernameOrEmail(), req.getPassword());
        return ResponseEntity.ok(new LoginSuccessfulResponse(token));
    }

}
