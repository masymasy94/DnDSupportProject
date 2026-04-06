package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByUsernameRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserFindByUsernameRepositoryJpa implements UserFindByUsernameRepository {

    private final UserMapper mapper;
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserFindByUsernameRepositoryJpa(UserMapper mapper, UserPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return panacheRepository.findByUsernameOrEmail(username).map(mapper);
    }
}
