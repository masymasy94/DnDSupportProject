package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.User;

public interface UserFindByIdRepository {
    User findById(long userId);
}
