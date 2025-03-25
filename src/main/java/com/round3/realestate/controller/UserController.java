package com.round3.realestate.controller;

import com.round3.realestate.entity.User;
import com.round3.realestate.payload.DashboardResponse;
import com.round3.realestate.payload.UserResponse;
import com.round3.realestate.service.EmploymentService;
import com.round3.realestate.service.MortgageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final EmploymentService employmentService;
    private final MortgageService mortgageService;

    public UserController(EmploymentService employmentService, MortgageService mortgageService) {
        this.employmentService = employmentService;
        this.mortgageService = mortgageService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> checkUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new DashboardResponse(
                mortgageService.getMortgages(user),
                employmentService.getEmployment(user))
        );
    }

}
