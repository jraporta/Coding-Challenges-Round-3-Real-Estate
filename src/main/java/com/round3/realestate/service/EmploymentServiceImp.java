package com.round3.realestate.service;

import com.round3.realestate.entity.EmploymentData;
import com.round3.realestate.entity.User;
import com.round3.realestate.exception.CustomBadRequestException;
import com.round3.realestate.repository.EmploymentDataRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EmploymentServiceImp implements EmploymentService{

    private final EmploymentDataRepository employmentDataRepository;

    public EmploymentServiceImp(EmploymentDataRepository employmentDataRepository) {
        this.employmentDataRepository = employmentDataRepository;
    }

    @Override
    public EmploymentData initiate(User user) {
        EmploymentData data = new EmploymentData(
                null,
                null,
                "unemployed",
                new BigDecimal(0),
                new BigDecimal(0),
                user);
        return employmentDataRepository.save(data);
    }

    @Override
    public EmploymentData update(String contract, Double salary, User user) {
        if(salary != null && salary < 0) {
            throw new CustomBadRequestException("Salary can't be negative.");
        }

        EmploymentData data = employmentDataRepository.findByUserId(user.getId()).orElse(new EmploymentData());

        data.setUser(user);
        if(contract != null) {
            data.setContract(contract);
        }
        if(salary != null && salary >= 0) {
            data.setSalary(BigDecimal.valueOf(salary));
            data.setNetMonthly(calculateMonthlyNetSalary(salary));
        }
        data.setEmploymentStatus(
                data.getContract() == null || data.getSalary().compareTo(BigDecimal.valueOf(0)) <= 0 ?
                        "unemployed" : "employed");

        return employmentDataRepository.save(data);
    }

    @Override
    public EmploymentData getEmployment(User user) {
        return employmentDataRepository.findByUserId(user.getId()).orElseGet(() -> initiate(user));
    }

    private BigDecimal calculateMonthlyNetSalary(Double annualGrossSalary) {
        BigDecimal salary = BigDecimal.valueOf(annualGrossSalary);
        return salary.subtract(calculateTax(salary))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
    }

    private BigDecimal calculateTax(BigDecimal salary) {

        BigDecimal[][] taxBrackets = {
                {BigDecimal.valueOf(12450), BigDecimal.valueOf(0.19)},
                {BigDecimal.valueOf(20200), BigDecimal.valueOf(0.24)},
                {BigDecimal.valueOf(35200), BigDecimal.valueOf(0.30)},
                {BigDecimal.valueOf(60000), BigDecimal.valueOf(0.37)},
                {BigDecimal.valueOf(300000), BigDecimal.valueOf(0.45)},
                {BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal.valueOf(0.50)}
        };

        BigDecimal tax = BigDecimal.valueOf(0);
        BigDecimal previousLimit = BigDecimal.valueOf(0);

        for (BigDecimal[] bracket : taxBrackets) {

            if (salary.compareTo(previousLimit) <= 0) {
                break;
            }

            BigDecimal limit = bracket[0];
            BigDecimal rate = bracket[1];

            BigDecimal taxableAmount = salary.min(limit).subtract(previousLimit);
            tax = tax.add(taxableAmount.multiply(rate));

            previousLimit = limit;
        }

        return tax.setScale(2, RoundingMode.DOWN);
    }


}
