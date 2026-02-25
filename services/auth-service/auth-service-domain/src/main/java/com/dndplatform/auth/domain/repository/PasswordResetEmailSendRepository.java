package com.dndplatform.auth.domain.repository;

public interface PasswordResetEmailSendRepository {
    void sendResetEmail(String email, String token);
}
