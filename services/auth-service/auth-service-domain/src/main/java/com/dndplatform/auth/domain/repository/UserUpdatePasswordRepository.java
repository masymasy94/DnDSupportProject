package com.dndplatform.auth.domain.repository;

public interface UserUpdatePasswordRepository {
    void updatePassword(long userId, String newPassword);
}
