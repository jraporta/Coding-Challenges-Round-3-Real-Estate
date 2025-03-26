package com.round3.realestate.service;

import com.round3.realestate.entity.User;
import com.round3.realestate.exception.LoginException;
import com.round3.realestate.exception.RegistrationException;
import com.round3.realestate.repository.UserRepository;
import com.round3.realestate.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User registerUser(String email, String username, String password) {
        if(userRepository.existsByUsername(username)){
            throw new RegistrationException("The username already exists");
        }
        if(userRepository.existsByEmail(email)){
            throw new RegistrationException("Email already exists");
        }
        User newUser = new User(null, username, email, passwordEncoder.encode(password));
        return userRepository.save(newUser);
    }

    @Override
    public String loginUser(String usernameOrEmail, String password) {
        log.info("Checking for username match...");
        User user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        if(user == null) {
            log.info("Checking for email match...");
            user = userRepository.findByEmail(usernameOrEmail).orElseThrow(() -> new LoginException("Unauthorised: Bad credentials"));
        }
        log.info("User found, checking the password...");
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new LoginException("Unauthorised: Bad credentials");
        }
        log.info("Generating the token...");
        return jwtUtil.generateToken(user.getId());
    }
}
