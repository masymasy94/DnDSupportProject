package com.dndplatform.user.domain;

import com.dndplatform.user.domain.model.User;

import java.util.List;

public interface UserSearchService {
    List<User> search(String query, int page, int size);
    long countByQuery(String query);
}
