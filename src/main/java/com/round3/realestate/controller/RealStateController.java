package com.round3.realestate.controller;

import com.round3.realestate.payload.RealStateData;
import com.round3.realestate.payload.ScrapeRequest;
import com.round3.realestate.payload.ScrapeResponse;
import com.round3.realestate.service.ScrapeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealStateController {

    private final ScrapeService scrapeService;

    public RealStateController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @PostMapping("/api/scrape")
    public ResponseEntity<ScrapeResponse> scrape(@RequestBody ScrapeRequest req) {
        RealStateData data = scrapeService.scrape(req.getUrl());
        if (Boolean.TRUE.equals(req.getStore())) {
            return ResponseEntity.ok(new ScrapeResponse(data, true));
        }
        return ResponseEntity.ok(new ScrapeResponse(data, false));
    }

}
