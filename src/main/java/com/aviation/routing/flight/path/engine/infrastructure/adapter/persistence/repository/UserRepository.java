package com.aviation.routing.flight.path.engine.infrastructure.adapter.persistence.repository;

import java.util.Optional;

import com.aviation.routing.flight.path.engine.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
