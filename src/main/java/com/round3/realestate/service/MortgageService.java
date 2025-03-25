package com.round3.realestate.service;

import com.round3.realestate.entity.Mortgage;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.MortgageAcceptedResponse;

import java.util.List;

public interface MortgageService {
    MortgageAcceptedResponse approveMortgage(Long propertyId, Integer years, User user);
    List<Mortgage> getMortgages(User user);
}
