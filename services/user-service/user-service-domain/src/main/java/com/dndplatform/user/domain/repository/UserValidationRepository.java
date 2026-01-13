package com.dndplatform.user.domain.repository;

public interface UserValidationRepository {
    void existsByUsernameOrEmail(String username, String email);
}
