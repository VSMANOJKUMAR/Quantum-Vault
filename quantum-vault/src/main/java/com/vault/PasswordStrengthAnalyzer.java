package com.vault;

import java.util.regex.Pattern;

public class PasswordStrengthAnalyzer {

    public static String getStrength(String password) {
        int length = password.length();
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("\\d").matcher(password).find();
        boolean hasSpecial = Pattern.compile(
                        "[!@#$%^&*~`+=\\-_?/<>,.:\\;()\\[\\]{}]")
                .matcher(password).find();

        double possibleChars = 0;
        if (hasUpper) possibleChars += 26;
        if (hasLower) possibleChars += 26;
        if (hasDigit) possibleChars += 10;
        if (hasSpecial) possibleChars += 32;

        double entropy = length * (Math.log(possibleChars) / Math.log(2));

        return getStrengthRating(entropy);
    }

    public static String getCrackTime(String password) {
        int length = password.length();
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("\\d").matcher(password).find();
        boolean hasSpecial = Pattern.compile(
                        "[!@#$%^&*~`+=\\-_?/<>,.:\\;()\\[\\]{}]")
                .matcher(password).find();

        double possibleChars = 0;
        if (hasUpper) possibleChars += 26;
        if (hasLower) possibleChars += 26;
        if (hasDigit) possibleChars += 10;
        if (hasSpecial) possibleChars += 32;

        double entropy = length * (Math.log(possibleChars) / Math.log(2));
        double secondsToCrack = Math.pow(2, entropy) / 1_000_000_000;

        return formatCrackingTime(secondsToCrack);
    }

    private static String formatCrackingTime(double seconds) {
        if (seconds < 60) {
            return String.format("%.2f seconds", seconds);
        } else if (seconds < 3600) {
            return String.format("%.2f minutes", seconds / 60);
        } else if (seconds < 86400) {
            return String.format("%.2f hours", seconds / 3600);
        } else if (seconds < 31536000) {
            return String.format("%.2f days", seconds / 86400);
        } else {
            return String.format("%.2f years", seconds / 31536000);
        }
    }

    private static String getStrengthRating(double entropy) {
        if (entropy < 28) return "Very Weak";
        if (entropy < 36) return "Weak";
        if (entropy < 60) return "Reasonable";
        if (entropy < 128) return "Strong";
        return "Very Strong";
    }
}