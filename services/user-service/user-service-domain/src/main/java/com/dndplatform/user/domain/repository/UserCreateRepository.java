package com.dndplatform.user.domain.repository;

import com.dndplatform.user.domain.model.User;

public interface UserCreateRepository {
    User create(User user);
}
