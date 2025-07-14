package com.osproject.microservices.userservice.repository;


import com.osproject.microservices.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);
    boolean existsByEmail(String email);

    boolean existsById(long id);
}
