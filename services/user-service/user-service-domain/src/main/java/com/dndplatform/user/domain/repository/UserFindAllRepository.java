package com.dndplatform.user.domain.repository;

import com.dndplatform.user.domain.model.User;

import java.util.List;

public interface UserFindAllRepository {
    List<User> findAll(int page, int size);
    long count();
}
