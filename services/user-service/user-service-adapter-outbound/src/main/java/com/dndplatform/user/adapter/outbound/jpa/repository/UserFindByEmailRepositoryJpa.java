package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByEmailRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserFindByEmailRepositoryJpa implements UserFindByEmailRepository {

    private final UserMapper mapper;
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserFindByEmailRepositoryJpa(UserMapper mapper, UserPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return panacheRepository.findByEmail(email).map(mapper);
    }
}
