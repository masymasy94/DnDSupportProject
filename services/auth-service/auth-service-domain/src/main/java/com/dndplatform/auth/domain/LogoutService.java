package com.dndplatform.auth.domain;

public interface LogoutService {
    void logout(String token, long userId);
}
