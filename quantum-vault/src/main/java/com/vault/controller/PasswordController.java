package com.vault.controller;

import com.vault.service.PasswordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(
            @Valid @RequestBody GenerateRequest request) {

        String password = passwordService.generate(
                request.length(),
                request.uppercase(),
                request.lowercase(),
                request.numbers(),
                request.symbols(),
                request.customCharacters()
        );

        return ResponseEntity.ok(Map.of(
                "password", password,
                "strength", passwordService.getStrength(password),
                "crackTime", passwordService.getCrackTime(password)
        ));
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @Valid @RequestBody AnalyzeRequest request) {

        return ResponseEntity.ok(Map.of(
                "strength", passwordService.getStrength(request.password()),
                "crackTime", passwordService.getCrackTime(request.password())
        ));
    }

    record GenerateRequest(
            @Min(8) @Max(128) int length,
            boolean uppercase,
            boolean lowercase,
            boolean numbers,
            boolean symbols,
            String customCharacters
    ) {}

    record AnalyzeRequest(
            @NotBlank String password
    ) {}
}