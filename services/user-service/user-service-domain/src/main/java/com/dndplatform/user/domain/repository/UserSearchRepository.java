package com.dndplatform.user.domain.repository;

import com.dndplatform.user.domain.model.User;

import java.util.List;

public interface UserSearchRepository {
    List<User> search(String query, int page, int size);
    long countByQuery(String query);
}
