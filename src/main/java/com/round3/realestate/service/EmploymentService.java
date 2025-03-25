package com.round3.realestate.service;

import com.round3.realestate.entity.EmploymentData;
import com.round3.realestate.entity.User;

public interface EmploymentService {

    EmploymentData initiate(User user);

    EmploymentData update(String contract, Double salary, User user);

    EmploymentData getEmployment(User user);

}
