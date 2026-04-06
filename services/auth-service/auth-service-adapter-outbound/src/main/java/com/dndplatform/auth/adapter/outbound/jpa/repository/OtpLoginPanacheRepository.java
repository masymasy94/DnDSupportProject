package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OtpLoginPanacheRepository implements PanacheRepository<OtpLoginEntity> {
    // Inherits all Panache methods: persist, find, update, delete, etc.
}
