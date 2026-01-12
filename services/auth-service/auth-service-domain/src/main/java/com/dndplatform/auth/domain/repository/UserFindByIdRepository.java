package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.User;

import java.util.Optional;

public interface UserFindByIdRepository {
    Optional<User> findById(long userId);
}
