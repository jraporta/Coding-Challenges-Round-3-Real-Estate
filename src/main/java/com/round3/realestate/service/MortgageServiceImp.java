package com.round3.realestate.service;

import com.round3.realestate.entity.EmploymentData;
import com.round3.realestate.entity.Mortgage;
import com.round3.realestate.entity.Property;
import com.round3.realestate.entity.User;
import com.round3.realestate.exception.MortgageException;
import com.round3.realestate.exception.MortgageRejectedException;
import com.round3.realestate.payload.MortgageAcceptedResponse;
import com.round3.realestate.repository.EmploymentDataRepository;
import com.round3.realestate.repository.MortgageRepository;
import com.round3.realestate.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@Slf4j
public class MortgageServiceImp implements MortgageService{

    public static final int MINIMUM_YEARS = 15;
    private static final BigDecimal EXTRA_COSTS = BigDecimal.valueOf(0.15);
    private static final BigDecimal INDEFINITE_THRESHOLD = BigDecimal.valueOf(0.3);
    private static final BigDecimal TEMPORARY_THRESHOLD = BigDecimal.valueOf(0.15);
    private static final BigDecimal ANNUAL_INTEREST_RATE = BigDecimal.valueOf(0.02);
    public static final int MONTHS_PER_YEAR = 12;

    private final MortgageRepository mortgageRepository;
    private final PropertyRepository propertyRepository;
    private final EmploymentDataRepository employmentRepository;

    public MortgageServiceImp(MortgageRepository mortgageRepository, PropertyRepository propertyRepository,
                              EmploymentDataRepository employmentRepository) {
        this.mortgageRepository = mortgageRepository;
        this.propertyRepository = propertyRepository;
        this.employmentRepository = employmentRepository;
    }

    @Override
    public MortgageAcceptedResponse approveMortgage(Long propertyId, Integer years, User user) {
        validateMortgageTerm(years);

        EmploymentData employmentData = getEmploymentData(user);
        validateEmploymentStatus(employmentData);

        Property property = getProperty(propertyId);

        BigDecimal totalCost = calculateTotalCost(property);

        BigDecimal allowedPercentage = getAllowedPercentage(employmentData);
        String allowedPercentageString = formatPercentage(allowedPercentage);
        BigDecimal allowedThreshold = employmentData.getNetMonthly().multiply(allowedPercentage);

        log.info("{} user with earning every month {}€, wants a mortgage for a property worth {}€ to pay in {} years...",
                employmentData.getEmploymentStatus(), employmentData.getNetMonthly(), property.getPrice(), years);
        BigDecimal monthlyPayment = calculateMonthlyPayment(years, totalCost);
        validateAffordability(monthlyPayment, allowedThreshold, employmentData, allowedPercentageString);
        log.info("Mortgage accepted, monthly payment established in {}€", monthlyPayment);

        Mortgage mortgage = saveMortgage(years, user, monthlyPayment, property);
        return buildMortgageResponse(years, mortgage, employmentData, allowedPercentageString);
    }

    private static void validateMortgageTerm(Integer years) {
        if(years < MINIMUM_YEARS) {
            throw new MortgageException("Minimum mortgage term is " + MINIMUM_YEARS + " years.");
        }
    }

    private EmploymentData getEmploymentData(User user) {
        return employmentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new MortgageException("Missing financial information. Please update your employment data."));
    }

    private void validateEmploymentStatus(EmploymentData employmentData) {
        if("unemployed".equalsIgnoreCase(employmentData.getEmploymentStatus())) {
            throw new MortgageException("Missing financial information. Please update your employment data.");
        }
    }

    private Property getProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new MortgageException("Property is unavailable for mortgage."));
    }

    private BigDecimal calculateTotalCost(Property property) {
        return property.getPrice().multiply(BigDecimal.valueOf(1).add(EXTRA_COSTS));
    }

    private BigDecimal getAllowedPercentage(EmploymentData employmentData) {
        return "indefinite".equalsIgnoreCase(employmentData.getContract()) ?
                INDEFINITE_THRESHOLD : TEMPORARY_THRESHOLD;
    }

    private String formatPercentage(BigDecimal allowedPercentage) {
        return allowedPercentage
                .multiply(BigDecimal.valueOf(100))
                .setScale(1, RoundingMode.DOWN)
                .toString() + "%";
    }

    private BigDecimal calculateMonthlyPayment(Integer years, BigDecimal totalCost) {
        BigDecimal monthlyInterestRate = ANNUAL_INTEREST_RATE
                .divide(BigDecimal.valueOf(MONTHS_PER_YEAR), 6, RoundingMode.HALF_UP);
        log.info("The monthly interest rate used is {}% (annual: {}%)", monthlyInterestRate, ANNUAL_INTEREST_RATE);

        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        return totalCost
                .multiply(monthlyInterestRate, mc)
                .divide(BigDecimal.ONE
                                .subtract(
                                        BigDecimal.ONE.divide(
                                                BigDecimal.ONE.add(monthlyInterestRate, mc)
                                                        .pow(years * MONTHS_PER_YEAR, mc),
                                                mc), mc), mc)
                .setScale(2, RoundingMode.UP);
    }

    private void validateAffordability(BigDecimal monthlyPayment, BigDecimal allowedThreshold, EmploymentData employmentData, String allowedPercentageString) {
        if (monthlyPayment.compareTo(allowedThreshold) > 0) {
            throw new MortgageRejectedException(
                    "Mortgage rejected. The monthly payment exceeds the allowed percentage of your net income.",
                    employmentData.getNetMonthly(),
                    monthlyPayment,
                    allowedPercentageString
            );
        }
    }

    private Mortgage saveMortgage(Integer years, User user, BigDecimal monthlyPayment, Property property) {
        Mortgage mortgage = new Mortgage(null, monthlyPayment, MONTHS_PER_YEAR * years, property, user);
        return mortgageRepository.save(mortgage);
    }

    private static MortgageAcceptedResponse buildMortgageResponse(Integer years, Mortgage mortgage, EmploymentData employmentData, String allowedPercentageString) {
        return new MortgageAcceptedResponse(true,
                mortgage.getId(),
                employmentData.getNetMonthly(),
                mortgage.getMonthlyPayment(),
                allowedPercentageString,
                "Mortgage approved.",
                years * MONTHS_PER_YEAR
        );
    }
}
