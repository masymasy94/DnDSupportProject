package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.auth.domain.repository.UserUpdateLastLoginByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
@Transactional
public class UserUpdateLastLoginByIdRepositoryJpa implements UserUpdateLastLoginByIdRepository, PanacheRepository<UserEntity> {
    @Override
    public void updateLastLoginById(long userId) {
        var now = LocalDateTime.now();
        update("lastLogin = ?1 where id = ?2", now, userId);
    }
}
