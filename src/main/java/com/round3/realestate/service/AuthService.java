package com.round3.realestate.service;

import com.round3.realestate.entity.User;
import com.round3.realestate.exception.RegistrationException;
import com.round3.realestate.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String email, String username, String password) {
        if(userRepository.existsByUsername(username)){
            throw new RegistrationException("The username already exists");
        }
        if(userRepository.existsByEmail(email)){
            throw new RegistrationException("Email already exists");
        }
        User newUser = new User(null, username, email, passwordEncoder.encode(password));
        userRepository.save(newUser);
    }
}
