package com.example.app.controller;

import java.time.LocalDateTime;

public class OtpResponse {
    private String otpCode;
    private LocalDateTime expirationTime;

    // Constructor, Getters, and Setters are required for Jackson serialization

    public OtpResponse(String otpCode, LocalDateTime expirationTime) {
        this.otpCode = otpCode;
        this.expirationTime = expirationTime;
    }

    // Getters
    public String getOtpCode() {
        return otpCode;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    // Setters
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}