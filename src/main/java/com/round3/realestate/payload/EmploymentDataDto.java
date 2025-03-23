package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmploymentDataDto {

    private Long id;
    private UserResponse user;
    private String contract;
    private double salary;
    private double netMonthly;
    private String employmentStatus;

}
