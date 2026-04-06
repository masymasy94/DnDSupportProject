package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefreshTokenPanacheRepository implements PanacheRepository<RefreshTokenEntity> {
    // Inherits all Panache methods: persist, find, update, delete, etc.
}
