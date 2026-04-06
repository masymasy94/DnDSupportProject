package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserPanacheRepository implements PanacheRepository<UserEntity> {

    public Optional<UserEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<UserEntity> findByIdOptional(long id) {
        return find("id", id).firstResultOptional();
    }

    public Optional<UserEntity> findByUsernameOrEmail(String usernameOrEmail) {
        return find("username = ?1 or email = ?2", usernameOrEmail, usernameOrEmail).firstResultOptional();
    }

    public List<UserEntity> findAllPaged(int page, int size) {
        return findAll(Sort.by("username"))
                .page(Page.of(page, size))
                .list();
    }

    public List<UserEntity> searchPaged(String pattern, int page, int size) {
        return find("lower(username) like ?1 or lower(email) like ?1",
                Sort.by("username"), pattern)
                .page(Page.of(page, size))
                .list();
    }

    public long countByQuery(String pattern) {
        return count("lower(username) like ?1 or lower(email) like ?1", pattern);
    }

    public long countByUsernameOrEmail(String username, String email) {
        return count("username = ?1 or email = ?2", username, email);
    }

    public int updatePassword(long id, String passwordHash, java.time.LocalDateTime updatedAt) {
        return update("passwordHash = ?1, updatedAt = ?2 where id = ?3", passwordHash, updatedAt, id);
    }
}
