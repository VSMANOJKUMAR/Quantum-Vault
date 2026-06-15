package com.vault.controller;

import com.vault.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        String token = authService.register(
                request.email(),
                request.name(),
                request.passwordHash(),
                request.kdfSalt()
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", request.email()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(
                request.email(),
                request.passwordHash()
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", request.email()
        ));
    }

    @GetMapping("/salt/{email}")
    public ResponseEntity<?> getSalt(@PathVariable String email) {
        String salt = authService.getKdfSalt(email);
        return ResponseEntity.ok(Map.of("kdfSalt", salt));
    }

    record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank String name,
            @NotBlank String passwordHash,
            @NotBlank String kdfSalt
    ) {}

    record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String passwordHash
    ) {}
}