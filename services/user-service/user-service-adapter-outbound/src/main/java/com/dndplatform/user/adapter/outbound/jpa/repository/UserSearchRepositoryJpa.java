package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserSearchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserSearchRepositoryJpa implements UserSearchRepository {

    private final UserMapper mapper;
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserSearchRepositoryJpa(UserMapper mapper, UserPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<User> search(String query, int page, int size) {
        String pattern = "%" + query.toLowerCase() + "%";
        return panacheRepository.searchPaged(pattern, page, size)
                .stream()
                .map(mapper)
                .toList();
    }

    @Override
    public long countByQuery(String query) {
        String pattern = "%" + query.toLowerCase() + "%";
        return panacheRepository.countByQuery(pattern);
    }
}
