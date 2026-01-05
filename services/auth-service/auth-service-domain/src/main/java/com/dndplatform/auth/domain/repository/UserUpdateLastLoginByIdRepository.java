package com.dndplatform.auth.domain.repository;

public interface UserUpdateLastLoginByIdRepository {
    void updateLastLoginById(long userId);
}
