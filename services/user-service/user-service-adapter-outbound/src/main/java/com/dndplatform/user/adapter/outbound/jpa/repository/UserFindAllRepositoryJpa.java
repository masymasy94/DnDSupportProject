package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserFindAllRepositoryJpa implements UserFindAllRepository {

    private final UserMapper mapper;
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserFindAllRepositoryJpa(UserMapper mapper, UserPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<User> findAll(int page, int size) {
        return panacheRepository.findAllPaged(page, size)
                .stream()
                .map(mapper)
                .toList();
    }

    @Override
    public long count() {
        return panacheRepository.count();
    }
}
