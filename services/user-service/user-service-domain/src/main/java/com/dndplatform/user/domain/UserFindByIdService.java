package com.dndplatform.user.domain;

import com.dndplatform.user.domain.model.User;

import java.util.Optional;

public interface UserFindByIdService {
    Optional<User> findById(long id);
}
