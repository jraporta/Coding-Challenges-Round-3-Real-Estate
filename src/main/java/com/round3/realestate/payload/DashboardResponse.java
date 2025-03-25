package com.round3.realestate.payload;

import com.round3.realestate.entity.EmploymentData;
import com.round3.realestate.entity.Mortgage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardResponse {

    private List<Mortgage> mortgages;
    private EmploymentData employmentData;

}
