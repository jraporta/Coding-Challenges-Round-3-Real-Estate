package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmploymentPatchResponse {

    private EmploymentDataDto employmentData;
    private String message;
    private boolean success;

}
