package com.round3.realestate.controller;

import com.round3.realestate.payload.RealStateData;
import com.round3.realestate.payload.ScrapeRequest;
import com.round3.realestate.payload.ScrapeResponse;
import com.round3.realestate.service.PropertyService;
import com.round3.realestate.service.ScrapeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealStateController {

    private final ScrapeService scrapeService;

    private final PropertyService propertyService;

    public RealStateController(ScrapeService scrapeService, PropertyService propertyService) {
        this.scrapeService = scrapeService;
        this.propertyService = propertyService;
    }

    @PostMapping("/api/scrape")
    public ResponseEntity<ScrapeResponse> scrapeProperty(@RequestBody ScrapeRequest req) {
        boolean saved = false;
        RealStateData data = scrapeService.scrape(req.getUrl());
        if (Boolean.TRUE.equals(req.getStore())) {
            propertyService.createProperty(data);
            saved = true;
        }
        return ResponseEntity.ok(new ScrapeResponse(data, saved));
    }

}
