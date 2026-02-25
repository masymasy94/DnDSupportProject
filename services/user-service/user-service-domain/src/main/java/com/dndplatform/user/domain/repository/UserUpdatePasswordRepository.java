package com.dndplatform.user.domain.repository;

public interface UserUpdatePasswordRepository {
    void updatePassword(long id, String passwordHash);
}
