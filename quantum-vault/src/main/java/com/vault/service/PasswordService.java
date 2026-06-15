package com.vault.service;

import com.vault.PasswordGenerator;
import com.vault.PasswordStrengthAnalyzer;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordGenerator passwordGenerator;

    public PasswordService() {
        this.passwordGenerator = new PasswordGenerator();
    }

    public String generate(int length, boolean uppercase,
                           boolean lowercase, boolean numbers,
                           boolean symbols, String customCharacters) {
        return passwordGenerator.generatePassword(
                length, uppercase, lowercase,
                numbers, symbols, customCharacters);
    }

    public String getStrength(String password) {
        return PasswordStrengthAnalyzer.getStrength(password);
    }

    public String getCrackTime(String password) {
        return PasswordStrengthAnalyzer.getCrackTime(password);
    }
}