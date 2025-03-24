package com.round3.realestate.payload;

import lombok.Data;

@Data
public class MortgageRequest {

    private Long propertyId;
    private Integer years;

}
