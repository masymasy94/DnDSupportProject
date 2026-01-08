package com.dndplatform.user.domain.repository;

public interface UserExistsByUsernameRepository {
    boolean existsByUsername(String username);
}
