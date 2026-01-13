package com.dndplatform.user.domain.repository;

public interface UserExistsByEmailRepository {
    boolean existsByEmail(String email);
}
