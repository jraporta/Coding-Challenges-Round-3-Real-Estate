package com.round3.realestate.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MortgageRejectedException extends RuntimeException {

    private final BigDecimal netMonthly;
    private final BigDecimal monthlyPayment;
    private final String allowedPercentage;

    public MortgageRejectedException(String message, BigDecimal netMonthly, BigDecimal monthlyPayment,
                                     String allowedPercentage) {
        super(message);
        this.netMonthly = netMonthly;
        this.monthlyPayment = monthlyPayment;
        this.allowedPercentage = allowedPercentage;
    }
}
