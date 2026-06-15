package com.vault.service;

import com.vault.model.User;
import com.vault.repository.UserRepository;
import com.vault.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(String email, String name,
                           String passwordHash, String kdfSalt) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .email(email)
                .name(name)
                .passwordHash(passwordEncoder.encode(passwordHash))
                .kdfSalt(kdfSalt)
                .build();

        userRepository.save(user);
        return jwtUtil.generateToken(email);
    }

    public String login(String email, String passwordHash) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(passwordHash, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(email);
    }

    public String getKdfSalt(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return user.getKdfSalt();
    }
}