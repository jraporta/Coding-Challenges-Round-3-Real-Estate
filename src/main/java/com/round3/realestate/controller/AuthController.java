package com.round3.realestate.controller;

import com.round3.realestate.entity.User;
import com.round3.realestate.payload.LoginRequest;
import com.round3.realestate.payload.LoginSuccessfulResponse;
import com.round3.realestate.payload.RegistrationRequest;
import com.round3.realestate.payload.RegistrationResponse;
import com.round3.realestate.service.AuthService;
import com.round3.realestate.service.EmploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmploymentService employmentService;

    public AuthController(AuthService authService, EmploymentService employmentService) {
        this.authService = authService;
        this.employmentService = employmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest req) {
        User user = authService.registerUser(req.getEmail(), req.getUsername(), req.getPassword());
        employmentService.initiate(user);
        return ResponseEntity.ok(new RegistrationResponse(true, "User successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessfulResponse> login(@RequestBody LoginRequest req) {
        String token = authService.loginUser(req.getUsernameOrEmail(), req.getPassword());
        return ResponseEntity.ok(new LoginSuccessfulResponse(token));
    }

}
