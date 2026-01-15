package com.dndplatform.user.domain.repository;

import com.dndplatform.user.domain.model.User;

import java.util.Optional;

public interface UserFindByIdRepository {
    Optional<User> findById(long id);
}
