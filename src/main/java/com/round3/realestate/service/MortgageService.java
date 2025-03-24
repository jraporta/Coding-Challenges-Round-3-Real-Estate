package com.round3.realestate.service;

import com.round3.realestate.entity.User;
import com.round3.realestate.payload.MortgageAcceptedResponse;

public interface MortgageService {
    MortgageAcceptedResponse approveMortgage(Long propertyId, Integer years, User user);
}
