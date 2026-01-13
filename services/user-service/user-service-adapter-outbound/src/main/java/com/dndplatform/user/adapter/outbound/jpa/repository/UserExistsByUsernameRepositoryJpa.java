package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.repository.UserExistsByUsernameRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserExistsByUsernameRepositoryJpa implements UserExistsByUsernameRepository, PanacheRepository<UserEntity> {

    @Override
    public boolean existsByUsername(String username) {
        return count("username", username) > 0;
    }
}
