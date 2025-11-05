package com.fastline.authservice.domain.repository;

import com.fastline.authservice.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);
    Optional<User> findById(Long id);

    void save(User user);
}
