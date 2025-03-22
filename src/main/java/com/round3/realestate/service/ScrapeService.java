package com.round3.realestate.service;

import com.round3.realestate.payload.RealStateData;

public interface ScrapeService {
    RealStateData scrape(String url);
}
