package com.dndplatform.auth.domain.repository;

public interface OtpLoginEmailSendRepository {
    void sendOtpLoginEmail(String email, String otpCode);
}
