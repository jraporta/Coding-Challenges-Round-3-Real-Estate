package com.round3.realestate.controller;

import com.round3.realestate.entity.User;
import com.round3.realestate.payload.MortgageRequest;
import com.round3.realestate.payload.MortgageAcceptedResponse;
import com.round3.realestate.service.MortgageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mortgage")
public class MortgageController {

    private final MortgageService mortgageService;

    public MortgageController(MortgageService mortgageService) {
        this.mortgageService = mortgageService;
    }

    @PostMapping
    public ResponseEntity<MortgageAcceptedResponse> requestMortgage(
            @RequestBody MortgageRequest req,
            @AuthenticationPrincipal User user) {
        MortgageAcceptedResponse mortgageResponse = mortgageService.approveMortgage(req.getPropertyId(), req.getYears(), user);
        return ResponseEntity.ok(mortgageResponse);
    }

}
