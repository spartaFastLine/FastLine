package com.fastline.authservice.domain.repository;

import com.fastline.authservice.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);
    Optional<User> findById(Long id);
    List<User> findUsers(String username, UUID hubId, String role, String status);
    void save(User user);
}
