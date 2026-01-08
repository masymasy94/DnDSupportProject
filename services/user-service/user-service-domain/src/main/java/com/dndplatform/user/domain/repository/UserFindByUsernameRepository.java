package com.dndplatform.user.domain.repository;

import com.dndplatform.user.domain.model.User;

import java.util.Optional;

public interface UserFindByUsernameRepository {
    Optional<User> findByUsername(String username);
}
