package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapeResponse {

    private RealStateData data;

    private boolean saved;

}
