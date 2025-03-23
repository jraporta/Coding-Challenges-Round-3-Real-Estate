package com.round3.realestate.service;

import com.round3.realestate.entity.EmploymentData;
import com.round3.realestate.entity.User;

public interface EmploymentService {

    void initiate(User user);

    EmploymentData update(String contract, Double salary, User user);

}
