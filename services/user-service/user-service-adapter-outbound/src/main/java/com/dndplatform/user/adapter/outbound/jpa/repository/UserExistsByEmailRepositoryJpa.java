package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.repository.UserExistsByEmailRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserExistsByEmailRepositoryJpa implements UserExistsByEmailRepository, PanacheRepository<UserEntity> {

    @Override
    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
}
