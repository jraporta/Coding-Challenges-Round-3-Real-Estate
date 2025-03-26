package com.round3.realestate.service;

import com.round3.realestate.entity.User;

public interface AuthService {
    User registerUser(String email, String username, String password);

    String loginUser(String usernameOrEmail, String password);
}
