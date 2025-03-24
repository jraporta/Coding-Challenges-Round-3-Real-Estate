package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MortgageRejectedResponse {

    private Boolean approved;
    private BigDecimal netMonthly;
    private BigDecimal monthlyPayment;
    private String allowedPercentage;
    private String message;

}
