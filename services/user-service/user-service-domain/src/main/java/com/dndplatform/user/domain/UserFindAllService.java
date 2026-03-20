package com.dndplatform.user.domain;

import com.dndplatform.user.domain.model.User;

import java.util.List;

public interface UserFindAllService {
    List<User> findAll(int page, int size);
    long count();
}
