package com.round3.realestate.payload;

import lombok.Data;

@Data
public class ScrapeRequest {

    private String url;
    private Boolean store;

}
