package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordResetPanacheRepository implements PanacheRepository<PasswordResetEntity> {
    // Inherits all Panache methods: persist, find, update, delete, etc.
}
