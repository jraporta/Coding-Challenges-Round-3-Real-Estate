package com.round3.realestate.controller;

import com.round3.realestate.entity.EmploymentData;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.*;
import com.round3.realestate.service.EmploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employment")
public class EmploymentController {

    private final EmploymentService employmentService;

    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @PostMapping
    public ResponseEntity<EmploymentPostResponse> addEmploymentData(
            @RequestBody EmploymentDataRequest req,
            @AuthenticationPrincipal User user) {
        EmploymentData data = employmentService.update(req.getContract(), req.getSalary(), user);
        return ResponseEntity.ok(
                new EmploymentPostResponse(
                        new EmploymentDataDto(data.getId(),
                                new UserResponse(user.getId(), user.getUsername(), user.getEmail()),
                                data.getContract(),
                                data.getSalary().doubleValue(),
                                data.getNetMonthly().doubleValue(),
                                data.getEmploymentStatus()),
                        "Employment data updated successfully."
                ));
    }

    @PatchMapping
    public ResponseEntity<EmploymentPatchResponse> updateEmploymentData(
            @RequestBody EmploymentDataRequest req,
            @AuthenticationPrincipal User user) {
        EmploymentData data = employmentService.update(req.getContract(), req.getSalary(), user);
        return ResponseEntity.ok(
                new EmploymentPatchResponse(
                        new EmploymentDataDto(data.getId(),
                                new UserResponse(user.getId(), user.getUsername(), user.getEmail()),
                                data.getContract(),
                                data.getSalary().doubleValue(),
                                data.getNetMonthly().doubleValue(),
                                data.getEmploymentStatus()),
                        "Employment data updated successfully.",
                        true
                ));
    }

}
