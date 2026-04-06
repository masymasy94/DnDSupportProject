package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserFindByIdRepositoryJpa implements UserFindByIdRepository {

    private final UserMapper mapper;
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserFindByIdRepositoryJpa(UserMapper mapper, UserPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<User> findById(long id) {
        return panacheRepository.findByIdOptional(id).map(mapper);
    }
}
