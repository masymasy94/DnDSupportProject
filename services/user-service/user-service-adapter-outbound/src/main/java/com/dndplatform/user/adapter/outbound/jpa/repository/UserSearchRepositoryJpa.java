package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserSearchRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserSearchRepositoryJpa implements UserSearchRepository {

    private final UserMapper mapper;

    @Inject
    public UserSearchRepositoryJpa(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<User> search(String query, int page, int size) {
        String pattern = "%" + query.toLowerCase() + "%";
        return UserEntity.find("lower(username) like ?1 or lower(email) like ?1",
                        Sort.by("username"), pattern)
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(e -> mapper.apply((UserEntity) e))
                .toList();
    }

    @Override
    public long countByQuery(String query) {
        String pattern = "%" + query.toLowerCase() + "%";
        return UserEntity.count("lower(username) like ?1 or lower(email) like ?1", pattern);
    }
}
