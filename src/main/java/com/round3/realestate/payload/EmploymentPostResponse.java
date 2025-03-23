package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmploymentPostResponse {

    private EmploymentDataDto employmentData;
    private String message;

}
